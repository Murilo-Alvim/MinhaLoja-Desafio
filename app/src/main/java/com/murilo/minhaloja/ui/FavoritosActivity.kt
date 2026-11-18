package com.murilo.minhaloja.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.murilo.minhaloja.adapter.FavoritoAdapter
import com.murilo.minhaloja.data.FavoritosRepository
import com.murilo.minhaloja.databinding.ActivityFavoritosBinding
import com.murilo.minhaloja.model.Favorito

class FavoritosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritosBinding
    private val adapter = FavoritoAdapter { favorito -> removerFavorito(favorito) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Meus favoritos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFavoritos.layoutManager = LinearLayoutManager(this)
        binding.rvFavoritos.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        carregarFavoritos()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun carregarFavoritos() {
        binding.progressFavoritos.visibility = View.VISIBLE
        binding.tvMensagem.visibility = View.GONE

        // O listener do Firebase já roda na thread principal
        FavoritosRepository.listar(
            onSucesso = { favoritos ->
                binding.progressFavoritos.visibility = View.GONE
                adapter.atualizar(favoritos)
                if (favoritos.isEmpty()) {
                    binding.tvMensagem.text = "Você ainda não tem favoritos.\nToque em um produto para salvar."
                    binding.tvMensagem.visibility = View.VISIBLE
                }
            },
            onErro = { erro ->
                binding.progressFavoritos.visibility = View.GONE
                binding.tvMensagem.text = "Erro ao carregar favoritos:\n${erro.localizedMessage}"
                binding.tvMensagem.visibility = View.VISIBLE
            }
        )
    }

    private fun removerFavorito(favorito: Favorito) {
        FavoritosRepository.remover(
            favorito.id,
            onSucesso = {
                Toast.makeText(this, "Removido: ${favorito.titulo}", Toast.LENGTH_SHORT).show()
                carregarFavoritos()
            },
            onErro = { erro ->
                Toast.makeText(this, "Erro ao remover: ${erro.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        )
    }
}
