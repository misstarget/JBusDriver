package me.jbusdriver.common

import com.billy.cc.core.component.CC
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.tencent.tinker.loader.app.TinkerApplication
import com.tencent.tinker.loader.shareutil.ShareConstants
import com.umeng.analytics.MobclickAgent
import io.reactivex.plugins.RxJavaPlugins
import jbusdriver.me.jbusdriver.BuildConfig
import me.jbusdriver.base.JBusManager
import me.jbusdriver.debug.stetho.initializeStetho

lateinit var JBus: AppContext

class AppContext : TinkerApplication(ShareConstants.TINKER_ENABLE_ALL, "me.jbusdriver.common.JBusApplicationLike",
        "com.tencent.tinker.loader.TinkerLoader", false) {

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }

        if (BuildConfig.DEBUG) {
            LeakCanary.install(this)

            val formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
                    .methodCount(2)         // (Optional) How many method line to show. Default 2
                    .methodOffset(0)        // (Optional) Hides internal method calls up to offset. Default 5
                    // .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                    .tag("old_driver")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                    .build()

            Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
                override fun isLoggable(priority: Int, tag: String?) = BuildConfig.DEBUG
            })

            initializeStetho(this) //chrome://inspect/#devices
        }

        MobclickAgent.setDebugMode(BuildConfig.DEBUG)

        CC.enableDebug(BuildConfig.DEBUG)  //普通调试日志，会提示一些错误信息
        CC.enableVerboseLog(BuildConfig.DEBUG)  //组件调用的详细过程日志，用于跟踪整个调用过程
        CC.enableRemoteCC(BuildConfig.DEBUG)

        RxJavaPlugins.setErrorHandler {
            try {
                if (!BuildConfig.DEBUG) MobclickAgent.reportError(this, it)
            } catch (e: Exception) {
                //ignore  report error
            }
        }

        JBus = this

        this.registerActivityLifecycleCallbacks(JBusManager)
    }


}