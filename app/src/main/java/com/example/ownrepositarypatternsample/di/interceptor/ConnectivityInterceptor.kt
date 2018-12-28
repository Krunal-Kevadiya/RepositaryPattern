package com.example.ownrepositarypatternsample.di.interceptor

import android.content.Context
import com.example.ownrepositarypatternsample.utils.extension.hasConnection
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!context.hasConnection) {
            throw IOException("No internet available")
        }
        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}
