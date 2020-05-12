package com.lunayoung.bungee.domain

import java.util.*
import javax.persistence.*

//이 클래스가 데이터베이스 테이블에 매핑된 정보를 가지고 있음을 뜻함
@Entity(name = "user")
class User( //이메일, 패스워드, 이름에는 null 허용하지 않음
        var email: String,
        var password: String,
        var name: String
) {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null

    @PrePersist //디비에 새 데이터가 저장되기 전 자동으로 호출됨
    fun prePersist() {
        createdAt = Date()
        updatedAt = Date()
    }

    @PreUpdate //해당 애너테이션이 붙은 함수는 디비에 데이터 업데이트 명령을 날리기 전 실행됨
    fun preUpdate(){
        updatedAt = Date()
    }
}