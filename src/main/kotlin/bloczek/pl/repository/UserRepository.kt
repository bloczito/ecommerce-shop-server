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

    suspend fun createDefaultUser(
        username: String,
        password: String,
        name: String? = null,
        city: String? = null,
        street: String? = null,
        postcode: String? = null
    ) = dbQuery {
        Users.insert {
            it[Users.username] = username
            it[Users.password] = password
            it[Users.name] = name
            it[Users.city] = city
            it[Users.street] = street
            it[Users.postcode] = postcode
            it[Users.accountType] = AccountType.DEFAULT
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

    suspend fun getPassword(username: String): String? = dbQuery {
        Users
            .slice(Users.password)
            .select(Users.username eq username)
            .map { it[Users.password] }
            .elementAtOrNull(0)
    }

    suspend fun getEmail(email: String): String? = dbQuery {
        Users.slice(Users.username)
            .select(Users.username eq email)
            .map { it[Users.username] }
            .elementAtOrNull(0)
    }

    suspend fun getUser(username: String, password: String): User? = dbQuery {
        Users.select {
            Users.username eq username
            Users.password eq password
        }
            .map { mapUser(it) }
            .elementAtOrNull(0)

    }


    private fun mapUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        password = row[Users.password],
        externalId = row[Users.externalId],
        name = row[Users.name],

        city = row[Users.city],
        street = row[Users.street],
        postcode = row[Users.postcode],

        accountType = row[Users.accountType]
    )
}