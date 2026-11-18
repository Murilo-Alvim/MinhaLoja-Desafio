package com.murilo.minhaloja.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.murilo.minhaloja.databinding.ItemFavoritoBinding
import com.murilo.minhaloja.model.Favorito
import com.murilo.minhaloja.util.Formatadores

class FavoritoAdapter(
    private val aoRemover: (Favorito) -> Unit
) : RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder>() {

    private var favoritos: List<Favorito> = emptyList()

    fun atualizar(novaLista: List<Favorito>) {
        favoritos = novaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val binding = ItemFavoritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        holder.bind(favoritos[position])
    }

    override fun getItemCount(): Int = favoritos.size

    inner class FavoritoViewHolder(
        private val binding: ItemFavoritoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favorito: Favorito) {
            binding.tvTitulo.text = favorito.titulo
            binding.tvCategoria.text = favorito.categoria
            binding.tvPreco.text = Formatadores.preco(favorito.preco)
            binding.tvSalvoEm.text = "Salvo em ${Formatadores.data(favorito.salvoEm)}"
            binding.btnRemover.setOnClickListener { aoRemover(favorito) }
        }
    }
}
