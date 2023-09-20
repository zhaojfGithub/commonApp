package com.zhao.common.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings


/**
 * @author zjf
 * @date 2023/9/15
 * @effect 设备信息工具类
 */
object EquipmentUtil {

    /**
     * 获取android 版本号
     */
    fun getAndroidVersion() = Build.VERSION.RELEASE

    /**
     * 获取android的版本名称
     */
    fun getAndroidVersionName() = Build.VERSION.CODENAME

    /**
     * 获取Api的级别
     */
    fun getApiLevel() = Build.VERSION.SDK_INT

    /**
     * 获取androidId 可当作唯一标识
     */
    fun getAndroidID(context: Context) = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    /**
     * 检查网络连接
     */
    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        //wifi 蜂窝 有线
        return networkCapabilities != null && (
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                )
    }

    /**
     * 判断是否为Wifi网络
     */
    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    }

    /**
     * 获取电池状态   0放电  1充电
     */
    fun getBatterState(context: Context): Int {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batterStatus = context.registerReceiver(null, ifilter)
        val status = batterStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL
        return if (isCharging) 1 else 0
    }

    /**
     * 剩余电量
     */
    fun getBatteryLevel(context: Context): Int {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, ifilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        // 计算电池剩余电量的百分比
        val batteryPct = level.toFloat() / scale.toFloat() * 100
        return batteryPct.toInt()
    }

    /**
     * 获取当前屏幕亮度
     */
    fun getScreenBrightness(context: Context): Int {
        return try {
            val contentResolver = context.contentResolver
            Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    /**
     * 修改屏幕亮度 亮度为 0-255
     * 应当具备[Manifest.permission.WRITE_SETTINGS]权限
     */
    fun setScreenBrightness(context: Activity, brightness: Int) {
        try {
            var validBrightness = brightness
            if (brightness < 0) validBrightness = 0
            if (brightness > 255) validBrightness = 255
            val contentResolver = context.contentResolver
            Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, validBrightness)
            val layoutParams = context.window.attributes
            layoutParams.screenBrightness = validBrightness / 255.0f
            context.window.attributes = layoutParams
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取当前媒体音量
     */
    fun getMediaVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    /**
     * 设置媒体音量
     * 应当具备[Manifest.permission.MODIFY_AUDIO_SETTINGS]权限
     */
    fun setMediaVolume(context: Context, volume: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND)
    }

    /**
     * 获取当前通知音量
     */
    fun getNotificationVolume(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
    }

    /**
     * 设置通知音量
     * 应当具备[Manifest.permission.MODIFY_AUDIO_SETTINGS]权限
     */
    fun setNotificationVolume(context: Context, volume: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, AudioManager.FLAG_PLAY_SOUND)
    }
}