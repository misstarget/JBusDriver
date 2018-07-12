package me.jbusdriver.db

import android.annotation.SuppressLint
import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import com.squareup.sqlbrite3.SqlBrite
import io.reactivex.schedulers.Schedulers
import me.jbusdriver.base.BuildConfig
import me.jbusdriver.base.JBusManager
import me.jbusdriver.base.KLog
import me.jbusdriver.base.db.SDCardDatabaseContext
import me.jbusdriver.base.db.dao.CategoryDao
import me.jbusdriver.base.db.dao.HistoryDao
import me.jbusdriver.base.db.dao.LinkItemDao
import java.io.File


@SuppressLint("CheckResult")
object DB {
    private val provideSqlBrite: SqlBrite by lazy {
        SqlBrite.Builder().apply {
            if (BuildConfig.DEBUG) {
                this.logger { message -> KLog.t("DataBase").i(message) }
            }
        }.build()
    }
    private const val JBUS_DB_NAME = "jbusdriver.db"
    private val dataBase by lazy {
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(JBusManager.context)
                .name(JBUS_DB_NAME).callback(JBusDBOpenCallBack()).build()
        provideSqlBrite.wrapDatabaseHelper(FrameworkSQLiteOpenHelperFactory().create(configuration), Schedulers.io()).apply {
            setLoggingEnabled(BuildConfig.DEBUG)
        }
    }

    private const val COLLECT_DB_NAME = "collect.db"
    val collectDataBase by lazy {
        val configuration = SupportSQLiteOpenHelper.Configuration.builder(object : SDCardDatabaseContext(JBusManager.context) {
            override val dir: String = JBusManager.context .packageName + File.separator + "collect"
        }).name(COLLECT_DB_NAME).callback(CollectDBCallBack()).build()
        provideSqlBrite.wrapDatabaseHelper(FrameworkSQLiteOpenHelperFactory().create(configuration), Schedulers.io()).apply {
            setLoggingEnabled(BuildConfig.DEBUG)
        }
    }


    val historyDao by lazy { HistoryDao(dataBase) }
    val categoryDao by lazy { CategoryDao(collectDataBase) }
    val linkDao by lazy { LinkItemDao(collectDataBase) }
}