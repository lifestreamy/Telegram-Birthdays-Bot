package mainBot

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.utils.inlineKeyboardMarkup
import kotlinx.coroutines.runBlocking
import mainBot.menu.MenuTree
import mainBot.types.TelegramName
import java.io.FileInputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.sql.Date
import java.sql.Time
import java.util.*

class MainBot {

    companion object {
        private fun getProp(key: String): String {
            val fis = FileInputStream("src/main/resources/config.properties")
            val prop = Properties()
            prop.load(fis);
            return prop.getProperty(key)
        }

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

        // TODO: migrate to https://github.com/vendelieu/telegram-bot
        @JvmStatic
        fun main(args: Array<String>): Unit = runBlocking {
            botToken = getProp("botToken")
            val bot = TelegramBot(botToken, "eu.vendeli.samples.controller")
            myChatId = getProp("myChatId")
            dbLink = getProp("dbLink")
            dbUser = getProp("dbUser")
            dbPassword = getProp("dbPassword")
//            bot.update.setListener {
//                message(it.message?.text ?: "").send(it.message?.from?.id ?: 0, bot)
//            }
            bot.handleUpdates {
                onCommand("/menu"){
                    message { "Open Menu" }.markup {
                        inlineKeyboardMarkup {
                            "button1" callback "button1"
                            "button2" callback "button2"
                            "button3" callback "button3"
                            "button4" callback "button4"
                            "button4" callback "button4"
                            "button4" callback "button4"
                            "button4" callback "button4"
                        }
                    }.send(user, bot)
                }
            }
        }
    }
}
