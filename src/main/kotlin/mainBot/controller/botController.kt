package mainBot.controller

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message
import eu.vendeli.tgbot.interfaces.sendAsync
import eu.vendeli.tgbot.types.KeyboardButton
import eu.vendeli.tgbot.types.ReplyKeyboardMarkup
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.isSuccess
import eu.vendeli.tgbot.types.internal.onFailure

/**
 * I CHOSE MANUAL HANDLING SO THIS CLASS IS JUST A PLACEHOLDER
 */

class botController {
    @CommandHandler(["/test"])
    suspend fun test(user : User, bot : TelegramBot) {
        val response = message { "test"
        }.sendAsync(user, bot).await()
        response.onFailure {  }
        if (response.isSuccess()) println("success")
//                        .onFailure {
//                        println("code: ${it.errorCode} description: ${it.description}") }
    }


    @CommandHandler(["/testkb"])
    suspend fun openMenu(user : User, bot : TelegramBot) {
        message {"Menu"}.markup {
            ReplyKeyboardMarkup(
                keyboard = mutableListOf(
                    listOf(
                        KeyboardButton("button1"),
                        KeyboardButton("button2")
                    )
                )

            )
        }.send(user, bot)
    }

}