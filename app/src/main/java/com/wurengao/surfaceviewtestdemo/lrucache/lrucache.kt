package com.wurengao.surfaceviewtestdemo.lrucache

import android.util.LruCache

/**
 * Created by wurengao on 2024/5/13
 * @author wurengao@bytedance.com
 */
class MyLruCache(maxSize: Int) : LruCache<String, String>(maxSize) {

    override fun entryRemoved(
        evicted: Boolean,
        key: String?,
        oldValue: String?,
        newValue: String?
    ) {
        super.entryRemoved(evicted, key, oldValue, newValue)
    }

    override fun sizeOf(key: String?, value: String?): Int {
        return super.sizeOf(key, value)
    }
}


fun test() {
    val cache = MyLruCache(8)

    cache.put("1", "2")
}