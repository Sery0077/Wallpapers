package sery.vlasenko.wallpapers.di

import android.content.Context
import android.content.pm.PackageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import sery.vlasenko.wallpapers.App
import sery.vlasenko.wallpapers.BuildConfig
import sery.vlasenko.wallpapers.model.repository.UnsplashService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideBaseUrl(): String = BuildConfig.USPLASH_API

    @Provides
    fun provideUnsplashApiKey(context: Context): String = context.packageManager
        .getApplicationInfo(
            context.packageName,
            PackageManager.GET_META_DATA
        ).metaData["unsplashApiKey"].toString()

    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request()

        val authUrl = request
            .url()
            .newBuilder()
            .addQueryParameter("client_id", provideUnsplashApiKey(App.applicationContext()))
            .build()

        val authRequest = request.newBuilder().url(authUrl).build()
        chain.proceed(authRequest)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(provideAuthInterceptor())
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(provideBaseUrl())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideUnsplashService(retrofit: Retrofit): UnsplashService =
        retrofit.create(UnsplashService::class.java)
}