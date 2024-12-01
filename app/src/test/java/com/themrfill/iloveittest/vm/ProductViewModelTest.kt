package com.themrfill.iloveittest.vm

import org.junit.Assert.assertEquals
import org.junit.Test

class ProductViewModelTest {
    private val viewModel = ProductViewModel(null)

    @Test
    fun getPageFromUrlTest1() {
        val query = "query=Phone&page=1&country=GB&sort_by=RELEVANCE&product_condition=ALL&is_prime=false&deals_and_discounts=NONE"
        val page = viewModel.getPageFromUrl(query)
        assertEquals(1, page)
    }

    @Test
    fun getPageFromUrlTest2() {
        val query = "query=Phone&country=GB&page=2&sort_by=RELEVANCE&product_condition=ALL&is_prime=false&deals_and_discounts=NONE"
        val page = viewModel.getPageFromUrl(query)
        assertEquals(2, page)
    }

    @Test
    fun getPageFromUrlTest3() {
        val query = "query=Phone&country=GB&sort_by=RELEVANCE&product_condition=ALL&is_prime=false&deals_and_discounts=NONE&page=3"
        val page = viewModel.getPageFromUrl(query)
        assertEquals(3, page)
    }

    @Test
    fun getPageFromUrlTestMissing() {
        val query = "query=Phone&country=GB&sort_by=RELEVANCE&product_condition=ALL&is_prime=false&deals_and_discounts=NONE"
        val page = viewModel.getPageFromUrl(query)
        assertEquals(1, page)
    }

    @Test
    fun getPageFromUrlTestEmpty() {
        val query = "query=Phone&country=GB&sort_by=RELEVANCE&page=&product_condition=ALL&is_prime=false&deals_and_discounts=NONE"
        val page = viewModel.getPageFromUrl(query)
        assertEquals(1, page)
    }

    @Test
    fun getPageListTest1() {
        val totalProducts = 200
        val top = 12 // 200 / 18 = 11.1111, round this up to 12

        viewModel.productPage.intValue = 1
        viewModel.productCount.intValue = totalProducts
        val pages = viewModel.getPageList()

        assertEquals(null, pages[0])
        assertEquals(null, pages[1])
        assertEquals(2, pages[2])
        assertEquals(top, pages[3])
    }

    @Test
    fun getPageListTest2() {
        val totalProducts = 200

        viewModel.productPage.intValue = 2
        viewModel.productCount.intValue = totalProducts
        val pages = viewModel.getPageList()
        val top = 12 // 200 / 18 = 11.1111, round this up to 12

        assertEquals(1, pages[0])
        assertEquals(1, pages[1])
        assertEquals(3, pages[2])
        assertEquals(top, pages[3])
    }

    @Test
    fun getPageListTest3() {
        val totalProducts = 200
        val top = 12 // 200 / 18 = 11.1111, round this up to 12

        viewModel.productPage.intValue = 10
        viewModel.productCount.intValue = totalProducts
        val pages = viewModel.getPageList()

        assertEquals(1, pages[0])
        assertEquals(9, pages[1])
        assertEquals(11, pages[2])
        assertEquals(top, pages[3])
    }

    @Test
    fun getPageListTest4() {
        val totalProducts = 200
        val top = 12 // 200 / 18 = 11.1111, round this up to 12

        viewModel.productPage.intValue = 11
        viewModel.productCount.intValue = totalProducts
        val pages = viewModel.getPageList()

        assertEquals(1, pages[0])
        assertEquals(10, pages[1])
        assertEquals(top, pages[2])
        assertEquals(top, pages[3])
    }

    @Test
    fun getPageListTest5() {
        val totalProducts = 200
        val top = 12 // 200 / 18 = 11.1111, round this up to 12

        viewModel.productPage.intValue = top
        viewModel.productCount.intValue = totalProducts
        val pages = viewModel.getPageList()

        assertEquals(1, pages[0])
        assertEquals(11, pages[1])
        assertEquals(null, pages[2])
        assertEquals(null, pages[3])
    }
}