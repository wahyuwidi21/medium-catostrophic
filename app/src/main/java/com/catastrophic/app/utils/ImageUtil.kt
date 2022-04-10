package com.catastrophic.app.utils

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.os.Build
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.URL
import java.net.URLConnection


fun CoroutineScope.getByteImageFromURL(url: String?): Deferred<ByteArray?> = async {
    val bmp = loadBitmap(url).await()//w w w.  ja v a 2s.c o m
    if (bmp != null) {
        val stream = ByteArrayOutputStream()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            bmp.compress(Bitmap.CompressFormat.WEBP_LOSSY, 10, stream)
        } else {
            bmp.compress(Bitmap.CompressFormat.WEBP, 10, stream)
        }
        bmp.recycle()
        return@async stream.toByteArray()
    }
   null
}

fun CoroutineScope.loadBitmap(url: String?): Deferred<Bitmap?> = async {
    var bm: Bitmap? = null
    var `is`: InputStream? = null
    var bis: BufferedInputStream? = null
    try {
        val conn: URLConnection = URL(url).openConnection()
        conn.connect()
        `is` = conn.getInputStream()
        bis = BufferedInputStream(`is`, 8192)
        bm = BitmapFactory.decodeStream(bis)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (bis != null) {
            try {
                bis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (`is` != null) {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
     bm
}
