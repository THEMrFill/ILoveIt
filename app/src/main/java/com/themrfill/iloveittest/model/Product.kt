package com.themrfill.iloveittest.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Product(
    val asin: String?,
    val climate_pledge_friendly: Boolean = false,
    val currency: String? = "GBP",
    val delivery: String? = "Free",
    val has_variations: Boolean = false,
    val is_amazon_choice: Boolean = false,
    val is_best_seller: Boolean = false,
    val is_prime: Boolean = false,
    val product_minimum_offer_price: String? = "£1.00",
    val product_num_offers: Int = 0,
    val product_num_ratings: Int = 0,
    val product_original_price: String? = "£2.00",
    val product_photo: String? = "",
    val product_price: String? = "£1.50",
    val product_star_rating: String? = "",
    val product_title: String? = "Product",
    val product_url: String? = "URL",
    val sales_volume: String? = "1 million",
) {
    fun toJson(): String = Json.encodeToString(this)
}
fun productFromJson(string: String): Product = Json.decodeFromString<Product>(string)
