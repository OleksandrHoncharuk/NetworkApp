package com.example.networkaplication.details


import android.graphics.Bitmap

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils

import java.security.MessageDigest

class CropSquareTransformation : BitmapTransformation() {

    private var size: Int = 0

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        this.size = Math.max(outWidth, outHeight)
        return TransformationUtils.centerCrop(pool, toTransform, size, size)
    }

    override fun toString(): String {
        return "CropSquareTransformation(size=$size)"
    }

    override fun equals(o: Any?): Boolean {
        return o is CropSquareTransformation && o.size == size
    }

    override fun hashCode(): Int {
        return ID.hashCode() + size * 10
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + size).toByteArray(CHARSET))
    }

    companion object {
        private val VERSION = 1
        private val ID = "com.example.networkaplication.details.CropSquareTransformation.$VERSION"
    }
}
