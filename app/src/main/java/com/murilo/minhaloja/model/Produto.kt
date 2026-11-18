package com.murilo.minhaloja.model

// Formato de cada item devolvido por GET /products.
// "rating" é um objeto dentro do objeto, por isso vira outra data class.
data class Produto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating?
)

data class Rating(
    val rate: Double,
    val count: Int
)
