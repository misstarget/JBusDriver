package me.jbusdriver.base.http

import android.support.v4.util.ArrayMap
import io.reactivex.Flowable
import me.jbusdriver.base.*
import me.jbusdriver.base.common.C
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

/**
 * Created by Administrator on 2017/4/8.
 */
interface JAVBusService {


    //https://announce.javbus8.com/website.php
    @GET
    fun get(@Url url: String, @Header("existmag") existmag: String? = null): Flowable<String?>


    companion object {
        val JBusServices by lazy { arrayMapof<String, JAVBusService>() }

        var defaultFastUrl = "https://www.javbus2.pw"
        var INSTANCE = getInstance(defaultFastUrl)
        fun getInstance(source: String): JAVBusService {
            KLog.d("instances : $JBusServices, defaultFastUrl : $defaultFastUrl")
            //JBusServices[type] 会出异常
            return JBusServices.getOrPut(source) {
                createService(source)
            }
        }

        private fun createService(url: String) = NetClient.getRetrofit(if (!url.endsWith("/")) "$url/" else url).create(JAVBusService::class.java)

        /**
         *key: "xyz" else "default"
         */
        val defaultImageUrlHosts by lazy {
            CacheLoader.acache.getAsString(C.Cache.IMG_HOSTS)?.let {
                GSON.fromJson<ArrayMap<String, MutableSet<String>>>(it)
            } ?: arrayMapof()
        }
    }
}