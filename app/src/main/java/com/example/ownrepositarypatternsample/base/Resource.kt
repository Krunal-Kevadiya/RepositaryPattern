package com.example.ownrepositarypatternsample.base

import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class Resource<out T>(val status: Status, val data: T?, val message: String?, val onLastPage: Boolean) {
    var errorEnvelope: ErrorEnvelope? = null

    init {
        message?.let {
            errorEnvelope = try {
                Gson().fromJson(message, ErrorEnvelope::class.java) as ErrorEnvelope
            } catch (e: JsonSyntaxException) {
                ErrorEnvelope(400, message, false)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val resource = other as Resource<*>

        if (status !== resource.status) {
            return false
        }
        if (if (message != null) message != resource.message else resource.message != null) {
            return false
        }
        return if (data != null) data == resource.data else resource.data == null
    }

    override fun toString(): String {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }

    companion object {
        fun <T> success(data: T?, onLastPage: Boolean): Resource<T> {
            return Resource(status = Status.SUCCESS, data =  data, message =  null, onLastPage = false)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(status = Status.ERROR, data = data, message = msg, onLastPage = true)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(status = Status.LOADING, data = data, message = null, onLastPage = false)
        }
    }
}
