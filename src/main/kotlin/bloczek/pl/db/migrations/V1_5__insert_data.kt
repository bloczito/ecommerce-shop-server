package bloczek.pl.db.migrations

import bloczek.pl.enums.Category
import bloczek.pl.enums.Subcategory
import bloczek.pl.model.Brands
import bloczek.pl.model.Products
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal

class V1_5__insert_data : BaseJavaMigration(){
    override fun migrate(context: Context?) {
        transaction {
            Brands.insert { it[name] = "Brand A" }
            Brands.insert { it[name] = "Brand B" }
            Brands.insert { it[name] = "Brand C" }

            val brandId = Brands.selectAll().map { it[Brands.id] }[0]

            Products.insert {
                it[name] = "Product A"
                it[description] = "Lorem ipsum"
                it[price] = BigDecimal(153)
                it[url] = "https://img01.ztat.net/article/spp-media-p1/2990c0e6c2033178ba8fb02075ce9a1e/fae1516392e24767ada318877bbbd121.jpg?imwidth=762"
                it[Products.brandId] = brandId
                it[category] = Category.SWEATSHIRTS
                it[subcategory] = Subcategory.HOODIE
            }

            Products.insert {
                it[name] = "Product B"
                it[description] = "Lorem ipsum"
                it[price] = BigDecimal(1167)
                it[url] = "https://www.zalando.pl/adidas-originals-unisex-bluza-carbon-ad121009m-q11.html"
                it[Products.brandId] = brandId
                it[category] = Category.SWEATSHIRTS
                it[subcategory] = Subcategory.HOODIE
            }

            Products.insert {
                it[name] = "Product C"
                it[description] = "Lorem ipsum"
                it[price] = BigDecimal(56)
                it[url] = "https://www.zalando.pl/yourturn-butterflies-hoodie-bluza-pink-yo12100lz-j11.html"
                it[Products.brandId] = brandId
                it[category] = Category.SWEATSHIRTS
                it[subcategory] = Subcategory.HOODIE
            }

            Products.insert {
                it[name] = "Product D"
                it[description] = "Lorem ipsum"
                it[price] = BigDecimal(999)
                it[url] = "https://www.zalando.pl/yourturn-bluza-z-kapturem-pink-yo121009q-j11.html"
                it[Products.brandId] = brandId
                it[category] = Category.SWEATSHIRTS
                it[subcategory] = Subcategory.HOODIE
            }

        }
    }
}