package mainBot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.*
import com.github.kotlintelegrambot.entities.ParseMode.MARKDOWN
import com.github.kotlintelegrambot.entities.ParseMode.MARKDOWN_V2
import com.github.kotlintelegrambot.entities.TelegramFile.ByUrl
import com.github.kotlintelegrambot.entities.dice.DiceEmoji
import com.github.kotlintelegrambot.entities.inlinequeryresults.InlineQueryResult
import com.github.kotlintelegrambot.entities.inlinequeryresults.InputMessageContent
import com.github.kotlintelegrambot.entities.inputmedia.InputMediaPhoto
import com.github.kotlintelegrambot.entities.inputmedia.MediaGroup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.logging.LogLevel
import com.google.gson.Gson
import mainBot.buttons.InlineButtons
import mainBot.buttons.KeyboardButtons
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainBot {
    companion object {
        inline fun <reified T> Gson.fromJson(json: String): T =
            this.fromJson<T>(json, T::class.java)

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
        fun main(args: Array<String>) {
            main(args[0], args[1], args[2], args[3], args[4])
        }

        fun main(botToken: String, myChatId: String, dbLink: String, dbUser: String, dbPassword: String) {
            Companion.botToken = botToken
            Companion.myChatId = myChatId
            Companion.dbLink = dbLink
            Companion.dbUser = dbUser
            Companion.dbPassword = dbPassword

            val menu = MenuTree.buildMenu()
            KeyboardButtons.menu = menu
            InlineButtons.menu = menu
            val infoKeyboardButtonsCallText = KeyboardButtons.menu.childrenNamesList[0]
            val friendsKeyboardButtonsCallText = KeyboardButtons.menu.childrenNamesList[1]
            val notificationsKeyboardButtonsCallText = KeyboardButtons.menu.childrenNamesList[2]
            val supportDeveloperCallText = KeyboardButtons.menu.childrenNamesList[3]
            val singleNotificationsCallText = KeyboardButtons.menu.getNode(mutableListOf(2, 2)).name
            val multipleNotificationsCallText = KeyboardButtons.menu.getNode(mutableListOf(2, 3)).name
            val bot = bot {
                token = botToken
                timeout = 30
                logLevel = LogLevel.Network.Body
                val commandsList = mutableListOf<BotCommand>()
                commandsList.add(BotCommand("/menu", "Main Menu"))
                commandsList.add(BotCommand("/botinfo", "Info about bot"))
                dispatch {

                    initialize()

                    message(Filter.Sticker) {
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id),
                            text = "You have received an awesome sticker \\o/"
                        )
                    }

                    message(Filter.Reply or Filter.Forward) {
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id),
                            text = "someone is replying or forwarding messages ..."
                        )
                    }

                    command("markdownV2") {
                        val markdownV2Text = """
                    *bold \*text*
                    _italic \*text_
                    __underline__
                    ~strikethrough~
                    *bold _italic bold ~italic bold strikethrough~ __underline italic bold___ bold*
                    [inline URL](http://www.example.com/)
                    [inline mention of a user](tg://user?id=123456789)
                    `inline fixed-width code`
                    ```kotlin
                    fun main() {
                        println("Hello Kotlin!")
                    }
                    ```
                """.trimIndent()
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = markdownV2Text,
                            parseMode = MARKDOWN_V2
                        )
                    }

                    command("botinfo") {
                        bot.setMyCommands(commandsList)
                        val chatId = ChatId.fromId(update.message!!.chat.id)
                        var startMessage = """
                    Your chat Id is ${chatId.id}
                    Developer: @lifestreamy
                    For suggestions to developer use /suggest
                    To open Bot Menu type /menu or use button below.
                    """.trimIndent()
                        var commandsInString = "\nAvailable commands:\n"
                        bot.getMyCommands().fold({ list ->
                            list.forEach {
                                commandsInString = "$commandsInString /${it.command} - ${it.description} \n"
                            }
                        },
                            {

                            }
                        )
                        startMessage = "$startMessage$commandsInString"
                        val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
                            listOf(InlineKeyboardButton.CallbackData(text = "Menu", callbackData = menu.name))
                        )
                        val result = bot.sendMessage(
                            chatId, text = startMessage, replyMarkup = inlineKeyboardMarkup
                        )
                        result.fold(
                            {
                                // do something here with the response
                            },
                            {
                                // do something with the error
                            }
                        )
                    }


                    callbackQuery("updatemyinfo") {
                        updateMyInfoInDatabase()
                    }
                    callbackQuery("suggest") {
                        addSuggestionToDatabase()
                    }
                    command("addfriend") {
                        val chatId = ChatId.fromId(message.chat.id)
                        val joinedArgs = args.joinToString()
                        val response: String
                        if (joinedArgs.isBlank()) {
                            response = "There is no text apart from command!"
                            bot.sendMessage(chatId = chatId, text = response)
                        } else if (joinedArgs.startsWith("@") && !joinedArgs.drop(1)
                                .contains("[^A-Za-z0-9_]".toRegex()) && joinedArgs.drop(1).length >= 5
                        ) {
                            response = joinedArgs.drop(1)
                            sendFriendRequest(response)
                            bot.sendMessage(
                                chatId = chatId,
                                text = "Sent friend request to: @$response"
                            )

                        } else {
                            response = "Incorrect username format"
                            bot.sendMessage(chatId = chatId, text = response)

                        }
                    }
                    command("markdown") {
                        val markdownText = "_Cool message_: *Markdown* is `beautiful` :P"
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = markdownText,
                            parseMode = MARKDOWN
                        )
                    }

                    /* command("menu")
                                        command("menu") {
                                            val chatId = ChatId.fromId(message.chat.id)
                                            val keyboardMarkup =
                                                KeyboardReplyMarkup(keyboard = KeyboardButtons.generateMenuButtons(), resizeKeyboard = true)
                                            bot.sendMessage(
                                                chatId = chatId, text = "",
                                                replyMarkup = keyboardMarkup
                                            )
                                        }
                                        */

                    /* keyboard buttons text calls
                                        text(infoKeyboardButtonsCallText) {
                                            if (update.message!!.text.equals(infoKeyboardButtonsCallText)) {
                                                val chatId = ChatId.fromId(update.message!!.chat.id)
                                                val keyboardMarkup =
                                                    KeyboardReplyMarkup(
                                                        keyboard = KeyboardButtons.generateInfoButtons(),
                                                        resizeKeyboard = true
                                                    )
                                                bot.sendMessage(
                                                    chatId = chatId, text = "Opened Info",
                                                    replyMarkup = keyboardMarkup
                                                )
                                            }
                                        }
                                        text(friendsKeyboardButtonsCallText) {
                                            if (update.message!!.text.equals(friendsKeyboardButtonsCallText)) {
                                                val chatId = ChatId.fromId(update.message!!.chat.id)
                                                val keyboardMarkup =
                                                    KeyboardReplyMarkup(
                                                        keyboard = KeyboardButtons.generateFriendsButtons(),
                                                        resizeKeyboard = true
                                                    )
                                                bot.sendMessage(
                                                    chatId = chatId, text = "Opened Friends",
                                                    replyMarkup = keyboardMarkup
                                                )
                                            }
                                        }
                                        text(notificationsKeyboardButtonsCallText) {
                                            if (update.message!!.text.equals(notificationsKeyboardButtonsCallText)) {
                                                val chatId = ChatId.fromId(update.message!!.chat.id)
                                                val keyboardMarkup =
                                                    KeyboardReplyMarkup(
                                                        keyboard = KeyboardButtons.generateNotificationsButtons(),
                                                        resizeKeyboard = true
                                                    )
                                                bot.sendMessage(
                                                    chatId = chatId, text = "Opened Notifications",
                                                    replyMarkup = keyboardMarkup
                                                )
                                            }
                                        }
                                        text(singleNotificationsCallText) {
                                            if (update.message!!.text.equals(singleNotificationsCallText)) {
                                                val chatId = ChatId.fromId(update.message!!.chat.id)
                                                var messageId = update.message!!.messageId
                                                val keyboardMarkup =
                                                    KeyboardReplyMarkup(
                                                        keyboard = KeyboardButtons.generateSingleNotificationsButtons(),
                                                        resizeKeyboard = true
                                                    )
                                                bot.sendMessage(
                                                    chatId = chatId, text = "Opened Single Notifications",
                                                    replyMarkup = keyboardMarkup
                                                )
                                            }
                                        }
                                        text(multipleNotificationsCallText) {
                                            if (update.message!!.text.equals(multipleNotificationsCallText)) {
                                                val chatId = ChatId.fromId(update.message!!.chat.id)
                                                var messageId = update.message!!.messageId
                                                val keyboardMarkup =
                                                    KeyboardReplyMarkup(
                                                        keyboard = KeyboardButtons.generateMultipleNotificationsButtons(),
                                                        resizeKeyboard = true
                                                    )
                                                bot.sendMessage(
                                                    chatId = chatId, text = "Opened Multiple Notifications",
                                                    replyMarkup = keyboardMarkup
                                                )

                                            }
                                        }
                                        text(supportDeveloperCallText) {
                                            if (update.message!!.text.equals(supportDeveloperCallText)) {
                                                val chatId = ChatId.fromId(update.message!!.chat.id)
                                                val keyboardMarkup =
                                                    KeyboardReplyMarkup(
                                                        keyboard = KeyboardButtons.generateSupportDeveloperButtons(),
                                                        resizeKeyboard = true
                                                    )
                                                bot.sendMessage(
                                                    chatId = chatId, text = "Opened Developer Support",
                                                    replyMarkup = keyboardMarkup
                                                )
                                            }
                                        }
                                        */
                    command("mediaGroup") {
                        bot.sendMediaGroup(
                            chatId = ChatId.fromId(message.chat.id),
                            mediaGroup = MediaGroup.from(
                                InputMediaPhoto(
                                    media = ByUrl("https://www.sngular.com/wp-content/uploads/2019/11/Kotlin-Blog-1400x411.png"),
                                    caption = "I come from an url :P"
                                ),
                                InputMediaPhoto(
                                    media = ByUrl("https://www.sngular.com/wp-content/uploads/2019/11/Kotlin-Blog-1400x411.png"),
                                    caption = "Me too!"
                                )
                            ),
                            replyToMessageId = message.messageId
                        )
                    }

                    callbackQuery(menu.name) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == menu.name) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = menu.name,
                                replyMarkup = InlineButtons.generateMenuButtons()
                            )
                        }
                    }
                    val info = menu.getNode(mutableListOf(0)).name
                    callbackQuery(info) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == info) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = info,
                                replyMarkup = InlineButtons.generateInfoButtons()
                            )
                        }
                    }
                    val friends = menu.getNode(mutableListOf(1)).name
                    callbackQuery(friends) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == friends) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = friends,
                                replyMarkup = InlineButtons.generateFriendsButtons()
                            )
                        }
                    }
                    val notifications = menu.getNode(mutableListOf(2)).name
                    callbackQuery(notifications) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == notifications) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = notifications,
                                replyMarkup = InlineButtons.generateNotificationsButtons()
                            )
                        }
                    }
                    val singleNotifications = menu.getNode(mutableListOf(2, 2)).name
                    callbackQuery(singleNotifications) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == singleNotifications) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = singleNotifications,
                                replyMarkup = InlineButtons.generateSingleNotificationsButtons()
                            )
                        }
                    }
                    val multipleNotifications = menu.getNode(mutableListOf(2, 3)).name
                    callbackQuery(multipleNotifications) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == multipleNotifications) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = multipleNotifications,
                                replyMarkup = InlineButtons.generateMultipleNotificationsButtons()
                            )
                        }
                    }
                    val supportDeveloper = menu.getNode(mutableListOf(3)).name
                    callbackQuery(supportDeveloper) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == supportDeveloper) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = supportDeveloper,
                                replyMarkup = InlineButtons.generateSupportDeveloperButtons()
                            )
                        }
                    }
                    val goBackFromInfo = menu.getNode(mutableListOf(0)).childrenList.last().name
                    callbackQuery(goBackFromInfo) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == goBackFromInfo) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = goBackFromInfo,
                                replyMarkup = InlineButtons.generateMenuButtons()
                            )
                        }
                    }
                    val goBackFromFriends = menu.getNode(mutableListOf(1)).childrenList.last().name
                    callbackQuery(goBackFromFriends) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == goBackFromFriends) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = goBackFromFriends,
                                replyMarkup = InlineButtons.generateMenuButtons()
                            )
                        }
                    }
                    val goBackFromNotifications = menu.getNode(mutableListOf(2)).childrenList.last().name
                    callbackQuery(goBackFromNotifications) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == goBackFromNotifications) {
                        bot.sendMessage(
                            chatId = ChatId.fromId(chatId),
                            text = goBackFromNotifications,
                            replyMarkup = InlineButtons.generateMenuButtons()
                        )
                        }
                    }
                    val goBackFromSupport = menu.getNode(mutableListOf(3)).childrenList.last().name
                    callbackQuery(goBackFromSupport) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == goBackFromSupport) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = goBackFromSupport,
                                replyMarkup = InlineButtons.generateMenuButtons()
                            )
                        }
                    }
                    val publicFriends = menu.getNode(mutableListOf(1,0)).name
                    callbackQuery(publicFriends) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == publicFriends) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = publicFriends,
                                replyMarkup = InlineButtons.generatePublicFriendsButtons()
                            )
                        }
                    }
                    val localFriends = menu.getNode(mutableListOf(1,1)).name
                    callbackQuery(localFriends) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == localFriends) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = localFriends,
                                replyMarkup = InlineButtons.generateLocalFriendsButtons()
                            )
                        }
                    }
                    val goBackFromPublicFriends = menu.getNode(mutableListOf(1,0)).childrenList.last().name
                    callbackQuery(goBackFromPublicFriends) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.data == goBackFromPublicFriends){
                        bot.sendMessage(
                            chatId = ChatId.fromId(chatId),
                            text = goBackFromPublicFriends,
                            replyMarkup = InlineButtons.generateFriendsButtons()
                        )
                    }
                    }
                    val goBackFromLocalFriends = menu.getNode(mutableListOf(1,1)).childrenList.last().name
                    callbackQuery(goBackFromLocalFriends) {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        if (callbackQuery.message!!.equals(goBackFromLocalFriends)) {
                            bot.sendMessage(
                                chatId = ChatId.fromId(chatId),
                                text = goBackFromLocalFriends,
                                replyMarkup = InlineButtons.generateFriendsButtons()
                            )
                        }
                    }
                    callbackQuery("testButton") {
                        val chatId = callbackQuery.message?.chat?.id ?: return@callbackQuery
                        bot.sendMessage(ChatId.fromId(chatId), callbackQuery.data)
                    }

                    callbackQuery(
                        callbackData = "showAlert",
                        callbackAnswerText = "HelloText",
                        callbackAnswerShowAlert = true
                    ) {
                        if (callbackQuery.message?.chat != null) {
                            val chatId = callbackQuery.message!!.chat.id
                            bot.sendMessage(chatId = ChatId.fromId(chatId), callbackQuery.data)
                        } else return@callbackQuery
                    }

                    text("ping") {
                        bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Pong")
                    }
                    text("pong") {
                        bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Ping")
                    }
                    text("play game") {
                        val moves = 1 + (0..20).random()
                        val winner: String = if (moves % 2 == 0) {
                            "Player 1 won!"
                        } else
                            "Player 2 won"
                        var waitTime = (900..1500).random()
                        for (x in 0..moves) {
                            if (x % 2 == 0)
                                bot.sendMessage(chatId = ChatId.fromId(update.message?.chat!!.id), text = "Ping")
                            else
                                bot.sendMessage(chatId = ChatId.fromId(update.message?.chat!!.id), text = "Pong")
                            Thread.sleep(waitTime.toLong())
                            waitTime = (900..1500).random()
                        }
                        bot.sendMessage(chatId = ChatId.fromId(update.message?.chat!!.id), text = winner)
                    }
                    text("Hello") {
                        bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Hello from dev")
                    }

                    text("Nice ass") {
                        if (update.message?.text.equals("Nice ass")) {
                            bot.sendMessage(chatId = ChatId.fromId(message.chat.id), text = "Nice ass, bro!")
                        }
                    }

                    location {
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = "Your location is (${location.latitude}, ${location.longitude})",
                            replyMarkup = ReplyKeyboardRemove()
                        )
                    }

                    contact {
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = "Hello, ${contact.firstName} ${contact.lastName}",
                            replyMarkup = ReplyKeyboardRemove()
                        )
                    }

                    channel {
                        // Handle channel update
                    }

                    inlineQuery {
                        val queryText = inlineQuery.query

                        if (queryText.isBlank() or queryText.isEmpty()) return@inlineQuery

                        val inlineResults = (0 until 5).map {
                            InlineQueryResult.Article(
                                id = it.toString(),
                                title = "$it. $queryText",
                                inputMessageContent = InputMessageContent.Text("$it. $queryText"),
                                description = "Add $it. before your word"
                            )
                        }
                        bot.answerInlineQuery(inlineQuery.id, inlineResults)
                    }

                    photos {
                        bot.sendMessage(
                            chatId = ChatId.fromId(message.chat.id),
                            text = "Wowww, awesome photos!!! :P"
                        )
                    }

                    command("diceAsDartboard") {
                        bot.sendDice(ChatId.fromId(message.chat.id), DiceEmoji.Dartboard)
                    }

                    dice {
                        bot.sendMessage(
                            ChatId.fromId(message.chat.id),
                            "A dice ${dice.emoji.emojiValue} with value ${dice.value} has been received!"
                        )
                    }

                    telegramError {
                        println(error.getErrorMessage())
                    }

                }

            }

            bot.startPolling()


        }

        fun createMenu() {

        }

        fun sendFriendRequest(name: String) {
            // TODO: query to DataBase
        }

        fun updateMyInfoInDatabase() {
            // TODO: add or update my name, telegramid and date of birth in Database
            // 
        }

        fun updateMyWishlist(wish: String) {

        }

        fun addSuggestionToDatabase() {

        }

    }
}