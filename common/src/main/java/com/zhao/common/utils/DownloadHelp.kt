package com.zhao.common.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


/**
 * @author zjf
 * @date 2023/9/19
 * @effect 一个下载的辅助类，使用系统的DownloadManager，基于此做一下简化
 */
class DownloadHelp(
    private val context: Context,
    private val lifecycleScope: CoroutineScope,
    private val url: String,
    private val onResult: (File) -> Unit,
    private val onError: (String) -> Unit
) {

    private var downloadId: Long? = null

    private var job: Job? = null

    private var onProgress: ((Int) -> Unit)? = null

    fun start() {
        val downloadManager: DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), url.getUrlFileName())
        val downloadBytes = file.length()
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri).apply {
            setTitle("正在下载")
            setDescription("下载应用文件")
            addRequestHeader("Range", "bytes=${downloadBytes}-")
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.getUrlFileName())
            setAllowedOverMetered(true)
            setAllowedOverRoaming(true)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        }
        downloadId = downloadManager.enqueue(request)
        if (onProgress != null) {
            startProgressMonitoring(file)
        }
    }

    fun stop(){
        if (downloadId == null){
            return
        }
        val downloadManager: DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.remove(downloadId!!)
    }

    private fun startProgressMonitoring(file: File) {
        job = lifecycleScope.launch(Dispatchers.Default) {
            //查询进度
            searchProgress(file)
            delay(1000)
        }
    }

    private fun searchProgress(file: File) {
        if (downloadId == null) {
            return
        }
        val downloadManager: DownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query()
        query.setFilterById(downloadId!!)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            when (cursor.getInt(statusIndex)) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    //下载成功
                    onResult.invoke(file)
                    stopProgressMonitoring()
                }

                DownloadManager.STATUS_FAILED -> {
                    //下载失败
                    val failIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
                    val reason = cursor.getInt(failIndex)
                    onError.invoke("下载错误，错误代码为${reason}")
                    stopProgressMonitoring()
                }

                DownloadManager.STATUS_RUNNING -> {
                    //下载中
                    val progressIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                    val totalIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    val progress = cursor.getInt(progressIndex)
                    val total = cursor.getInt(totalIndex)
                    val percent = (progress.toFloat() / total * 100).toInt()
                    onProgress?.invoke(percent)
                }
            }
            cursor.close()
        }
    }

    private fun stopProgressMonitoring() {
        if (job != null) {
            job?.cancel()
            job = null
        }
    }

    fun setOnProgress(onProgress: (Int) -> Unit) {
        this.onProgress = onProgress
    }

}