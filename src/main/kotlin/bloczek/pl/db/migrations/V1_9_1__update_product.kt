package bloczek.pl.db.migrations

import bloczek.pl.model.Products
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1_9_1__update_product : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Products)
        }
    }

}