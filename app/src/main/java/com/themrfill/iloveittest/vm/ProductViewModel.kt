package com.themrfill.iloveittest.vm

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.themrfill.iloveittest.api.ProductService
import com.themrfill.iloveittest.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.ceil

class ProductViewModel(private val productService: ProductService?): ViewModel() {
    private val TAG = "ProductViewModel"

    val showSpinner = MutableTransitionState(false)
    val showError = MutableTransitionState(false)
    val searchString = mutableStateOf("")
    val productList = mutableStateListOf<Product>()
    val productCount = mutableIntStateOf(0)
    val productPage = mutableIntStateOf(0)

    fun findProducts(product: String, page: Int = 1) {
        setShowError(false)
        setShowSpinner(true)
        productList.clear()
        productCount.intValue = 0
        viewModelScope.launch(Dispatchers.IO) {
            val response = productService?.getProducts(product, page)
            setShowSpinner(false)
            if (response?.isSuccessful == true) {
                val body = response.body()
                if (body?.status == "OK") {
                    productCount.intValue = body.data.total_products
                    productPage.intValue = getPageFromUrl(response.raw().request.url.query)
                    productList.clear()
                    productList.addAll(body.data.products)
                } else {
                    setShowError(true)
                }
            } else {
                setShowError(true)
            }
        }
    }

    private fun setShowError(state: Boolean) {
        showError.targetState = state
    }

    private fun setShowSpinner(state: Boolean) {
        showSpinner.targetState = state
    }

    internal fun getPageFromUrl(query: String?): Int {
        return query?.split('&')?.map {
            val parts = it.split('=')
            val name = parts.firstOrNull() ?: ""
            val value = parts.drop(1).firstOrNull()?.ifEmpty { "1" }
            Pair(name, value)
        }?.firstOrNull{it.first == "page"}?.second?.toInt() ?: 1
    }

    internal fun getPageList(): MutableList<Int?> {
        val pages = ceil(productCount.intValue.toDouble() / PRODUCTS_PER_PAGE).toInt()
        val page = productPage.intValue
        val pageList = ArrayList<Int?>()
        pageList.add(if (page == 1) null else 1)
        pageList.add(if (page == 1) null else page - 1)
        pageList.add(if (page == pages) null else page + 1)
        pageList.add(if (page == pages) null else pages)
        return pageList
    }

    fun searchProduct(keyboard: SoftwareKeyboardController?) {
        val search = searchString.value.trim()
        if (search.isNotEmpty()) {
            keyboard?.hide()
            findProducts(search)
        }
    }

    companion object {
        const val PRODUCTS_PER_PAGE = 18
    }
}