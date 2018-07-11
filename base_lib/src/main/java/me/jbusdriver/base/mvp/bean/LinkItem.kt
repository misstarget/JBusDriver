package me.jbusdriver.base.mvp.bean

import me.jbusdriver.base.GSON
import me.jbusdriver.base.KLog
import me.jbusdriver.base.fromJson
import me.jbusdriver.base.http.JAVBusService
import me.jbusdriver.base.urlHost
import java.util.*

data class LinkItem(val type: Int, val createTime: Date, val key: String, val jsonStr: String, var categoryId: Int = -1) {
    var id: Int? = null
    fun getLinkValue() = doGet(type, jsonStr).also {
        if (it is ICollectCategory) {
            it.categoryId = this.categoryId
        }
    }
}

data class History(val type: Int, val createTime: Date, val jsonStr: String, var isAll: Boolean = false) {
    var id: Int? = null
    fun getLinkItem() = doGet(type, jsonStr)
}


private fun doGet(type: Int, jsonStr: String) = when (type) {
    1 -> GSON.fromJson<Movie>(jsonStr)
    2 -> GSON.fromJson<ActressInfo>(jsonStr)
    3 -> GSON.fromJson<Header>(jsonStr)
    4 -> GSON.fromJson<Genre>(jsonStr)
    5 -> GSON.fromJson<SearchLink>(jsonStr)
    6 -> GSON.fromJson<PageLink>(jsonStr)
    else -> error("$type : $jsonStr has no matched class ")
}.let { data ->
    val isXyz = data.link.urlHost.endsWith("xyz")
    if (isXyz) return@let data
    KLog.d("check type : $type  #$data hosts: ${JAVBusService.defaultImageUrlHosts}")
    val host: String by lazy { JAVBusService.defaultFastUrl }
//    val imageHost: Set<String> by lazy {
//        if (!isXyz) JAVBusService.defaultImageUrlHosts["default"] ?: mutableSetOf()
//        else JAVBusService.defaultImageUrlHosts["xyz"] ?: mutableSetOf()
//    }
    return@let when (data) {
        is Movie -> {
            val linkChange = data.link.urlHost != host
//            val imageChange =  imageHost.isNotEmpty() && data.imageUrl.urlHost !in imageHost
//            if (imageChange){
//                imageHost.map { it + data.imageUrl.urlPath }.filter { Glide.with(JBus).asFile().load(it).preload()  != null}
//            }
            data.copy(link = if (linkChange) data.link.replace(data.link.urlHost, host) else data.link, imageUrl = /*if (imageChange) data.imageUrl.replace(data.imageUrl.urlHost, imageHost) else*/ data.imageUrl)
        }
        is ActressInfo -> {
            val linkChange = data.link.urlHost != host
//            val imageChange =  imageHost.isNotEmpty() && data.avatar.urlHost !in imageHost && !data.avatar.endsWith("nowprinting.gif")
            data.copy(link = if (linkChange) data.link.replace(data.link.urlHost, host) else data.link, avatar = /*if (imageChange) data.avatar.replace(data.avatar.urlHost, imageHost) else*/ data.avatar)
        }
        else -> {
            val linkChange = data.link.urlHost != host
            if (linkChange) {
                when (data) {
                    is Header -> data.copy(link = data.link.replace(data.link.urlHost, host))
                    is Genre -> data.copy(link = data.link.replace(data.link.urlHost, host))
                    else -> data
                }
            } else data
        }
    }.apply {
        KLog.d("check link : $this")
    }

}
