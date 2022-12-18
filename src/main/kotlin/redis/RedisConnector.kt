package redis

import com.sxtanna.database.Kedis
import com.sxtanna.database.config.KedisConfig
import redis.clients.jedis.Jedis

class RedisConnector(address: String, port: Int = 6379) {

    private val kedis: Kedis
    private val resource: Jedis

    init {
        kedis = Kedis(
            KedisConfig(
                server = KedisConfig.ServerOptions(address = address, port = port),
                user = KedisConfig.UserOptions(auth = "qwe123qwe", defaultDB = 0),
                pool = KedisConfig.PoolOptions(maxSize = 1000, idleSize = 1000, timeout = 0)
            )
        )
        kedis.enable()
        resource = kedis.resource()
    }

    fun set(key: String, value: String): String {
        return resource.set(key, value)
    }

    @Deprecated("")
    fun getFirst(key: String): String {
        return resource.mget(key)[0]
    }

    @Deprecated("")
    fun getSecond(key: String): String? {
        return if (resource.mget(key).size == 2) resource.mget(key)[1] else null
    }

//
//    fun del(key: String): RedisConnector {
//        resource.del(key)
//        return this
//    }


//    //clear user menu history, reset list to [0] (0th node of the menu tree)
//    fun resetStack(key: String): RedisConnector {
//        resource.del(key)
//        resource.lpush(key, "0")
//        return this
//    }

    fun del(key :String) : RedisConnector {
        resource.del(key)
        return this
    }

    // TODO: stack rPush rPop

    //get index value of the last menu node
    fun getLast(key: String): Int {
        return resource.lrange(key, -1, -1)[0].toInt()
    }

    fun addLast(key: String, value: String): RedisConnector {
        resource.rpush(key, value)
        return this
    }

    fun removeLast(key: String): RedisConnector {
        resource.rpop(key)
        return this
    }

    fun setStack(key: String, vararg path: Int): RedisConnector {
        resource.del(key)
        path.forEach {
            resource.rpush(key, it.toString())
        }
        return this
    }

    fun getAll(key: String): IntArray {
        return resource.lrange(key, 0, -1).map { it.toInt() }.toIntArray()
    }

    fun getSize(key: String): Int {
        return resource.lrange(key, 0, -1).size
    }

}