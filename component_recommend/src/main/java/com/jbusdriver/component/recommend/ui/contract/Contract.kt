package com.jbusdriver.component.recommend.ui.contract

import me.jbusdriver.base.mvp.BaseView
import me.jbusdriver.base.mvp.presenter.BasePresenter

interface Contract {
    interface HotRecommendContract {
        interface HotRecommendView : BaseView.BaseListWithRefreshView
        interface HotRecommendPresenter :  BasePresenter.BaseRefreshLoadMorePresenter<HotRecommendView>
    }
}