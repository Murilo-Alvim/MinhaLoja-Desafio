package com.murilo.minhaloja.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.murilo.minhaloja.model.Favorito
import com.murilo.minhaloja.model.Produto

/**
 * Todas as operações de favoritos no Firestore.
 * Estrutura: usuarios/{uid}/favoritos/{idProduto}
 */
object FavoritosRepository {

    private fun colecaoDoUsuario(): CollectionReference {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("Nenhum usuário logado")
        return FirebaseFirestore.getInstance()
            .collection("usuarios").document(uid)
            .collection("favoritos")
    }

    fun salvar(produto: Produto, onSucesso: () -> Unit, onErro: (Exception) -> Unit) {
        val dados = hashMapOf(
            "id" to produto.id,
            "titulo" to produto.title,
            "preco" to produto.price,
            "categoria" to produto.category,
            "salvoEm" to FieldValue.serverTimestamp()
        )

        // id do produto como nome do documento: salvar de novo só sobrescreve
        colecaoDoUsuario()
            .document(produto.id.toString())
            .set(dados)
            .addOnSuccessListener { onSucesso() }
            .addOnFailureListener { erro -> onErro(erro) }
    }

    fun listar(onSucesso: (List<Favorito>) -> Unit, onErro: (Exception) -> Unit) {
        colecaoDoUsuario()
            .orderBy("salvoEm", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { resultado ->
                val favoritos = resultado.documents.map { doc ->
                    Favorito(
                        id = doc.getLong("id") ?: 0L,
                        titulo = doc.getString("titulo") ?: "",
                        preco = doc.getDouble("preco") ?: 0.0,
                        categoria = doc.getString("categoria") ?: "",
                        salvoEm = doc.getTimestamp("salvoEm")?.toDate()
                    )
                }
                onSucesso(favoritos)
            }
            .addOnFailureListener { erro -> onErro(erro) }
    }

    fun remover(idProduto: Long, onSucesso: () -> Unit, onErro: (Exception) -> Unit) {
        colecaoDoUsuario()
            .document(idProduto.toString())
            .delete()
            .addOnSuccessListener { onSucesso() }
            .addOnFailureListener { erro -> onErro(erro) }
    }
}
