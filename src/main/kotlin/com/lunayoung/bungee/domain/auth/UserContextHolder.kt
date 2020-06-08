package com.lunayoung.bungee.domain.auth

import com.lunayoung.bungee.domain.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


/*
* UserRepository로부터 사용자 정보를 읽어 ThreadLocal<UserHolder> 타입의 프로퍼티에 사용자 정보를 저장하는 set함수와
* 이를 초기화해주는 clear 함수를 가지고 있는 클래스
* */
@Service
class UserContextHolder @Autowired constructor(
        private val userRepository: UserRepository
) {
    private val userHolder = ThreadLocal.withInitial {
        UserHolder()
    }

    val id: Long? get() = userHolder.get().id
    val name: String? get() = userHolder.get().name
    val email: String? get() = userHolder.get().email

    fun set(email: String) = userRepository
            .findByEmail(email)?.let { user ->
                this.userHolder.get().apply {
                    this.id = user.id
                    this.name = user.name
                    this.email = user.email
                }.run(userHolder::set)
            }

    fun clear() {
        userHolder.remove()
    }

    class UserHolder {
        var id: Long? = null
        var email: String? = null
        var name: String? = null
    }

}
