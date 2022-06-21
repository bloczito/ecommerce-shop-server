package bloczek.pl.service

import bloczek.pl.dto.SignUpDto
import bloczek.pl.dto.UserDto
import bloczek.pl.enums.AccountType
import bloczek.pl.model.User
import bloczek.pl.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt

class UserService(private val userRepo: UserRepository) {

    suspend fun getById(id: Int): User = userRepo.getById(id)

    suspend fun getByUsername(username: String) = userRepo.getByUsername(username)

    private suspend fun getUser(externalId: String, accountType: AccountType) =
        userRepo.getByUsername(getUsername(externalId, accountType))

    suspend fun getOrCreateUser(externalId: String, name: String, accountType: AccountType): User {
        return getUser(externalId, accountType) ?: run {
            with(createUser(externalId, name, accountType)) {
                getById(this)
            }
        }
    }

    private suspend fun createUser(externalUserid: String, name: String, accountType: AccountType): Int =
        userRepo.createUser(externalUserid, name, accountType)

    suspend fun createDefaultUser(dto: SignUpDto): Int  =
        userRepo.createDefaultUser(dto.email, hashPassword(dto.password), dto.name, dto.city, dto.street, dto.postcode)

    suspend fun updateUser(userId: Int, dto: UserDto): Int = userRepo.updateUser(userId, dto)

    private fun getUsername(id: String, accountType: AccountType) = "${accountType.serviceName}-${id}"

    private fun hashPassword(pwd: String): String = BCrypt.hashpw(pwd, BCrypt.gensalt())

    private fun verifyPassword(pwd: String, hashedPwd: String): Boolean = BCrypt.checkpw(pwd, hashedPwd)

    suspend fun isEmailAvailable(email: String): Boolean = userRepo.getEmail(email) == null

    suspend fun signIn(username: String, password: String): User? {
        val user = getByUsername(username) ?: return null

        if (user.password.isNullOrBlank()) return null

        return if (verifyPassword(password, user.password)) user else null
    }
}