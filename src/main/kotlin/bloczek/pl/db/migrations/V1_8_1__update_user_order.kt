package bloczek.pl.db.migrations

import bloczek.pl.model.Orders
import bloczek.pl.model.Users
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1_8_1__update_user_order : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(Orders, Users)
        }
    }
}