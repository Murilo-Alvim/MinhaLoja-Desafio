package com.murilo.minhaloja.model

import java.util.Date

// Documento salvo em usuarios/{uid}/favoritos/{idProduto}
data class Favorito(
    val id: Long,
    val titulo: String,
    val preco: Double,
    val categoria: String,
    val salvoEm: Date?
)
