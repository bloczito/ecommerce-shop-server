package bloczek.pl.db.migrations

import bloczek.pl.model.Users
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V1_6__create_users : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            exec("CREATE TYPE AccountType AS ENUM ('GOOGLE' ,'GITHUB')")
            SchemaUtils.create(Users)
        }
    }
}