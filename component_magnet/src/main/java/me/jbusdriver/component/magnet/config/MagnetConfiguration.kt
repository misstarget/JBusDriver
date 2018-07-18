package me.jbusdriver.component.magnet.config

import me.jbusdriver.base.GSON
import me.jbusdriver.base.data.AppConfiguration
import me.jbusdriver.base.fromJson
import me.jbusdriver.base.toJsonString
import me.jbusdriver.component.magnet.loader.MagnetLoaders

//region magnet
private const val MagnetSourceS: String = "MagnetSourceS"
val MagnetKeys: MutableList<String> by lazy {
    GSON.fromJson<MutableList<String>>(AppConfiguration.getSp(MagnetSourceS)
            ?: "")?.takeIf { it.size > 0 } ?: run {
        //todo
        val default = MagnetLoaders.keys.take(2)
        AppConfiguration.saveSp(MagnetSourceS, default.toJsonString())
        default.toMutableList()
    }
}

fun saveMagnetKeys() = AppConfiguration.saveSp(MagnetSourceS, MagnetKeys.toJsonString())
//endregion