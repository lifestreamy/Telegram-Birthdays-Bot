package databaseConnectivity.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object Users : Table("users") {
        val id = integer("id").autoIncrement()
        val name = text("name")
        val telegramId = text("telegramid")
        val birth_date = date("birth_date")
        override val primaryKey = PrimaryKey(id, name = "users_pkey")
}
