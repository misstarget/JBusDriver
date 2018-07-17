package me.jbusdriver.base.mvp

import com.billy.cc.core.component.IParamJsonConverter
import me.jbusdriver.base.GSON
import me.jbusdriver.base.JBusManager

class GsonParamConverter : IParamJsonConverter {
    override fun <T : Any?> json2Object(json: String?, clazz: Class<T>?): T {
        return GSON.fromJson(json, clazz)
    }

    override fun object2Json(instance: Any?): String {
        return GSON.toJson(instance)
    }
}