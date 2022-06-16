package bloczek.pl.repository

import bloczek.pl.db.DatabaseFactory.dbQuery
import bloczek.pl.dto.UserDto
import bloczek.pl.enums.AccountType
import bloczek.pl.model.User
import bloczek.pl.model.Users
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

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

    suspend fun createUser(externalId: String, name: String, accountType: AccountType): Int = dbQuery {
        Users.insert {
            it[Users.username] = "${accountType.serviceName}-${externalId}"
            it[Users.externalId] = externalId
            it[Users.name] = name
            it[Users.accountType] = accountType
        }[Users.id]
    }

    suspend fun updateUser(userId: Int, dto: UserDto): Int = dbQuery {
        Users.update({ Users.id eq userId }) {
            it[name] = dto.customerName
            it[city] = dto.city
            it[street] = dto.street
            it[postcode] = dto.postcode
        }
    }


    private fun mapUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        externalId = row[Users.externalId],
        name = row[Users.name],

        city = row[Users.city],
        street = row[Users.street],
        postcode = row[Users.postcode],

        accountType = row[Users.accountType]
    )
}