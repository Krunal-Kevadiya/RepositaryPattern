package com.example.ownrepositarypatternsample.base

class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null,
    val onLastPage: Boolean = false
) {
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
        return "Resource(status=$status, data=$data, message=$message, onLastPage=$onLastPage)"
    }

    companion object {
        fun <T> success(data: T?, onLastPage: Boolean): Resource<T> {
            return Resource(status = Status.SUCCESS, data =  data, onLastPage = onLastPage)
        }

        fun <T> error(msg: String?, data: T?, onLastPage: Boolean): Resource<T> {
            return Resource(status = Status.ERROR, data = data, message = msg, onLastPage = onLastPage)
        }

        fun <T> loading(): Resource<T> {
            return Resource(status = Status.LOADING)
        }
    }
}
