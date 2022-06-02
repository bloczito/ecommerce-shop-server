package bloczek.pl.service

import bloczek.pl.enums.AccountType
import bloczek.pl.model.User
import bloczek.pl.repository.UserRepository

class UserService(private val userRepo: UserRepository) {

    suspend fun getById(id: Int): User = userRepo.getById(id)

    suspend fun getUser(externalId: String, accountType: AccountType) =
        userRepo.getByUsername(getUsername(externalId, accountType))

    suspend fun getOrCreateUser(externalId: String, name: String, accountType: AccountType): User {
        return getUser(externalId, accountType) ?: run {
            with(createUser(externalId, name, accountType)) {
                getById(this)
            }
        }
    }

    suspend fun createUser(externalUserid: String, name: String, accountType: AccountType): Int =
        userRepo.createUser(externalUserid, name, accountType)


    private fun getUsername(id: String, accountType: AccountType) = "${accountType.serviceName}-${id}"
}