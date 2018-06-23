package me.jbusdriver.mvp.presenter

import me.jbusdriver.base.mvp.bean.ILink
import me.jbusdriver.mvp.LinkCollectContract

class LinkCollectPresenterImpl : BaseAbsCollectPresenter<LinkCollectContract.LinkCollectView, ILink>(), LinkCollectContract.LinkCollectPresenter {
    override fun lazyLoad() {
        onFirstLoad()
    }
}