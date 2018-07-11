package me.jbusdriver.base.db.bean

import me.jbusdriver.base.mvp.bean.*


data class DBPage(val currentPage: Int, val totalPage: Int, val pageSize: Int = 20)

val DBPage.toPageInfo
    inline get() = PageInfo(currentPage, Math.min(currentPage + 1, totalPage))