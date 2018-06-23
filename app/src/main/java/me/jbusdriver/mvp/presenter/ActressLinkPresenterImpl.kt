package me.jbusdriver.mvp.presenter

import me.jbusdriver.base.mvp.bean.ILink
import me.jbusdriver.base.mvp.bean.PageInfo
import me.jbusdriver.mvp.bean.ActressInfo
import org.jsoup.nodes.Document

/**
 * 演员列表
 */
class ActressLinkPresenterImpl(val link: ILink) : LinkAbsPresenterImpl<ActressInfo>(link) {

    override fun stringMap(page: PageInfo, str: Document) = ActressInfo.parseActressList(str)

}