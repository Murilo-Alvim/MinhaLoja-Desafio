package com.murilo.minhaloja.network

import com.murilo.minhaloja.model.Produto
import retrofit2.http.GET

interface ProdutoService {

    // A resposta começa com [ ], então o retorno é uma lista
    @GET("products")
    suspend fun listarProdutos(): List<Produto>
}
