package com.themrfill.iloveittest.api

import com.themrfill.iloveittest.model.ProductSearch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("search")
    suspend fun getProducts(
        @Query("query") searchQuery: String,
        @Query("page") page: Int = 1,
        @Query("country") country: String = "GB",
        @Query("sort_by") sortBy: String = "RELEVANCE",
        @Query("product_condition") condition: String = "ALL",
        @Query("is_prime") isPrime: Boolean = false,
        @Query("deals_and_discounts") deals: String = "NONE",
        ): Response<ProductSearch>
}