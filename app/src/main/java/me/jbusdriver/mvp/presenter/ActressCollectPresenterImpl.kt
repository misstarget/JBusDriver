package me.jbusdriver.mvp.presenter

import me.jbusdriver.mvp.ActressCollectContract
import me.jbusdriver.base.mvp.bean.ActressInfo

class ActressCollectPresenterImpl : BaseAbsCollectPresenter<ActressCollectContract.ActressCollectView, ActressInfo>(), ActressCollectContract.ActressCollectPresenter {

    override fun lazyLoad() {
        onFirstLoad()
    }
}