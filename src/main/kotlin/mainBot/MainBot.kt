package mainBot

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.types.ReplyKeyboardMarkup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mainBot.buttons.KeyboardButtons
import mainBot.menu.MenuTree
import mainBot.util.Util
import redis.RedisConnector
import java.io.FileInputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainBot {

    companion object {
        private fun getProp(key: String): String {
            val fis = FileInputStream("src/main/resources/config.properties")
            val prop = Properties()
            prop.load(fis);
            return prop.getProperty(key)
        }

        private suspend fun initialize() {
            val url = URL("https://api.telegram.org/bot$botToken/sendMessage")
            val c = withContext(Dispatchers.IO) {
                url.openConnection()
            } as HttpURLConnection
            try {
                c.requestMethod = "POST"
                c.doOutput = true
                val wr = OutputStreamWriter(c.outputStream)
                withContext(Dispatchers.IO) {
                    wr.write("chat_id=$myChatId&text=Backend started")
                    wr.flush()
                }
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
            myChatId = getProp("myChatId")
            dbLink = getProp("dbLink")
            dbUser = getProp("dbUser")
            dbPassword = getProp("dbPassword")
            initialize()
            val redis = RedisConnector("localhost")
            val regex = Util.convertMenuToRegex()
            val menuTree = MenuTree.instance
            val menuMap = menuTree.menuMap
            val bot = TelegramBot(botToken, "mainBot.controller") {
                rateLimits { // general limits
                    period = 1000
                    rate = 3
                }
            }
            bot.handleUpdates {
                onCommand("""(red|green|blue)""".toRegex()) {
                    message { "you typed ${update.message?.text} color" }.send(user, bot)
                }
                onCommand("/menu") {
                    // delete path for this user id
                    redis.del(update.message!!.from!!.id.toString())
                    message { "Main menu" }.markup {
                        ReplyKeyboardMarkup(KeyboardButtons.generateButtonsByPath())
                    }.send(user, bot)
                }
                onCommand(regex) {
                    val text = update.message?.text
                    val fromId = update.message!!.from!!.id
                    if (text != "Go back") {
                        val path = menuMap[text]
                        path?.let {
                            redis.setStack(fromId.toString(), *it)
                            message { "Opening $text" }.markup {
                                ReplyKeyboardMarkup(KeyboardButtons.generateButtonsByPath(*path))
                            }.send(user, bot)
                        }
                    } else {
                        val path = redis.removeLast(fromId.toString()).getAll(fromId.toString())
                        message { "Going back" }.markup {
                            ReplyKeyboardMarkup(KeyboardButtons.generateButtonsByPath(*path))
                        }.send(user, bot)
                    }
                }


            }


        }

    }
}
