package com.murilo.minhaloja.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatadores {

    private val localeBR = Locale("pt", "BR")

    fun preco(valor: Double): String = String.format(localeBR, "US$ %.2f", valor)

    fun data(data: Date?): String {
        if (data == null) return "agora"
        return SimpleDateFormat("dd/MM/yyyy HH:mm", localeBR).format(data)
    }
}
