package mainBot.buttons

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import mainBot.MenuNavigationTree

abstract class InlineButtons {
    companion object {
        lateinit var menu: MenuNavigationTree.TreeNode<String>

        val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
            listOf(
                InlineKeyboardButton.CallbackData(
                    text = "Test Inline Button",
                    callbackData = "testButton"
                )
            ),
            listOf(InlineKeyboardButton.CallbackData(text = "Show alert", callbackData = "showAlert"))
        )

        fun generateButtonsList(buttonList: MutableList<String>): InlineKeyboardMarkup {
            lateinit var list: MutableList<List<InlineKeyboardButton>>
            buttonList.forEach {
                list.add(listOf(InlineKeyboardButton.CallbackData(it, it)))
            }
            return InlineKeyboardMarkup.create(list)
        }

        fun generateMenuButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode().childrenNamesList)


        fun generateInfoButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(0)).childrenNamesList)


        fun generateFriendsButtons():InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(1)).childrenNamesList)


        fun generateNotificationsButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(2)).childrenNamesList)


        fun generateSupportDeveloperButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(3)).childrenNamesList)


        fun generateSingleNotificationsButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(2, 2)).childrenNamesList)


        fun generateMultipleNotificationsButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(2, 3)).childrenNamesList)

    }

}