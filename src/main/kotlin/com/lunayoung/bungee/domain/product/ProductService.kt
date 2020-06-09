package com.lunayoung.bungee.domain.product

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class ProductService @Autowired constructor(
    private val productRepository: ProductRepository
) {

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

        return when(condition) { //코틀린의 when절에서는 객체의 비교도 허용함! (코틀린 when절의 강력한 기능 중 하나)
            NEXT_IN_CATEGORY -> productRepository
                .findByCategoryIdLessThanOrderByIdDesc(
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