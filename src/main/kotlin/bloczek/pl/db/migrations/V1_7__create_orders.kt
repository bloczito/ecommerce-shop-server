package bloczek.pl.db.migrations

import bloczek.pl.model.OrderElements
import bloczek.pl.model.Orders
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1_7__create_orders : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.create(Orders)
            SchemaUtils.create(OrderElements)
        }
    }

}