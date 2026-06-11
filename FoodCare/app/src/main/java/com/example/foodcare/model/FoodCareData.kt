package com.example.foodcare.model



// ─── Sample data ──────────────────────────────────────────────────────────────

object FoodCareData {

    data class ProdutoFeedUi(
        val id: Int,
        val nome: String,
        val categoria: String,
        val distancia: String,
        val validade: String,
        val imageColor: Long,
        val descricao: String,
        val doador: String
    )

    val produtos: List<ProdutoFeedUi> = emptyList()



}
