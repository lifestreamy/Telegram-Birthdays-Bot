package postgresql.tables

import org.jetbrains.exposed.sql.Table

object Wishes : Table("wishes") {
    val id = integer("id").references(Users.id)
    val text = varchar("text", 200)
    val who_will_buy = integer("who_will_buy").references(Users.id).nullable()
    override val primaryKey = PrimaryKey(id, text, name = "wishes_pkey")
}