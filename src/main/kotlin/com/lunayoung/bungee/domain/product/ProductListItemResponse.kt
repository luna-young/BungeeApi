package com.lunayoung.bungee.domain.product

data class ProductListItemResponse (
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val status: String,
    val sellerId: Long,
    val imagePaths: List<String>
)


    //확장함수를 이용해 Product 객체를 ProductItemResponse로 변환하는 함수
    fun Product.toProductListItemResponse() = id?.let{ //레파지토리로부터 데이터를 정상적으로 읽어왔다면 Product의 id가 null인 경우는 발생하지 않겠지만, 방어를 위해 id가 null인 경우 정상적이지 않은 데이터이므로 null 반환
        ProductListItemResponse (
            it,
            name,
            description,
            price,
            status.name,
            userId,
            images.map { it.toThumbs() } //toThumbs() -> ProductImage를 섬네일 주소로 변경하는 확장함수
        )
    }

    fun ProductImage.toThumbs() : String {
        val ext = path.takeLastWhile { it != '.' }
        val fileName = path.takeWhile { it != '.' }
        val thumbnailPath = "$fileName-thumb.$ext"

        return if (ext == "jpg") thumbnailPath else "$thumbnailPath.jpg"
    }