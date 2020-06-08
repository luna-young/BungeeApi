package com.lunayoung.bungee.domain.product

import com.lunayoung.bungee.domain.jpa.BaseEntity
import sun.security.x509.AccessDescription
import javax.persistence.*

@Entity(name = "product")
class Product(
        @Column(length = 40)
        var name: String,
        @Column(length = 500)
        var description: String,
        var price: Int,
        var categoryId: Int,
        @Enumerated(EnumType.STRING) //코드상 문자 그대로 저장되도록 변경; 디폴트-정수형태로 저장됨.
        var status: ProductStatus,
        @OneToMany //product 하나에 ProductImage 여러개가 매핑될 수 있음
        @JoinColumn(name = "productId")
        var images: MutableList<ProductImage>,
        var userId: Long
) : BaseEntity() {

}