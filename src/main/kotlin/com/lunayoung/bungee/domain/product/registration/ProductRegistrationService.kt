package com.lunayoung.bungee.domain.product.registration

import com.lunayoung.bungee.common.BungeeException
import com.lunayoung.bungee.domain.auth.UserContextHolder
import com.lunayoung.bungee.domain.product.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductRegistrationService @Autowired constructor(
    private val productRepository: ProductRepository,
    private val productImageRepository: ProductImageRepository,
    private val userContextHolder: UserContextHolder
){

    //사용자의 아이디가 있는지 검사한 후 존재하면 상품 등록 로직을 수행,
    //그렇지 않다면 예외를 던짐
    fun register(request: ProductRegistrationRequest) =
        userContextHolder.id?.let { userId ->
            val images by lazy { findAndValidateImages(request.imageIds) }
            request.validateRequest()
            request.toProduct(images, userId)
                .run(::save)
        } ?: throw BungeeException(
            "상품 등록에 필요한 사용자 정보가 존재하지 않습니다."
        )

    private fun findAndValidateImages(imageIds: List<Long?>) =
        productImageRepository.findByIdIn(imageIds.filterNotNull())
            .also { images ->
                images.forEach { image ->
                    if(image.productId != null)
                        throw BungeeException("이미 등록된 상품입니다.")
                }
            }

    private fun save(product: Product) = productRepository.save(product)

    //요청객체의 유효성을 검증하는 validateRequest()와 이를 엔티티로 변환하는 toProduct()에 확장함수 이용
    //코틀린에서는 전역적으로 사용될 만한 확장함수는 해당 클래스가 선언된 파일에 확장함수를 함께 정의하고,
    //특별한 클래스에서만 사용되는 확장함수는 이 함수를 사용할 클래스 정의 아래에 정의하는 것을 권장함
    private fun ProductRegistrationRequest.validateRequest() = when {
        name.length !in 1..40 ||
        imageIds.size !in 1..4 ||
        imageIds.filterNotNull().isEmpty() ||
        description.length !in 1..500 ||
        price <= 0 ->
            throw BungeeException("올바르지 않은 상품 정보입니다.")
        else -> {

        }
    }

    private fun ProductRegistrationRequest.toProduct(
        images: MutableList<ProductImage>,
        userId: Long
    ) = Product (
        name,
        description,
        price,
        categoryId,
        ProductStatus.SELLABLE,
        images,
        userId
    )
}