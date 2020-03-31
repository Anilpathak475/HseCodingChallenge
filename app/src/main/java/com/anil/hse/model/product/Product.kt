package com.anil.hse.model.product


data class Product(
    val additionalInformation: String?,
    val averageStars: Double,
    val brandId: String,
    val brandNameLong: String,
    val brandNameShort: String?,
    val categoryCode: String?,
    val deliveryDate: Any?,
    val deliveryText: String?,
    val deliveryTimeDays: Any?,
    val deliveryType: Any?,
    val energyFactor: Any?,
    val fromPrice: Boolean?,
    val hazardWarning: Any?,
    val imageUris: List<String>,
    val ingredients: Any?,
    val legalText: String?,
    val longDescription: String?,
    val nameShort: String,
    val notAllowedInCountry: Boolean,
    val picCount: Int?,
    val productPrice: ProductPrice,
    val reviewers: Int?,
    val reviewsForbidden: Boolean?,
    val sku: String,
    val status: String,
    val stockAmount: Int?,
    val stockColor: String?,
    val title: String?,
    val usps: List<String>?,
    val variations: List<Variation>

)

data class Variation(
    val imageUris: List<String>,
    val picCount: Int,
    val productPrice: ProductPrice,
    val sku: String,
    val status: String,
    val stockAmount: Int,
    val stockColor: String,
    val variationType: String
)

data class ProductPrice(
    val basePrice: Any,
    val basePriceInfo: Any,
    val basePriceRefAmount: Any,
    val basePriceUnit: Any,
    val country: Any,
    val currency: String,
    val percentDiscount: String,
    val price: Double,
    val priceDiscount: Float,
    val priceLabel: String,
    val priceValidTo: String,
    val referencePrice: Double,
    val referencePriceLabel: String,
    val shippingCosts: Double
)