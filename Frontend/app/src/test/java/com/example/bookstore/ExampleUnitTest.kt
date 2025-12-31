package com.example.bookstore

import org.junit.Test
import org.junit.Assert.*
import com.example.bookstore.repository.ProductRepository
import kotlinx.coroutines.test.runTest

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun callAPI() = runTest {
//        val repository = ProductRepository()
//        val productId = "M1MjEAAAQBAJ";
//        val  result = repository.getProductDetail(productId)
//
//        // Kiểm tra xem kết quả có thành công không
//        assert(result.isSuccess) { "API call failed: ${result.exceptionOrNull()?.message}" }
//
//        // Kiểm tra xem dữ liệu có tồn tại không
//        val response = result.getOrNull()
//        val productDetail = response?.data
//        assert(productDetail != null) { "API call successful but data is null" }
//
//        // In kết quả ra console trong quá trình test
//        println("=== TEST SUCCESS ===")
//        println("Total items: ${productDetail?.size}")
//        productDetail?.take(3)?.forEachIndexed { index, item ->
//            println("${index + 1}. Item Detail: $item")
//        }
    }
}