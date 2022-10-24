package mainBot.types

class TelegramName(name : String) {
    val startChar = '@'
    var valid = false
    var body : String?
    init{
        valid = name.startsWith(startChar)
        if (valid) this.body = name.drop(1) else this.body = null
    }
}