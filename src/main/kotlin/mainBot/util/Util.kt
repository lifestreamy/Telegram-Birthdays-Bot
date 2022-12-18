package mainBot.util

import mainBot.menu.MenuTree

object Util {

    fun convertMenuToRegex(): Regex {
        return MenuTree.menuTree.getAllNodes().associateBy { it.name }.keys.joinToString("|").toRegex()
    }
}