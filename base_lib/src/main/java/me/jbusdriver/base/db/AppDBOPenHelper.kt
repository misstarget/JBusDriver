package me.jbusdriver.base.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import me.jbusdriver.base.KLog
import me.jbusdriver.base.mvp.bean.AllFirstParentDBCategoryGroup

private const val JBUS_DB_VERSION = 1

class JBusDBOpenCallBack : SupportSQLiteOpenHelper.Callback(JBUS_DB_VERSION) {

    override fun onCreate(db: SupportSQLiteDatabase?) {
        KLog.d("JBusDBOpenCallBack onCreate")
        db?.execSQL(CREATE_HISTORY_SQL)
    }

    override fun onUpgrade(db: SupportSQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        KLog.d("JBusDBOpenCallBack onUpgrade $oldVersion $newVersion")
    }
    /*for (i in oldVersion..newVersion) {
     when (i) {
         JBUS_DB_VERSION_V2 -> up2V2(db)
     }
 }*/
    /* private fun up2V2(db: SQLiteDatabase?) {
           KLog.d("JBusDBOpenCallBack up2V2 $db")
           db?.execSQL(CREATE_LINK_ITEM_SQL)
           db?.execSQL(CREATE_COLLECT_CATEGORY_SQL)
       }*/


}


private const val COLLECT_DB_VERSION = 1

class CollectDBCallBack : SupportSQLiteOpenHelper.Callback(COLLECT_DB_VERSION) {

    override fun onCreate(db: SupportSQLiteDatabase?) {
        KLog.d("JBusDBOpenCallBack onCreate")
        db?.execSQL(CREATE_LINK_ITEM_SQL)
        db?.execSQL(CREATE_COLLECT_CATEGORY_SQL)
        AllFirstParentDBCategoryGroup.forEach {
            db?.insert(CategoryTable.TABLE_NAME, SQLiteDatabase.CONFLICT_NONE, it.value.cv())
            db?.update(CategoryTable.TABLE_NAME, SQLiteDatabase.CONFLICT_NONE, it.value.cv(),CategoryTable.COLUMN_ID + " = ${it.value.id!!} ",null)
        }
//        AllFirstParentDBCategoryGroup.forEach {
//            try {
//                CategoryService.insert(it.value)
//                CategoryService.update(it.value)
//            } catch (e: Exception) {
//
//            }
//        }

    }


    override fun onUpgrade(db: SupportSQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        KLog.d("JBusDBOpenCallBack onUpgrade $oldVersion $newVersion")
    }

}

