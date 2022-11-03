package mainBot

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message
import mainBot.buttons.InlineButtons
import mainBot.buttons.KeyboardButtons
import mainBot.menu.MenuTree
import mainBot.types.TelegramName
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.sql.Date
import java.sql.Time

class MainBot {
    companion object {
        private fun initialize() {
            val url = URL("https://api.telegram.org/bot$botToken/sendMessage")
            val c = url.openConnection() as HttpURLConnection
            try {
                c.requestMethod = "POST"
                c.doOutput = true
                val wr = OutputStreamWriter(c.outputStream)
                wr.write("chat_id=$myChatId&text=Backend started")
                wr.flush()
                println("URL : $url")
                println("Response Code : ${c.responseCode}")
            } finally {
                c.disconnect()
            }

        }

        lateinit var myChatId: String
        lateinit var botToken: String
        lateinit var dbLink: String
        lateinit var dbUser: String
        lateinit var dbPassword: String

        @JvmStatic
        suspend fun main(args: Array<String>) {
            main(args[0], args[1], args[2], args[3], args[4])
        }

        // TODO: migrate to https://github.com/vendelieu/telegram-bot

        suspend fun main(botToken: String, myChatId: String, dbLink: String, dbUser: String, dbPassword: String) {
            val bot = TelegramBot(botToken, "eu.vendeli.samples.controller")
            Companion.botToken = botToken
            Companion.myChatId = myChatId
            Companion.dbLink = dbLink
            Companion.dbUser = dbUser
            Companion.dbPassword = dbPassword
            bot.update.setListener {
                message(it.message?.text ?: "").send(it.message?.from?.id ?: 0, bot)
            }
        }
    }
}
