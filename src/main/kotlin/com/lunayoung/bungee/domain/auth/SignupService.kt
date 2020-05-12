package com.lunayoung.bungee.domain.auth

import com.lunayoung.bungee.common.BungeeException
import com.lunayoung.bungee.domain.User
import com.lunayoung.bungee.domain.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service //스프링이 관리하는 빈 클래스-그중 비즈니스 로직을 담당
//autowired: 빈 클래스 자동으로 주입받겠다는 뜻
class SignupService @Autowired constructor(
        private val userRepository: UserRepository //사용자의 데이터를 읽어와야 하므로 데이터 읽기/쓰기 담당하는 클래스를 주입받음
){

    fun signup(signupRequest: SignupRequest) {
        validateRequest(signupRequest)
        checkDuplicates(signupRequest.email)
        registerUser(signupRequest)
    }

    private fun validateRequest(signupRequest: SignupRequest) {
        validateEmail(signupRequest.email)
        validateName(signupRequest.name)
        validatePassword(signupRequest.password)
    }

    private fun validateEmail(email: String){
        val isNotValidEmail = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
                .toRegex(RegexOption.IGNORE_CASE)
                .matches(email)
                .not()
        if(isNotValidEmail)
            throw BungeeException("이메일 형식이 올바르지 않습니다.")
    }

    private fun validateName(name: String) {
        if(name.trim().length !in 2..20){
            throw BungeeException("이름은 2자 이상 20자 이하여야 합니다.")
        }
    }

    private fun validatePassword(password: String) {
        if(password.trim().length !in 8..20){
            throw BungeeException("비밀번호는 공백을 제외하고 8자 이상 20자 이하여야 합니다.")
        }
    }

    private fun checkDuplicates(email: String) {
        userRepository.findByEmail(email)?.let {
            throw BungeeException("이미 사용 중인 이메일입니다.")
        }
    }

    private fun registerUser(signupRequest: SignupRequest) =
            with(signupRequest) {
                val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
                val user = User(email, hashedPassword, name)
                userRepository.save(user)
            }
}