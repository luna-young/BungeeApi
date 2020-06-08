package com.lunayoung.bungee.domain.product

import com.lunayoung.bungee.domain.jpa.BaseEntity
import java.util.*
import javax.persistence.*

@Entity(name = "product_image")
class ProductImage(
        var path: String,
        var productId: Long? =null
): BaseEntity() {


}