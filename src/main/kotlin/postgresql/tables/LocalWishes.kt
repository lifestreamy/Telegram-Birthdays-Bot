package postgresql.tables

import org.jetbrains.exposed.sql.Table

object LocalWishes : Table("local_wishes") {
    val id = integer("id").references(LocalFriends.id)
    val name = varchar("name",32).references(LocalFriends.name)
    val text = varchar("text",200)
    val who_will_buy = varchar("who_will_buy",32).nullable()
    override val primaryKey = PrimaryKey(id, name, text, name = "local_wishes_pkey")
}