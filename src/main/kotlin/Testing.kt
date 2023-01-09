import kotlinx.datetime.LocalDate
import postgresql.DatabaseConnector

fun main() {
    val db = DatabaseConnector("jdbc:postgresql://localhost/telegram_bot_db","postgres", "1234")
    db.insertInUsers("John",123456789, "@john",LocalDate(1000,10,10),"EN",+10)
}