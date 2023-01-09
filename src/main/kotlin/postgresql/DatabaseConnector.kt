package postgresql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.datetime.LocalDate
import postgresql.tables.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.min


// TODO: IMPLEMENT DAO INSTEAD OF DSL

class DatabaseConnector(dbLink: String, dbUser: String, dbPassword: String) {

    val db: Database
    private val logger: Logger = LoggerFactory.getLogger(DatabaseConnector::class.java)
    private val maxBackoffMs = 16000L
    private val defaultBackoffSequenceMs = generateSequence(1000L) { min(it * 2, maxBackoffMs) }

    init {
        val hikariDataSource = createHikariDataSourceWithRetry(dbLink, dbUser, dbPassword)
        db = Database.connect(hikariDataSource)
    }

    /**
     * Creates a HikariDataSource and returns it. If any exception is thrown, the operation is retried after x millis as
     * defined in the backoff sequence. If the sequence runs out of entries, the operation fails with the last
     * encountered exception.
     */
    private tailrec fun createHikariDataSourceWithRetry(
        url: String, usr: String, psw: String, backoffSequenceMs: Iterator<Long> = defaultBackoffSequenceMs.iterator()
    ): HikariDataSource {
        try {
            val config = HikariConfig().apply {
                jdbcUrl = url
                username = usr
                password = psw
                driverClassName = "org.postgresql.Driver"
                maximumPoolSize = 10
            }
            return HikariDataSource(config)
        } catch (ex: Exception) {
            logger.error("Failed to create data source ${ex.message}")
            if (!backoffSequenceMs.hasNext()) throw ex
        }
        val backoffMillis = backoffSequenceMs.next()
        logger.info("Trying again in $backoffMillis millis")
        Thread.sleep(backoffMillis)
        return createHikariDataSourceWithRetry(url, usr, psw, backoffSequenceMs)
    }


    fun insertInUsers(
        pName: String?, pUserId: Int, pTelegramId: String?, pDate: LocalDate?, pLocale: String?, pTimeZone: Int?
    ): Int {
        val insertedNamesId = transaction {
            Users.insert() {
//                it[name] = pName
//                it[user_id] = pUserId
//                it[telegram_id] = pTelegramId
//                it[birth_date] = pDate
//                it[locale] = pLocale
//                it[time_zone] = pTimeZone

            } get Users.id // fetches the auto generated ID
        }
        return insertedNamesId
    }

    /* fun insertInWishes() {}

     fun insertInLocalWishes() {}

     fun insertInNotifications() {}

     fun insertInFriendships() {}

     fun insertInLocalFriends() {}

     fun insertInLocalFriendsAccess() {}*/

    fun <T : Table> T.insert(body: T.(InsertStatement<Number>) -> Unit): InsertStatement<Number> = InsertStatement<Number>(this).apply {
        body(this)
        execute(TransactionManager.current())
    }


    fun executeUsers(columns: List<List<String>>) {
        transaction {
            Users.batchInsertOnDuplicateKeyUpdate(columns, listOf("id"), listOf()) { batch, user ->
//                batch[id] = user
//                batch[user_id] = user
            }
        }
    }

    // The below code is just a copy-paste that should actually be in the lib
    class BatchInsertUpdateOnDuplicate1(
        table: Table, private val keyColumns: List<String>, private val onDupUpdate: List<String>
    ) : BatchInsertStatement(table, false) {
        override fun prepareSQL(transaction: Transaction): String {
            val onUpdateSQL = if (onDupUpdate.isNotEmpty()) {
                " ON CONFLICT (${keyColumns.joinToString()}) DO UPDATE SET " + onDupUpdate.joinToString { "$it=excluded.$it" }
            } else ""
            return super.prepareSQL(transaction) + onUpdateSQL
        }
    }

    private fun <T : Table, E> T.batchInsertOnDuplicateKeyUpdate(
        data: List<E>, keyColumns: List<String>, onDupUpdateColumns: List<String>, body: T.(BatchInsertUpdateOnDuplicate1, E) -> Unit
    ) {
        data.takeIf { it.isNotEmpty() }?.let {
            val insert = BatchInsertUpdateOnDuplicate1(this, keyColumns, onDupUpdateColumns)
            data.forEach {
                insert.addBatch()
                body(insert, it)
            }
            TransactionManager.current().exec(insert)
        }
    }
}