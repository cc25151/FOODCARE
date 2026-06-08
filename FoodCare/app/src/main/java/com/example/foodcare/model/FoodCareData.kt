package com.example.foodcare.model







data class AlimentoFormData(
    val idAlimento : Int,
    val nome: String,
    val descricao: String,
    val quantidade: Int,
    val validade: String,
    val idCategoria: Int
)

data class CategoriaVisual(val nome: String, val emoji: String)

data class Categoria(val nome: String)

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
    val categoriasVisuais = listOf(
        CategoriaVisual("Marmitas", "🍱"),
        CategoriaVisual("Não perecíveis", "🌾"),
        CategoriaVisual("Frutas", "🍎"),
        CategoriaVisual("Laticínios", "🧀"),
        CategoriaVisual("Bebidas", "🧃")
    )


}
