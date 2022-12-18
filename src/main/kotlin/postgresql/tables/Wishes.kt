package postgresql.tables

import org.jetbrains.exposed.sql.Table

object Wishes : Table("wishes") {
    val id = integer("id").references(Users.id)
    val text = text("text")
    val who_will_buy = integer("who_will_buy").nullable()
    override val primaryKey = PrimaryKey(id, text, name = "wishes_pkey")
}