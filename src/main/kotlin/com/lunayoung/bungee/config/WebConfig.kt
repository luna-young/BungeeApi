package com.lunayoung.bungee.config

import com.lunayoung.bungee.interceptor.TokenValidationInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration //이 클래스가 스프링에서 사용하는 설정을 담은 빈 클래스라는 것을 나타냄.
// 해당 애너테이션 사용 시 스프링이 SpringBootApplication 이하의 패키지에서 모든 설정 클래스들을 검사해 자동으로 빈을 생성해줌
class WebConfig @Autowired constructor(
        private val tokenValidationInterceptor: TokenValidationInterceptor
) : WebMvcConfigurer //스프링 MVC 프로젝트에서 네이티브 코드 베이스로 설정을 임력할 수 있게 해주는 여러 가지 콜백함수들이 정의된 인터페이스
{
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(tokenValidationInterceptor)
                .addPathPatterns("/api/**") // /api/ 이하의 uri에서 인터셉터가 동작하도록 설정
    }

    @Value("\${bungee.file-upload.default-dir}") //application.yml에 기입한 파일 업로드 디렉토리 설정을 읽은후 애너테이션이 붙은 변수에 대입
    var uploadPath: String? = ""

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
            //registry.addResourceHandler("/img/**", "/images/**", "/test/**", "/images/**", "/images/**", "images/**")
            //    .addResourceLocations("classpath:/static/img/", "file:/bungee/images/", "file:///C:/Users/LUNA/Pictures/Saved Pictures/", "file:///bungee/images/", "file:bungee/images/",
             //       "file:$uploadPath/images/"
             //   )


        registry.addResourceHandler("/images/**")
            .addResourceLocations("file:/bungee/images/")

        registry.addResourceHandler("/images/**")
            .addResourceLocations("file:$uploadPath/images/")

}    }
