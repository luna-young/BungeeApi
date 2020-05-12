package com.lunayoung.bungee.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/*
* 해당 인터페이스가 스프링이 관리하는 레파지토리 빈으로서 동작한다는 것을 의미
* cf. repository: 데이터 읽기/쓰기 등 담당하는 클래스
* */
@Repository
interface UserRepository : JpaRepository<User,Long>{
    /*JpaRepository를 상속받으면 레파지토리를 JPA 스펙에 맞게 확장하면서 기본적인 CRUD 함수 제공할 수 있게 됨
    * */

    fun findByEmail(email: String): User?
    //이메일 검색 시 0명 혹은 1명의 유저반환해야 하므로 ?
}