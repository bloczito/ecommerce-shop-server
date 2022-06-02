package bloczek.pl.model

import bloczek.pl.db.postgresEnumeration
import bloczek.pl.enums.AccountType
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


object Users: Table() {
    val id: Column<Int> = integer("user_id").autoIncrement()

    val username: Column<String> = varchar("username", 255)
    val name: Column<String> = varchar("name", 255)
    val accountType = postgresEnumeration<AccountType>("account_type", "AccountType")

    override val primaryKey = PrimaryKey(id, name = "PK_Users_Id")
}



data class User (
    val id: Int,
    val username: String,
    val name: String? = null,
    val accountType: AccountType
)