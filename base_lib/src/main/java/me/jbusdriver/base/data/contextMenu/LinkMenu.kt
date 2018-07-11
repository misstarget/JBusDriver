package me.jbusdriver.base.data.contextMenu

import me.jbusdriver.base.JBusManager
import me.jbusdriver.base.KLog
import me.jbusdriver.base.copy
import me.jbusdriver.base.mvp.bean.*
import me.jbusdriver.base.mvp.model.CollectModel
import me.jbusdriver.base.toast

/**
 * Created by Administrator on 2018/2/4.
 */
object LinkMenu {

    val movieActions by lazy {
        mapOf("复制标题" to { movie: Movie ->
            JBusManager.context.copy(movie.title)
            JBusManager.context.toast("已复制")
        }, "复制番号" to { movie: Movie ->
            JBusManager.context.copy(movie.code)
            JBusManager.context.toast("已复制")
        }, "收藏" to { movie: Movie ->
            CollectModel.addToCollectForCategory(movie.convertDBItem())
        }, "取消收藏" to { movie: Movie ->
            CollectModel.removeCollect(movie.convertDBItem())
        })
    }


    val actressActions by lazy {
        mapOf("复制名字" to { act: ActressInfo ->
            JBusManager.context.copy(act.name)
            JBusManager.context.toast("已复制")
        }, "收藏" to { act: ActressInfo ->
            CollectModel.addToCollectForCategory(act.convertDBItem())
        }, "取消收藏" to { act: ActressInfo ->
            CollectModel.removeCollect(act.convertDBItem())
        })

    }


    val linkActions by lazy {
        mapOf("复制" to { link: ILink ->
            KLog.d("copy $link ${link.des}")
            JBusManager.context.copy(link.des.split(" ").last())
            JBusManager.context.toast("已复制")
        }, "收藏" to { link ->
            CollectModel.addToCollectForCategory(link.convertDBItem())
        }, "取消收藏" to { link ->
            CollectModel.removeCollect(link.convertDBItem())
        })
    }


}