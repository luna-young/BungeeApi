package com.lunayoung.bungee.domain.product

enum class ProductStatus(private val status: String) {
    SELLABLE("판매 중"),
    SOLD_OUT("품절")
}