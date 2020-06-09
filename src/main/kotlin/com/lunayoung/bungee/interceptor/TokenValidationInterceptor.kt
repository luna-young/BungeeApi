package com.lunayoung.bungee.interceptor

import com.lunayoung.bungee.domain.auth.JWTUtil
import com.lunayoung.bungee.domain.auth.UserContextHolder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component //이 클래스가 스프링이 관리하는 빈 클래스임을 나타냄. Service와 기술적으로는 동일하지만, 의미상으로는 비즈니스 로직을 처리하는 클래스가 아니라는 점에서 다름
class TokenValidationInterceptor @Autowired constructor(
        private val userContextHolder: UserContextHolder
) : HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun preHandle(request: HttpServletRequest,
                           response: HttpServletResponse,
                           handler: Any
    ): Boolean {
        val authHeader = request.getHeader(AUTHORIZATION) //HttpServletRequest에 포함된 Auth헤더 반환
        print("----authHeader: $authHeader")

        if(authHeader.isNullOrBlank()) {
            val pair = request.method to request.servletPath
            if(!DEFAULT_ALLOWED_API_URLS.contains(pair)){
                response.sendError(401) //허용된 url이 아니라면 클라이언트에 권한이 없다는 것을 알림
                return false
            }
            return true
        } else {
            val grantType = request.getParameter(GRANT_TYPE)
            print("---grantType: $grantType")
            val token = extractToken(authHeader)
            print("---token: $token")

            return handleToken(grantType, token, response)
        }
    }

    private fun handleToken(
            grantType: String,
            token: String,
            response: HttpServletResponse
    ) = try {
        val jwt = when(grantType) {
            GRANT_TYPE_REFRESH -> JWTUtil.verifyRefresh(token)
            else -> JWTUtil.verify(token)
        }
        val email = JWTUtil.extractEmail(jwt)
        userContextHolder.set(email)
        true
    } catch (e: Exception) {
        logger.error("토큰 검증 실패. token = $token", e)
        response.sendError(401)
        false
    }

    private fun extractToken(token: String) = token.replace(BEARER, "").trim()

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        userContextHolder.clear()
    }

    companion object {
        private const val AUTHORIZATION = "Authorization" //Authorization 토큰이 포함된 헤더값을 가져오기 위한 상수
        private const val BEARER = "Bearer" //Auth 헤더에 jwt 토큰을 전달할 때 사용되느 인증방법을 나타내는 스키마. 실제 토큰을 사용하려면 헤더 값에서 이 문자열을 제거한 후 사용해야 함
        private const val GRANT_TYPE = "grant_type" //토큰 재발행을 요청할 때 사용될 파라미터의 키와 값
        const val GRANT_TYPE_REFRESH = "refresh_token" //상동

        //토큰 인증없이 사용할 수 있는 url을 정의하기 위해 선언한 리스트
        private val DEFAULT_ALLOWED_API_URLS = listOf(
                "POST" to "/api/v1/signin",
                "POST" to "/api/v1/users"
            )
        }
}
