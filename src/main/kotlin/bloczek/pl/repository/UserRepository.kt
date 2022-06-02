package bloczek.pl.repository

import bloczek.pl.db.DatabaseFactory.dbQuery
import bloczek.pl.enums.AccountType
import bloczek.pl.model.User
import bloczek.pl.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepository {

    suspend fun getById(id: Int): User = dbQuery {
        Users.select(Users.id eq  id)
            .map { mapUser(it) }
            .elementAtOrNull(0)!!
    }

    suspend fun getByUsername(username: String): User? = dbQuery {
        Users.select {Users.username eq username}
            .map { mapUser(it) }
            .elementAtOrNull(0)
    }

    suspend fun createUser(username: String, name: String, accountType: AccountType): Int = dbQuery {
        Users.insert {
            it[Users.username] = username
            it[Users.name] = name
            it[Users.accountType] = accountType
        }[Users.id]
    }


    private fun mapUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        name = row[Users.name],
        accountType = row[Users.accountType]
    )
}