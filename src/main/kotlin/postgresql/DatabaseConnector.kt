package postgresql

import kotlinx.datetime.LocalDate
import postgresql.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseConnector(dbLink: String, dbUser: String, dbPassword: String) {

    val db : Database

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
        }
    }

    fun insertInUsers(pName: String, pUserId: Int,pTelegramId: String, pDate: LocalDate, pLocale : String, pTimeZone : Int): Int {
        val insertedNamesId = transaction {
            Users.insert {
                it[name] = pName
                it[user_id] = pUserId
                it[telegram_id] = pTelegramId
                it[birth_date] = pDate
                it[locale] = pLocale
                it[time_zone] = pTimeZone
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