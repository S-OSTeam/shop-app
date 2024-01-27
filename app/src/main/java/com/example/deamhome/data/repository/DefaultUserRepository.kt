package com.example.deamhome.data.repository

import com.example.deamhome.domain.model.ApiResponse
import com.example.deamhome.domain.model.UserProfile
import com.example.deamhome.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// 나중에 유저이름, 유저프로필, 앱 처음 방문 여부, 알림설정 정보들 같은거 저장하고, 불러올 예정임.
class DefaultUserRepository(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override suspend fun fetchUserProfile(): ApiResponse<UserProfile> {
        return ApiResponse.Success(
            UserProfile(
                "고명진",
                "https://png.pngtree.com/png-clipart/20190115/ourmid/pngtree-company-business-business-company-friendly-man-greeting-man-png-image_336981.jpg",
            ),
        )
    }
}
