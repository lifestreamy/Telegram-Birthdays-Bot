package postgresql.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date
/**
 * added locale RU/EN
 * added time_zone
 */
object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = text("name")
    val telegramId = text("telegramid")
    val birth_date = date("birth_date")
    val locale = varchar("locale", 2)
    val time_zone = integer("time_zone")
    override val primaryKey = PrimaryKey(id, name = "users_pkey")
}
