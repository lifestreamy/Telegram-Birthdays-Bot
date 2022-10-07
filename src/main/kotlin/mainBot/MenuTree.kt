package mainBot

import treeBuilder.Tree
import treeBuilder.Tree.TreeNode

class MenuTree {
    companion object {

        lateinit var menuRoot: TreeNode<String>
            private set


        fun buildMenu(): TreeNode<String> {
            val menuRoot = TreeNode<String>("Main Menu")
            val menuTree = Tree(menuRoot)
            val menuList = mutableListOf(
                "Open Info Menu",  //0
                "Friends Actions", //1
                "Notifications Actions", //2
                "Support Developer" //3
            )
            menuTree.addNodes(menuRoot, Pair(intArrayOf(), menuList))
            val infoMenuList = mutableListOf(
                "See Your Info", "Change Your Info", "Go back from info"
            )
            val friendsActions = mutableListOf(
                "Public friends", "Local friends", "Go back from friends"
            )
            val notificationsActions = mutableListOf(
                "My active notifications", //0
                "Bot notifications", //1
                "Single friend reminders settings", //2
                "Multiple friend reminders settings", //3
                "Go Back from notifications"
            )
            val supportDeveloperActions = mutableListOf(
                "Share this bot", "Donate", "Go back from support"
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
                "Go back from public friends"
            )
            val localFriends = mutableListOf(
                "My local friends",
                "Create a local friend",
                "Share my local friend",
                "See local friend profile info",
                "Go back from local friends"
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
                "Go Back"
            )
            val multipleNotificationsActions = mutableListOf(
                "My active notifications",
                "Modify all notifications",
                "Add all friends' birthdays to notifications",
                "Remove all notifications",
                "Go Back"
            )
            menuTree.addNodes(
                menuRoot,
                Pair(intArrayOf(2, 2), singleNotificationsActions),
                Pair(intArrayOf(2, 3), multipleNotificationsActions)
            )
            menuTree.visualizeTree()
            this.menuRoot = menuRoot
            return menuRoot
        }


        @JvmStatic
        fun main(args: Array<String>) {
            buildMenu()
        }
    }
}