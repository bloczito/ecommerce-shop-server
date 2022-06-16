package bloczek.pl.enums

enum class Subcategory(
    val title: String
) {
    // SWEATSHIRTS
    HOODIE("Z kapturem"),
    CRAWNECK("Bez kaptura"),

    // T-SHIRTS
    T_SHIRT("Z krótkim rękawem"),
    LONG_SLEEVE("Z długim rękawem"),
    POLO("Polo"),

    // TROUSERS
    CHINOS("Chinosy"),
    JEANS("Jeansy"),
    ELEGANT("Eleganckie"),

    // ACCESSORIES
    HATS("Czapki i kapelusze"),
    WATCHES("Zegarki"),
    STRIPES("Paski"),
    BACKPACKS("Plecaki")
}
