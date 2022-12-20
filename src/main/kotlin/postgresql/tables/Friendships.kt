package postgresql.tables

import org.jetbrains.exposed.sql.Table

object Friendships : Table("friendships") {
    val id1 = integer("id1").references(Users.id)
    val id2 = integer("id2").references(Users.id)
    val accepted = bool("accepted")
    override val primaryKey = PrimaryKey(id1,id2, name = "friendships_pkey")

}