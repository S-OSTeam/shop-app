package com.example.deamhome.data.secure

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.example.deamhome.BuildConfig
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoManager {
    // Android Keystore System Service 에 접근하기 위해
    // AndroidKeyStore라는 프로바이더를 사용해서 현재 저장된 키들의 리스트를 주는 키스토어 객체를 로드함.
    private val keyStore = KeyStore.getInstance(KEYSTORE_TYPE).apply { load(null) }

    // Cipher는 데이터 암호화를 위해, init의 두번째 인자로 주어진 키로부터 퍼블릭키를 얻는다.
    // Cipher는 자바에서 암호화 / 복호화 기능을 위해 제공하는 클래스
    private fun getEncryptionCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            // ENCRYPT_MODE : This is an encryption cipher
            init(Cipher.ENCRYPT_MODE, getKey())
        }
    }

    // Cipher는 데이터 복호화를 위해, init의 인자로 주어진 키로부터 퍼블릭키를 얻는다.
    // 요청마다 다른 IV 를 사용해 보안성을 높일 수 있다
    private fun getDecryptCipherForIv(iv: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        }
    }

    private fun getKey(): SecretKey {
        // secret이라는 별칭을 지닌 키를 가져옴. 가져온건 KeyStore.Entry 타압암. 마치 쉐어드 프리퍼런스의 key와 별칭이 비슷한 개념임.
        // 이걸 SecretKeyEntry 타입으로 캐스팅함.
        val existingKey = keyStore.getEntry(KEYSTORE_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    KEYSTORE_ALIAS,
                    KEY_PURPOSE,
                )
                    .setBlockModes(BLOCK_MODE)
                    .setKeySize(KEY_SIZE * 8)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false) // 지문인식같은 유저의 생체 인식 정보를 요구하는 키를 만들때 사용.
                    .setRandomizedEncryptionRequired(true)
                    .build(),
            )
        }.generateKey()
    }

    // 암호화 메소드
    // 암호화할 대상인 bytes를 암호화해서 outputStream에 쓴다. 이 outputStream은 파일과 연결된 아웃풋 스트림임.
    fun encrypt(bytes: ByteArray, outputStream: OutputStream) {
        val cipher = getEncryptionCipher()
        val iv = cipher.iv
        outputStream.use { opStream ->
            opStream.write(iv) // 초기화 벡터 정보를 먼저 앞에 적고 시작함.
            val inputStream = ByteArrayInputStream(bytes).apply { // 암호화할 정보가 담긴 바이트어레이를 인풋스트림으로 초기화
                val buffer = ByteArray(CHUNK_SIZE)
                while (available() > CHUNK_SIZE) {
                    read(buffer) // 버퍼에다가 청크 단위로 읽어옴.
                    val ciphertextChunk = cipher.update(buffer) // 부분적인 암호화로 최종 블록을 제외한 블록들 생성
                    opStream.write(ciphertextChunk)
                }
            }
            val remainingBytes = inputStream.readBytes()
            val lastChunk = cipher.doFinal(remainingBytes) // doFinal로 마지막 블록에 대한 암호화
            opStream.write(lastChunk)
        }
    }

    // 복호화 메소드
    // 암호화된 문자열이 들어있는 파일(혹은 암호화된 스트링)과 연결된 인풋스트림을 받아서 복호화하고 ByteArray로 반환한다.
    fun decrypt(inputStream: InputStream): ByteArray {
        return inputStream.use {
            val iv = ByteArray(KEY_SIZE)
            it.read(iv) // 아까 암호화할때 맨 앞에 적었던 초기화벡터 정보를 다시 앞부분에서 읽어옴.
            val cipher = getDecryptCipherForIv(iv)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(CHUNK_SIZE) // CHUNK_SIZE 크기를 갖는 바이트 어레이 객체 생성.
            while (inputStream.available() > CHUNK_SIZE) {
                inputStream.read(buffer)
                val ciphertextChunk = cipher.update(buffer) // 부분적인 복호화로 최종 블록을 제외한 블록들 생성
                outputStream.write(ciphertextChunk)
            }
            val remainingBytes = inputStream.readBytes()
            val lastChunk = cipher.doFinal(remainingBytes) // doFinal로 마지막 블록에 대한 복호화
            outputStream.write(lastChunk)
            outputStream.toByteArray()
        }
    }

    companion object {
        // 미국에서 개발된 블럭 단위 암호화 방식. 데이터를 블록 단위로 나눠 암호화
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

        // 블럭을 회전시키는 방식 중 하나임. 보안성이 높지만,
        // CBC방식을 위해서는 초기화 벡터(IV)의 사용이 필수적임.
        // IV란? CBC 블럭 회전 방식에 사용하는 초기화값. 키를 같은걸 사용해도 이게 바뀌면 매번 같은 입력에 대해 새롭게 암호화한다고 함.
        // 덕분에 외부에 유출되어도 상관없지만, 키의 길이와 일치하는 길이를 가진 IV여야 한다고 함.
        // IV는 암호화용 Cipher를 만들때마다 얻을 수 있음. 이 iv로 암호화한 내용은 반드시 동일한 IV로만 복호화할 수 있음.
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC

        // 블록 기반 암호화 알고리즘은 암호화할 대상이 블럭크기보다 작으면 암호화가 불가능함. 이 패딩을 적용해서 강제로 블럭길이만큼 맞출 수 있음.
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val KEYSTORE_TYPE = "AndroidKeyStore" // 이건 항상 고정임. 그래야 안드로이드 os의 키스토어에 접근 가능.
        private const val KEYSTORE_ALIAS = BuildConfig.KEY_STORE_ALIAS

        private const val KEY_PURPOSE =
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT

        private const val KEY_SIZE = 16 // bytes
        private const val CHUNK_SIZE = 1024 * 4 // bytes
    }
}
