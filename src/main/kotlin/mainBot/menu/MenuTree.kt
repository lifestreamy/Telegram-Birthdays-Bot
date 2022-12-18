package mainBot.menu

import treeBuilder.Tree

class MenuTree private constructor(){


    companion object {

        private fun generateMenuMap() : Map<String, IntArray> {
            val map : MutableMap<String, IntArray> = mutableMapOf()
            menuTree.getAllNodes().forEach {
                map[it.name] = it.getPath().toIntArray()
            }
            return map.toMap()
        }

        val menuMap : Map<String, IntArray>;

        val instance = MenuTree
        lateinit var menuRoot: Tree.TreeNode<String>
            private set

        lateinit var menuTree: Tree<String>
            private set

        init {
            buildMenu()
            menuMap = generateMenuMap()
        }

        private fun buildMenu() {
            val menuRoot = Tree.TreeNode<String>("Main Menu")
            val menuTree = Tree(menuRoot)
            val menuList = mutableListOf(
                "Open Info Menu",  //0
                "Friends Actions", //1
                "Notifications Actions", //2
                "Support Developer" //3
            )
            menuTree.addNodes(menuRoot, Pair(intArrayOf(), menuList))
            val infoMenuList = mutableListOf(
                "See Your Info", "Add Your Info", "Edit my Wishlist", "Go back"
            )
            val friendsActions = mutableListOf(
                "Public friends", "Local friends", "Go back"
            )
            val notificationsActions = mutableListOf(
                "My active notifications", //0
                "Bot notifications", //1
                "Single friend reminders settings", //2
                "Multiple friend reminders settings", //3
                "Go back"
            )
            val supportDeveloperActions = mutableListOf(
                "Share this bot", "Donate", "Go back"
            )
            menuTree.addNodes(
                menuRoot, Pair(intArrayOf(0), infoMenuList), Pair(intArrayOf(1), friendsActions), Pair(
                    intArrayOf(2), notificationsActions
                ), Pair(
                    intArrayOf(3), supportDeveloperActions
                )
            )
            val publicFriends = mutableListOf(
                "My friend list",
                "Friend requests",
                "Send friend request",
                "Remove a friend",
                "See friend's profile information",
                "See friend's wishlist",
                "Go back"
            )
            val localFriends = mutableListOf(
                "My local friends",
                "Create a local friend",
                "Share my local friend",
                "See local friend profile info",
                "Go back"
            )
            menuTree.addNodes(
                menuRoot, Pair(
                    intArrayOf(1, 0), publicFriends
                ), Pair(intArrayOf(1, 1), localFriends)
            )
            val singleNotificationsActions = mutableListOf(
                "My active notifications",
                "Add a notification",
                "Change a notification",
                "Delete a notification",
                "Go back"
            )
            val multipleNotificationsActions = mutableListOf(
                "My active notifications",
                "Modify all notifications",
                "Add all friends' birthdays to notifications",
                "Remove all notifications",
                "Go back"
            )
            menuTree.addNodes(
                menuRoot,
                Pair(intArrayOf(2, 2), singleNotificationsActions),
                Pair(intArrayOf(2, 3), multipleNotificationsActions)
            )
            this.menuRoot = menuRoot
            this.menuTree = menuTree
        }


        @JvmStatic
        fun main(args: Array<String>) {
            buildMenu()
        }
    }
}