package me.jbusdriver.base.data.collect

interface ICollect<T> {
    val key: String

    fun addToCollect(data: T): Boolean
    fun has(data: T): Boolean
    fun removeCollect(data: T): Boolean

    fun update(data: T): Boolean
}
