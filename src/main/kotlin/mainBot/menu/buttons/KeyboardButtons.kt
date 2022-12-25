package mainBot.menu.buttons


import eu.vendeli.tgbot.types.KeyboardButton
import mainBot.menu.MenuTree
import treeBuilder.Tree.TreeNode

abstract class KeyboardButtons {

    companion object {

        private fun generateButtonsList(buttonList: MutableList<String>): MutableList<List<KeyboardButton>> {
            val list = mutableListOf<List<KeyboardButton>>()
            buttonList.forEach {
                list.add(listOf(KeyboardButton(it)))
            }
            return list
        }

        fun generateButtonsByName(name: String): MutableList<List<KeyboardButton>> =
            when (name) {
                "Main Menu" -> generateButtonsList(MenuTree.menuRoot.childrenNamesList)
                getMenuNode(0).name -> generateButtonsList(getMenuNode(0).childrenNamesList)
                getMenuNode(1).name -> generateButtonsList(getMenuNode(1).childrenNamesList)
                getMenuNode(2).name -> generateButtonsList(getMenuNode(2).childrenNamesList)
                getMenuNode(3).name -> generateButtonsList(getMenuNode(3).childrenNamesList)
                getMenuNode(2, 2).name -> generateButtonsList(getMenuNode(2, 2).childrenNamesList)
                getMenuNode(2, 3).name -> generateButtonsList(getMenuNode(2, 3).childrenNamesList)
                else -> mutableListOf()
            }


        fun generateButtonsByPath(vararg path: Int): MutableList<List<KeyboardButton>> =
            generateButtonsList(getMenuNode(*path).childrenNamesList)


        private fun getMenuNode(vararg path: Int): TreeNode<String> =
            MenuTree.menuRoot.getNode(*path)

    }
}