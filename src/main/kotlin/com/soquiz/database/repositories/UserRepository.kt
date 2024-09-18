package com.soquiz.database.repositories


import com.soquiz.database.tables.UserDao
import com.soquiz.database.tables.UsersTable
import com.soquiz.models.RegisterUser
import com.soquiz.models.Role
import com.soquiz.models.User
import com.soquiz.models.UserRole
import com.soquiz.utils.hashPassword
import com.soquiz.utils.suspendTransaction

class UserRepository {

    suspend fun registerUser(user: RegisterUser) = suspendTransaction {
        UserDao.new {
            name = user.name
            email = user.email
            password = hashPassword(user.password)
            role = Role.USER.name
        }
    }

    suspend fun getUserByEmail(email: String): User? = suspendTransaction {

        val userDao = UserDao.find {
            UsersTable.email eq email
        }

        userDao.map {
            User(
                id = it.id.value,
                name = it.name,
                email = it.email,
                role = it.role,
                password = it.password
            )
        }.firstOrNull()
    }

    suspend fun setUserRole(userRole: UserRole) = suspendTransaction {

        val userDao = UserDao.find {
            UsersTable.email eq userRole.email
        }.firstOrNull()

        userDao?.apply {
            role = userRole.role
        }
    }

}