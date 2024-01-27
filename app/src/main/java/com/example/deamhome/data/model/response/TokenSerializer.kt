package com.example.deamhome.data.model.response

import androidx.datastore.core.Serializer
import com.example.deamhome.common.util.log
import com.example.deamhome.data.secure.CryptoManager
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class TokenSerializer(
    private val cryptoManager: CryptoManager,
) : Serializer<Token> {
    override val defaultValue: Token
        get() = Token.EMPTY_TOKEN

    override suspend fun readFrom(input: InputStream): Token {
        val decryptedBytes = cryptoManager.decrypt(input) // 복호화한 문자열 얻음
        return try {
            // 복호화한 문자열을 kotlin-serialization을 써서 데이터클래스로 파싱.
            log("mendel", "복호화 시도")
            Json.decodeFromString(
                deserializer = Token.serializer(),
                string = decryptedBytes.decodeToString(),
            )
        } catch (e: SerializationException) {
            log("mendel", "복호화 실패로, 기본값 줌")
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: Token, output: OutputStream) {
        cryptoManager.encrypt(
            // Token타입의 객체를 Json형식의 바이트어레이로 변환
            bytes = Json.encodeToString(
                serializer = Token.serializer(),
                value = t,
            ).encodeToByteArray(),
            outputStream = output,
        )
    }
}
