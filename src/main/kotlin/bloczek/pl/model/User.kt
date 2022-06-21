package bloczek.pl.model

import bloczek.pl.db.postgresEnumeration
import bloczek.pl.enums.AccountType
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


object Users: Table() {
    val id: Column<Int> = integer("user_id").autoIncrement()

    val username: Column<String> = varchar("username", 255)
    val password: Column<String?> = varchar("password", 255).nullable()
    val externalId: Column<String?> = varchar("external_id", 255).nullable()
    val name: Column<String?> = varchar("name", 255).nullable()

    val city: Column<String?> = varchar("city", 255).nullable()
    val street: Column<String?> = varchar("street", 255).nullable()
    val postcode: Column<String?> = varchar("postcode", 255).nullable()

    val accountType = postgresEnumeration<AccountType>("account_type", "AccountType")

    override val primaryKey = PrimaryKey(id, name = "PK_Users_Id")

    init {
        index(true, username)
    }
}



data class User (
    val id: Int,
    val username: String,
    val password: String? = null,
    val externalId: String? = null,
    val name: String? = null,
    val city: String? = null,
    val street: String? = null,
    val postcode: String? = null,
    val accountType: AccountType
)