package me.jbusdriver.component.recommend

import android.app.Application
import com.billy.cc.core.component.CC
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import me.jbusdriver.base.JBusManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CC.enableDebug(BuildConfig.DEBUG)  //普通调试日志，会提示一些错误信息
        CC.enableVerboseLog(BuildConfig.DEBUG)  //组件调用的详细过程日志，用于跟踪整个调用过程
        CC.enableRemoteCC(BuildConfig.DEBUG)  //组件调用的详细过程日志，用于跟踪整个调用过程


        if (BuildConfig.DEBUG) {

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

        }
        this.registerActivityLifecycleCallbacks(JBusManager)
        JBusManager.setContext(this)

    }
}