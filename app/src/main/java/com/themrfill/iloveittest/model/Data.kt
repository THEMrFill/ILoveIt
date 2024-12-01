package com.themrfill.iloveittest.model

data class Data(
    val country: String,
    val domain: String,
    val products: List<Product>,
    val total_products: Int
)