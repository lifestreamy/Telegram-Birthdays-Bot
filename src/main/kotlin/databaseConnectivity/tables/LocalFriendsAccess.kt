package databaseConnectivity.tables

import org.jetbrains.exposed.sql.Table

object LocalFriendsAccess : Table("local_friends_access") {
    val id1 = integer("id1").references(LocalFriends.id)
    val name = text("name").references(LocalFriends.name)
    val id2 = integer("id2").references(Users.id)
    override val primaryKey = PrimaryKey(id1, name, id2, name = "local_friends_access_pkey")
}