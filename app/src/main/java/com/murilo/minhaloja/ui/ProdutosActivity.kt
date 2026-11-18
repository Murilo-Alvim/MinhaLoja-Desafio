package com.murilo.minhaloja.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.murilo.minhaloja.adapter.ProdutoAdapter
import com.murilo.minhaloja.data.FavoritosRepository
import com.murilo.minhaloja.databinding.ActivityProdutosBinding
import com.murilo.minhaloja.model.Produto
import com.murilo.minhaloja.network.RetrofitClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class ProdutosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProdutosBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val adapter = ProdutoAdapter { produto -> salvarFavorito(produto) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Produtos"
        supportActionBar?.subtitle = auth.currentUser?.email

        binding.rvProdutos.layoutManager = LinearLayoutManager(this)
        binding.rvProdutos.adapter = adapter

        binding.btnTentarNovamente.setOnClickListener { carregarProdutos() }
        binding.btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }
        binding.btnSair.setOnClickListener { sair() }

        carregarProdutos()
    }

    private fun carregarProdutos() {
        mostrarEstado(Estado.CARREGANDO)

        // Chamada do Retrofit dentro de lifecycleScope.launch (como no ViaCEP)
        lifecycleScope.launch {
            try {
                val produtos = RetrofitClient.produtoService.listarProdutos()
                adapter.atualizar(produtos)
                mostrarEstado(if (produtos.isEmpty()) Estado.ERRO else Estado.LISTA)
                if (produtos.isEmpty()) binding.tvErro.text = "Nenhum produto encontrado."
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                binding.tvErro.text = "Não foi possível carregar os produtos.\nVerifique sua internet e tente novamente."
                mostrarEstado(Estado.ERRO)
            }
        }
    }

    private fun salvarFavorito(produto: Produto) {
        FavoritosRepository.salvar(
            produto,
            onSucesso = {
                Toast.makeText(this, "Salvo nos favoritos: ${produto.title}", Toast.LENGTH_SHORT).show()
            },
            onErro = { erro ->
                Toast.makeText(this, "Erro ao salvar: ${erro.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun sair() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private enum class Estado { CARREGANDO, LISTA, ERRO }

    private fun mostrarEstado(estado: Estado) {
        binding.layoutCarregando.visibility = if (estado == Estado.CARREGANDO) View.VISIBLE else View.GONE
        binding.rvProdutos.visibility = if (estado == Estado.LISTA) View.VISIBLE else View.GONE
        binding.layoutErro.visibility = if (estado == Estado.ERRO) View.VISIBLE else View.GONE
    }
}
