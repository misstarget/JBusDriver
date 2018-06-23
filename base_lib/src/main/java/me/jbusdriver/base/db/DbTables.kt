package me.jbusdriver.base.db

//region history
const val CREATE_HISTORY_SQL = "CREATE TABLE ${HistoryTable.TABLE_NAME} ( " +
        " ${HistoryTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
        " ${HistoryTable.COLUMN_DB_TYPE} TINYINT  NOT NULL ," +
        " ${HistoryTable.COLUMN_CREATE_TIME} INTEGER  DEFAULT 0 ," +
        " ${HistoryTable.COLUMN_JSON_STR} TEXT  NOT NULL ," +
        " ${HistoryTable.COLUMN_IS_ALL} TINYINT  NOT NULL " +
        ")"

object HistoryTable {
    const val TABLE_NAME = "t_history"
    const val COLUMN_ID = "id"
    const val COLUMN_DB_TYPE = "dbType"
    const val COLUMN_CREATE_TIME = "createTime"
    const val COLUMN_JSON_STR = "jsonStr"
    const val COLUMN_IS_ALL = "isAll"
}
//endregion

//region collect category
object CategoryTable {
    const val TABLE_NAME = "t_category"
    const val COLUMN_ID = "id"
    const val COLUMN_P_ID = "pid"
    const val COLUMN_NAME = "name"
    const val COLUMN_TREE = "tree"
    const val COLUMN_ORDER = "orderIndex"
}

const val CREATE_COLLECT_CATEGORY_SQL = "CREATE TABLE ${CategoryTable.TABLE_NAME} ( " +
        " ${CategoryTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
        " ${CategoryTable.COLUMN_P_ID} INTEGER  NOT NULL DEFAULT -1," +
        " ${CategoryTable.COLUMN_NAME} NVARCHAR(100) NOT NULL ," +
        " ${CategoryTable.COLUMN_TREE} TEXT NOT NULL , " +
        " ${CategoryTable.COLUMN_ORDER}  INTEGER  NOT NULL DEFAULT 0 " +
        ")"
//endregion


//region link
object LinkItemTable {
    const val TABLE_NAME = "t_link"
    const val COLUMN_ID = "id"
    const val COLUMN_CATEGORY_ID = "categoryId"
    const val COLUMN_DB_TYPE = "dbType"
    const val COLUMN_CREATE_TIME = "createTime"
    const val COLUMN_KEY = "key"
    const val COLUMN_JSON_STR = "jsonStr"

}

const val CREATE_LINK_ITEM_SQL = "CREATE TABLE ${LinkItemTable.TABLE_NAME} ( " +
        " ${LinkItemTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
        " ${LinkItemTable.COLUMN_CATEGORY_ID} INTEGER  DEFAULT -1 ," +
        " ${LinkItemTable.COLUMN_DB_TYPE} TINYINT  NOT NULL ," +
        " ${LinkItemTable.COLUMN_CREATE_TIME} INTEGER  DEFAULT 0 ," +
        " ${LinkItemTable.COLUMN_KEY} VARCHAR(100) NOT NULL UNIQUE," +
        " ${LinkItemTable.COLUMN_JSON_STR} TEXT  NOT NULL " +
        ")"
//endregion