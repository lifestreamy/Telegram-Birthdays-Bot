package databaseConnectivity

import databaseConnectivity.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DatabaseConnectivity {

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
}