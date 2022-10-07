package databaseConnectivity.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object LocalFriends : Table("local_friends") {
    val id = integer("id").references(Users.id)
    val name = text("name")
    val birth_date = date("birth_date")
    override val primaryKey = PrimaryKey(id, name, name = "local_friends_pkey")
}