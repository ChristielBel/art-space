package com.example.artspace.ui.util

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
fun loadScaledImage(@DrawableRes resId: Int, maxWidth: Int, maxHeight: Int): ImageBitmap {
    val context = LocalContext.current
    return remember(resId) {
        if (android.os.Build.VERSION.SDK_INT >= 28) {
            val source = ImageDecoder.createSource(context.resources, resId)
            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                val scale = minOf(
                    maxWidth.toFloat() / info.size.width,
                    maxHeight.toFloat() / info.size.height,
                    1f
                )
                val targetWidth = (info.size.width * scale).toInt()
                val targetHeight = (info.size.height * scale).toInt()
                decoder.setTargetSize(targetWidth, targetHeight)
            }
            bitmap.asImageBitmap()
        } else {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeResource(context.resources, resId, options)

            val (width, height) = options.outWidth to options.outHeight
            var inSampleSize = 1
            if (height > maxHeight || width > maxWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2
                while ((halfHeight / inSampleSize) >= maxHeight && (halfWidth / inSampleSize) >= maxWidth) {
                    inSampleSize *= 2
                }
            }

            val scaledOptions = BitmapFactory.Options().apply { this.inSampleSize = inSampleSize }
            val bitmap = BitmapFactory.decodeResource(context.resources, resId, scaledOptions)
            bitmap.asImageBitmap()
        }
    }
}