package com.lunayoung.bungee.domain.product

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

//상품정보를 읽어올 레파지토리 인터페이스
@Repository //<- 이거 없어서 오류났었음
interface ProductRepository :  JpaRepository<Product, Long> {
    //상품을 카테고리별로 보여줘야 하고
    //스크롤에 따라 상품의 id값을 기준으로 전/후를 읽어야하므로 다음과 같은 두개의 함수를 선언한다

    //상품리스트가 위로 스크롤될 때 호출되는 함수 --최신데이터를 로딩해야하므로 id값이 커야함
    @JsonManagedReference
    fun findByCategoryIdGreaterThanOrderByIdDesc (
        categoryId : Int?, id: Long, pageable: Pageable
    ): List<Product>

    //Pageable: 검색 조건을 기준으로 페이지당 몇 개의 값을 읽을 것인지 설정하고 몇 번째 페이지의 값들을 읽어올지 설정할 수 있는 객체
                //SQL에서의 'limit offset, rowCount'값
    //상품리스트가 아래로 스크롤될 때 호출되는 함수
    fun findByCategoryIdLessThanOrderByIdDesc (
        categoryId : Int?, id: Long, pageable: Pageable
    ): List<Product>

    fun findByCategoryIdAndIdLessThanOrderByIdDesc(
        categoryId: Int?, id: Long, pageable: Pageable
    ): List<Product>
}