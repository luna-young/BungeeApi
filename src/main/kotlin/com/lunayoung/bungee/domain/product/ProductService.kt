package com.lunayoung.bungee.domain.product

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class ProductService @Autowired constructor(
    private val productRepository: ProductRepository
) {
    

    //상품하나를 조회한다 -> 레파지토리에서 id로 읽어온다
    //아이디에 해당하는 상품이 존재하지 않을 수도 있으므로 findByIdOrNull()을 통해
    //null 혹은 존재하는 엔티티를 반환하도록 만듦
    fun get(id: Long) = productRepository.findByIdOrNull(id)

    fun search (
        categoryId: Int?,
        productId: Long,
        direction: String,
        limit: Int
    ): List<Product> {
        val pageable = PageRequest.of(0, limit) //각 조건에 맞는 0 페이지를 limit수만큼 가져오기 위한 Pageable을 상속받은 객체
        val condition = ProductSearchCondition ( //상품의 검색 조건을 표현하기 위한 객체
            categoryId != null,
            direction
        )
        print ("\n ###condition: $condition")
        print ("\n ###categoryId: $categoryId")
        print ("\n ###productId: $productId")
        print ("\n ###pageable: $pageable")

        return when(condition) { //코틀린의 when절에서는 객체의 비교도 허용함! (코틀린 when절의 강력한 기능 중 하나)

            NEXT_IN_CATEGORY -> productRepository
                .findByCategoryIdAndIdLessThanOrderByIdDesc(
                    categoryId, productId, pageable
                )
            PREV_IN_CATEGORY -> productRepository
                .findByCategoryIdGreaterThanOrderByIdDesc(
                    categoryId, productId, pageable
                )
            else -> throw IllegalArgumentException("상품 검색 조건 오류")
        }
    }


    data class ProductSearchCondition (
        val categoryIsNotNull: Boolean,
        val direction: String
    )

    companion object {
        val NEXT_IN_CATEGORY = ProductSearchCondition(true, "next")
        val PREV_IN_CATEGORY = ProductSearchCondition(true, "prev")
    }
}