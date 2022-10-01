package mainBot.buttons

import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import mainBot.MenuNavigationTree

abstract class KeyboardButtons {
    companion object {
        lateinit var menu: MenuNavigationTree.TreeNode<String>

        fun generateButtonsList(buttonList: MutableList<String>): List<List<KeyboardButton>> {
            val list = mutableListOf<List<KeyboardButton>>()
            buttonList.forEach {
                list.add(listOf(KeyboardButton(it)))
            }
            return list.toList()
        }

        fun generateMenuButtons(): List<List<KeyboardButton>> =
            generateButtonsList(menu.getNode().childrenNamesList)


        fun generateInfoButtons(): List<List<KeyboardButton>> =
            generateButtonsList(menu.getNode(mutableListOf(0)).childrenNamesList)


        fun generateFriendsButtons(): List<List<KeyboardButton>> =
            generateButtonsList(menu.getNode(mutableListOf(1)).childrenNamesList)


        fun generateNotificationsButtons(): List<List<KeyboardButton>> =
            generateButtonsList(menu.getNode(mutableListOf(2)).childrenNamesList)


        fun generateSupportDeveloperButtons(): List<List<KeyboardButton>> =
            generateButtonsList(menu.getNode(mutableListOf(3)).childrenNamesList)


        fun generateSingleNotificationsButtons(): List<List<KeyboardButton>> {
            val list = generateButtonsList(menu.getNode(mutableListOf(2, 2)).childrenNamesList)
//            list.forEach{
//                println(it[0].text)
//            }
            return list
        }

        fun generateMultipleNotificationsButtons(): List<List<KeyboardButton>> =
            generateButtonsList(menu.getNode(mutableListOf(2,3)).childrenNamesList)

    }
}