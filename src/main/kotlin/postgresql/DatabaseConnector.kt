package postgresql

import kotlinx.datetime.LocalDate
import postgresql.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseConnector(dbLink: String, dbUser: String, dbPassword: String) {

    private val db : Database

    init {
        db = Database.connect(
            url = dbLink,
            user = dbUser, password = dbPassword
        )
    }


    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            main(args[0], args[1], args[2])
        }


        fun main(dbLink: String, dbUser: String, dbPassword: String) {
            val db = Database.connect(
                url = dbLink,
                user = dbUser, password = dbPassword
            )
            val namesList = transaction {
                Users.selectAllBatched(10).toList().forEach {
                    it.forEach { row ->
                        println(row)
                    }
                }
            }
//            println(namesList.toString())
//    val insertedNamesId = transaction {
//        Users.insert {
//            it[name] = "Yorky"
//            it[telegramId] = "@Pedik"
//        } get Users.id // fetches the auto generated ID
//    }
//    println(insertedNamesId)
        }
    }

    fun insertInUsers(pName: Column<String>, pID: Column<String>, pDate: Column<LocalDate>, pLocale : Column<String>): Int {
        val insertedNamesId = transaction {
            Users.insert {
                it[name] = pName
                it[telegramId] = pID
                it[birth_date] = pDate
                it[locale] = pLocale
            } get Users.id // fetches the auto generated ID
        }
        return insertedNamesId
    }

    fun insertInWishes() {}
    fun insertInLocalWishes() {}
    fun insertInNotifications() {}
    fun insertInFriendships() {}
    fun insertInLocalFriends() {}
    fun insertInLocalFriendsAccess() {}



}