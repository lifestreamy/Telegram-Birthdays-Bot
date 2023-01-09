package postgresql.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = text("name").nullable()
    val user_id = integer("user_id").uniqueIndex()
    val telegram_id = text("telegram_id").nullable()
    val birth_date = date("birth_date").nullable()
    val locale = varchar("locale", 2).nullable()
    val time_zone = integer("time_zone").nullable()
    override val primaryKey = PrimaryKey(id, name = "users_pkey")
}
