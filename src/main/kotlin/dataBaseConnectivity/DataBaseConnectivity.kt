package dataBaseConnectivity

import mainBot.MainBot
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class DataBaseConnectivity {
    companion object {


        object Users : Table("users") {
            val id = integer("id").autoIncrement()
            val name = text("name")
            val telegramId = text("telegramid")
        }

        @JvmStatic
        fun main(args: Array<String>){
            main(args[0],args[1],args[2])
        }


        fun main(dbLink: String, dbUser: String, dbPassword: String) {
            val db = Database.connect(
                url = MainBot.dbLink,
                user = MainBot.dbUser, password = MainBot.dbPassword
            )
            val namesList = transaction {
                Users.selectAllBatched(10).toList()
            }
            println(namesList.toString())
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