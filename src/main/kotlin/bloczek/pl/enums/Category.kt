package bloczek.pl.enums

enum class Category(
    val title: String
) {
    T_SHIRTS("T-shirty") {
        override fun getSubcategories(): List<Subcategory> =
            listOf(Subcategory.T_SHIRT, Subcategory.LONG_SLEEVE, Subcategory.POLO)
    },
    SWEATSHIRTS("Bluzy") {
        override fun getSubcategories(): List<Subcategory> =
            listOf(Subcategory.HOODIE, Subcategory.CRAWNECK)
    },
    TROUSERS("Spodnie") {
        override fun getSubcategories(): List<Subcategory> =
            listOf(Subcategory.CHINOS, Subcategory.JEANS, Subcategory.ELEGANT)
    },
    ACCESSORIES("Akcesoria") {
        override fun getSubcategories(): List<Subcategory> =
            listOf(Subcategory.HATS, Subcategory.WATCHES, Subcategory.STRIPES, Subcategory.BACKPACKS)
    };

    abstract fun getSubcategories(): List<Subcategory>
}
