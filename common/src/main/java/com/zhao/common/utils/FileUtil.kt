package com.zhao.common.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * @author zjf
 * @date 2023/9/15
 * @effect  文件工具类
 */
object FileUtil {

    /**
     * 检查文件或目录是否存在
     */
    fun exists(file: File): Boolean {
        return file.exists()
    }

    /**
     * 创建文件或者文件夹
     */
    fun createFile(file: File) {
        if (file.exists()) {
            return
        }
        if (file.isDirectory) {
            file.mkdirs()
        }
        if (file.isFile) {
            file.createNewFile()
        }
    }

    /**
     * 删除文件或者文件夹
     */
    fun deleteFile(file: File): Boolean {
        if (file.exists()) {
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    deleteFile(it)
                }
            }
            return file.delete()
        }
        return true
    }

    /**
     * 复制文件
     * [sourceFile] 源文件
     * [destinationFile] 目标文件
     */
    fun copyFile(sourceFile: File, destinationFile: File): Boolean {
        try {
            if (!sourceFile.exists()) {
                throw IOException("file is null?")
            }
            if (destinationFile.exists()) {
                destinationFile.createNewFile()
            }
            FileInputStream(sourceFile).use { input ->
                FileOutputStream(destinationFile).use { output ->
                    val buffer = ByteArray(4 * 1024)
                    var byteRead: Int
                    while (input.read(buffer).also { byteRead = it } != -1) {
                        output.write(buffer, 0, byteRead)
                    }
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    /**
     * 移动文件
     * [sourceFile] 源文件
     * [destinationFile] 目标文件
     */
    fun moveFile(sourceFile: File, destinationFile: File): Boolean {
        return if (sourceFile.renameTo(destinationFile)) {
            true
        } else copyFile(sourceFile, destinationFile)
    }


    /**
     * 压缩文件或者文件夹
     */
    fun zipFileOrFolder(sourceFile: File, zipFile: File): Boolean {
        return try {
            val fos = FileOutputStream(zipFile)
            val zipOut = ZipOutputStream(fos)
            if (sourceFile.isDirectory) {
                zipFolder(sourceFile, sourceFile.name, zipOut)
            } else {
                zipFile(sourceFile, zipOut)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 压缩文件
     */
    private fun zipFile(fileToZip: File, zipOut: ZipOutputStream) {
        val fis = FileInputStream(fileToZip)
        val zipEntry = ZipEntry(fileToZip.name)
        zipOut.putNextEntry(zipEntry)
        val bytes = ByteArray(1024)
        var length: Int
        while (fis.read(bytes).also { length = it } >= 0) {
            zipOut.write(bytes, 0, length)
        }
        fis.close()
    }

    /**
     * 压缩文件夹
     */
    private fun zipFolder(fileToZip: File, parentFolder: String, zipOut: ZipOutputStream) {
        fileToZip.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                zipFolder(file, "$parentFolder/${file.name}", zipOut)
            } else {
                val fis = FileInputStream(file)
                val zipEntry = ZipEntry("$parentFolder/${file.name}")
                zipOut.putNextEntry(zipEntry)
                val bytes = ByteArray(1024)
                var length: Int
                while (fis.read(bytes).also { length = it } >= 0) {
                    zipOut.write(bytes, 0, length)
                }
                fis.close()
            }
        }
    }

    /**
     * 解压文件或者文件夹
     */
    private fun unZip(zipFilePath: File, targetFolderPath: File): Boolean {
        if (!zipFilePath.exists() || !zipFilePath.isFile || !targetFolderPath.exists() || !targetFolderPath.isDirectory) {
            return false
        }
        try {
            val zipIn = ZipInputStream(FileInputStream(zipFilePath))
            var entry = zipIn.nextEntry
            while (entry != null) {
                val filePath = File(targetFolderPath, entry.name)
                if (entry.isDirectory) {
                    filePath.mkdirs()
                } else {
                    val parentDir = filePath.parentFile
                    parentDir?.mkdirs()
                    val buffer = ByteArray(1024)
                    val fos = FileOutputStream(filePath)
                    var len: Int
                    while (zipIn.read(buffer).also { len = it } > 0) {
                        fos.write(buffer, 0, len)
                    }
                    fos.close()
                }
                zipIn.closeEntry()
                entry = zipIn.nextEntry
            }
            zipIn.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


}