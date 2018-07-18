package me.jbusdriver.component.magnet.ui.fragment

import android.support.v4.app.Fragment
import io.reactivex.schedulers.Schedulers
import me.jbusdriver.base.common.C

import me.jbusdriver.base.data.AppConfiguration
import me.jbusdriver.base.mvp.fragment.TabViewPagerFragment
import me.jbusdriver.component.magnet.config.MagnetKeys
import me.jbusdriver.component.magnet.config.saveMagnetKeys
import me.jbusdriver.component.magnet.loader.MagnetLoaders
import me.jbusdriver.component.magnet.mvp.Contract.MagnetPagerContract.MagnetPagerPresenter
import me.jbusdriver.component.magnet.mvp.Contract.MagnetPagerContract.MagnetPagerView
import me.jbusdriver.component.magnet.mvp.presenter.MagnetPagerPresenterImpl

/**
 * Created by Administrator on 2017/7/17 0017.
 */
class MagnetPagersFragment : TabViewPagerFragment<MagnetPagerPresenter, MagnetPagerView>(), MagnetPagerView {

    private val keyword by lazy {
        arguments?.getString(C.BundleKey.Key_1) ?: error("must set keyword")
    }


    override fun createPresenter() = MagnetPagerPresenterImpl()

    override val mTitles: List<String> by lazy {
        val allKeys = MagnetLoaders.keys
        MagnetKeys.filter { allKeys.contains(it) }.apply {
            Schedulers.single().scheduleDirect {
                MagnetKeys.clear()
                MagnetKeys.addAll(this)
                saveMagnetKeys()
            }
        }


    }

    override val mFragments: List<Fragment> by lazy {
        mTitles.map { MagnetListFragment.newInstance(keyword, it) }
    }


}