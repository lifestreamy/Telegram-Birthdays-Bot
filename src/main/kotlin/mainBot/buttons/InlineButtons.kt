package mainBot.buttons
/*

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import treeBuilder.Tree.TreeNode

abstract class InlineButtons {

    companion object {

        lateinit var menu: TreeNode<String>


        fun generateButtonsList(buttonList: MutableList<String>): InlineKeyboardMarkup {
            val list: MutableList<List<InlineKeyboardButton>> = mutableListOf()
            buttonList.forEach {
                list.add(listOf(InlineKeyboardButton.CallbackData(text = it, callbackData = it)))
                println(it)
            }
            return InlineKeyboardMarkup.create(list.toList())
        }


        fun generateMenuButtons(): InlineKeyboardMarkup {
            return generateButtonsList(menu.getNode().childrenNamesList)
        }


        fun generateInfoButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(0)).childrenNamesList)


        fun generateFriendsButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(1)).childrenNamesList)

        fun generatePublicFriendsButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(1,0)).childrenNamesList)

        fun generateLocalFriendsButtons(): InlineKeyboardMarkup =
            generateButtonsList(menu.getNode(mutableListOf(1,1)).childrenNamesList)

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
*/
