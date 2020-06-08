package com.lunayoung.bungee.domain.product.registration

//상품 등록요청 클래스
data class ProductRegistrationRequest(
    val name: String,
    val description: String,
    val price: Int,
    val categoryId: Int,
    val imageIds: List<Long?>
)