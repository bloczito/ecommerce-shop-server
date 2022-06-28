package bloczek.pl.db.migrations

import bloczek.pl.db.DatabaseFactory.dbQuery
import bloczek.pl.model.*
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1_0__create_models : BaseJavaMigration() {
    override  fun migrate(context: Context?) {
        transaction {
//            exec("CREATE TYPE Category AS ENUM ('T_SHIRTS', 'SWEATSHIRTS', 'TROUSERS', 'ACCESSORIES')")
//            exec("CREATE TYPE Subcategory AS ENUM ('HOODIE' ,'CRAWNECK', 'T_SHIRT', 'LONG_SLEEVE', 'POLO', 'CHINOS', " +
//                    "'JEANS', 'ELEGANT', 'HATS', 'WATCHES', 'STRIPES', 'BACKPACKS')")
//            exec("CREATE TYPE AccountType AS ENUM ('GOOGLE' ,'GITHUB', 'DEFAULT')")
            SchemaUtils.createMissingTablesAndColumns(Brands)
            SchemaUtils.create(Products)
            SchemaUtils.create(Users)
            SchemaUtils.create(Orders)
            SchemaUtils.create(OrderElements)

        }
    }
}