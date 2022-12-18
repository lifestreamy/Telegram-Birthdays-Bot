package postgresql.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Notifications : Table("notifications") {
    val id = integer("id").references(Users.id)
    val type = text("type")
    val message = text("message")
    val date_time  = timestamp("date_time")
    override val primaryKey = PrimaryKey(id, type, message, date_time, name = "notifications_pkey")
}