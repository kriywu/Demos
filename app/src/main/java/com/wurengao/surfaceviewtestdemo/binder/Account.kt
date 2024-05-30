package com.wurengao.surfaceviewtestdemo.binder

import org.json.JSONObject

/**
 * Created by wurengao on 2024/5/31
 * @author wurengao@bytedance.com
 */
data class Account(val uid: String, val pwd: String) {

    override fun toString(): String {
        return JSONObject().apply {
            put("uid", uid)
            put("pwd",pwd)
        }.toString()
    }

    companion object {
        fun fromJSONString(str: String): Account {
            return JSONObject(str).run {
                Account(getString("uid"), getString("pwd"))
            }

        }
    }
}