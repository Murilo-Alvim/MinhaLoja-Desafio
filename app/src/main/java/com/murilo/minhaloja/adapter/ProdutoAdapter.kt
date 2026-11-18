package com.murilo.minhaloja.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.murilo.minhaloja.databinding.ItemProdutoBinding
import com.murilo.minhaloja.model.Produto
import com.murilo.minhaloja.util.Formatadores

class ProdutoAdapter(
    private val aoTocar: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    private var produtos: List<Produto> = emptyList()

    fun atualizar(novaLista: List<Produto>) {
        produtos = novaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val binding = ItemProdutoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProdutoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        holder.bind(produtos[position])
    }

    override fun getItemCount(): Int = produtos.size

    inner class ProdutoViewHolder(
        private val binding: ItemProdutoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(produto: Produto) {
            binding.tvTitulo.text = produto.title
            binding.tvCategoria.text = produto.category
            binding.tvPreco.text = Formatadores.preco(produto.price)
            binding.ivImagem.load(produto.image) { crossfade(true) }
            binding.root.setOnClickListener { aoTocar(produto) }
        }
    }
}
