package com.zhao.common.utils

import java.io.File
import java.net.URL

/**
 * @author zjf
 * @date 2023/9/20
 * @effect 对String的扩展
 */

/**
 * 获取Url类型字符串的后缀名，要求必须是个文件
 */
fun String?.getUrlFileName(): String {
    return try {
        val url = URL(this)
        val file = File(url.path)
        file.name
    } catch (e: Exception) {
        ""
    }
}