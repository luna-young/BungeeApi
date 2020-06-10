package com.lunayoung.bungee.controller

import com.lunayoung.bungee.common.ApiResponse
import com.lunayoung.bungee.domain.product.Product
import com.lunayoung.bungee.domain.product.ProductService
import com.lunayoung.bungee.domain.product.registration.ProductImageService
import com.lunayoung.bungee.domain.product.registration.ProductRegistrationRequest
import com.lunayoung.bungee.domain.product.registration.ProductRegistrationService
import com.lunayoung.bungee.domain.product.toProductListItemResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1")
class ProductApiController @Autowired constructor(
    private val productImageService: ProductImageService,
    private val productRegistration: ProductRegistrationService,
    private val productService: ProductService
) {

    @GetMapping("/products")
    fun search (
        @RequestParam productId: Long,
        @RequestParam(required = false) categoryId: Int?,
        @RequestParam direction: String,
        @RequestParam(required = false) limit: Int?
    ) = productService
        .search(categoryId, productId, direction, limit ?: 10)
        .mapNotNull { Product::toProductListItemResponse } //toProductItemResponse()의 값이 null일 경우 mapNotNull() 함수가 리스트에서 필터링함
        .let {ApiResponse.ok(it)}

    @PostMapping("/product_images")
    fun uploadImage(image:MultipartFile) = ApiResponse.ok(
        productImageService.uploadImage(image)
    )

    @PostMapping("/products")
    fun register(
        @RequestBody request: ProductRegistrationRequest
    ) = ApiResponse.ok(
        productRegistration.register(request)
    )

}