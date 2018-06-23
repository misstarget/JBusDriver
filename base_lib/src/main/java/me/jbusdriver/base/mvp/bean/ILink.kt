package me.jbusdriver.base.mvp.bean

import java.io.Serializable

interface ILink : ICollectCategory, Serializable {
    val link: String
}