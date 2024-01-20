package com.example.deamhome.app

import android.app.Application
import android.content.Context
import androidx.datastore.dataStore
import com.apollographql.apollo3.ApolloClient
import com.example.deamhome.BuildConfig
import com.example.deamhome.data.apollo.AuthApolloService
import com.example.deamhome.data.apollo.ProductApolloService
import com.example.deamhome.data.apollo.interceptor.AuthorizationInterceptor
import com.example.deamhome.data.apollo.interceptor.LoggingApolloInterceptor
import com.example.deamhome.data.datasource.local.LocalAuthDataSource
import com.example.deamhome.data.datasource.network.NetworkAuthDataSource
import com.example.deamhome.data.datasource.network.NetworkProductDataSource
import com.example.deamhome.data.model.response.TokenSerializer
import com.example.deamhome.data.repository.DefaultAuthRepository
import com.example.deamhome.data.repository.DefaultProductRepository
import com.example.deamhome.data.secure.CryptoManager
import com.example.deamhome.domain.repository.AuthRepository

// 객체 생성을 애플리케이션 객체 초기화시 이렇게 모두 해버리면 앱 시작이 늦어질 수 있음. 싱글톤 패턴으로 분리예정
class DIContainer(
    application: Application,
) {
    private val Context.dataStore by dataStore(
        fileName = LocalAuthDataSource.AUTH_TOKEN_STORE_NAME,
        serializer = TokenSerializer(CryptoManager()),
    )
    private val localAuthDataSource: LocalAuthDataSource =
        LocalAuthDataSource(application.dataStore)

    // 로깅용 인터셉터로 공용임.
    private val loggingInterceptor = LoggingApolloInterceptor()

    // 토큰 삽입을 하지 않고 요청을 보내는 아폴로 클라이언트
    private val authApolloClient: ApolloClient =
        ApolloClient.Builder().serverUrl(BuildConfig.SERVER_URL)
            .addInterceptor(loggingInterceptor)
            .build()
    private val networkAuthDataSource = NetworkAuthDataSource(
        AuthApolloService(authApolloClient),
    )

    val authRepository: AuthRepository =
        DefaultAuthRepository(localAuthDataSource, networkAuthDataSource)

    val isLogin = authRepository.isLogin

    // 토큰 자동 삽입 및 갱신 기능이 있는 아폴로 클라이언트 만드는 과정
    private val authInterceptor = AuthorizationInterceptor(authRepository)
    private val productApolloClient: ApolloClient =
        ApolloClient.Builder().serverUrl(BuildConfig.SERVER_URL)
            .addInterceptor(loggingInterceptor)
            .addHttpInterceptor(authInterceptor)
            .build()
    private val networkProductDataSource =
        NetworkProductDataSource(ProductApolloService(productApolloClient))

    val productRepository = DefaultProductRepository(networkProductDataSource)
}
