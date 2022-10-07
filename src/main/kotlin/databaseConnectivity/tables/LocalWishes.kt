package databaseConnectivity.tables

import org.jetbrains.exposed.sql.Table

object LocalWishes : Table("local_wishes") {
    val id = integer("id").references(LocalFriends.id)
    val name = text("name").references(LocalFriends.name)
    val text = text("name")
    val who_will_buy = text("who_will_buy").nullable()
    override val primaryKey = PrimaryKey(id, name, text, name = "local_wishes_pkey")
}