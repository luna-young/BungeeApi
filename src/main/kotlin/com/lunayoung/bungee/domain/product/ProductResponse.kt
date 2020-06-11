package com.lunayoung.bungee.domain.product

import com.lunayoung.bungee.common.BungeeException


//상품 상세정보로 사용될 클래스
//레파지토리에서 상품 정보를 읽은 후 ProductResponse로 변환해 상품 정보를 제공한다
data class ProductResopnse (
        val id: Long,
        val name: String,
        val description: String,
        val price: Int,
        val status: String,
        val sellerId: Long,
        val imagePaths: List<String>
)

fun Product.toProductResponse() = id?.let{ //product의 id는 null이 될 수 있기 때문에 null이 아닌 경우에만 정상적인 응답 반환
        ProductResopnse (
                it,
                name,
                description,
                price,
                status.name,
                userId,
                images.map { it.path }
        )
} ?: throw BungeeException("상품 정보를 찾을 수 없습니다 @ProductResopnse")
//product id가 null인 경우에 예외를 던진다
