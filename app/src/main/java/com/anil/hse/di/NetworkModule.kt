package com.anil.hse.di

import com.anil.hse.networking.AuthInterceptor
import com.anil.hse.networking.HseClient
import com.anil.hse.networking.HseService
import com.anil.hse.networking.ResponseHandler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val networkModule = module {
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideHseApi(get()) }
    factory { provideLoggingInterceptor() }
    single { provideRetrofit(get()) }
    factory { provideHseClient(get()) }
    factory { ResponseHandler() }
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl("https://www.hse24.de/").client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

private fun provideOkHttpClient(
    authInterceptor: AuthInterceptor,
    loggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor).build()
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BASIC
    return logger
}

private fun provideHseApi(retrofit: Retrofit): HseService = retrofit.create(HseService::class.java)
private fun provideHseClient(hseService: HseService): HseClient = HseClient(hseService)

