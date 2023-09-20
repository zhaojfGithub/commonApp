package com.zhao.common.utils

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * @author zjf
 * @date 2023/9/15
 * @effect 适用于普通通知，扩展通知，更新通知
 */
object NotificationUtil {

    /**
     * 发送一个通知
     * [context] 上下文
     * [channelId] 渠道id
     * [icon] 图标
     * [title] 标题
     * [content] 内容
     * [priority] 优先级
     * [intent] 点击事件
     * [notificationId] 通知id 用户更新 取消
     */
    fun postNotification(
        context: Context, channelId: String, icon: Int, title: String, content: String,
        priority: Int = NotificationCompat.PRIORITY_DEFAULT, intent: Intent,
        notificationId: Int
    ) {
        //开始一个新的栈
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
            return
        }
        notificationManager.notify(notificationId, builder.build())
    }

}