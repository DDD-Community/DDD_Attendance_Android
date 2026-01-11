package com.ddd.attendance.data.api.di

import com.ddd.attendance.data.api.AuthenticationApi
import com.ddd.attendance.data.api.BuildConfig
import com.ddd.attendance.data.api.OnboardingApi
import com.ddd.attendance.data.api.UserApi
import com.ddd.attendance.data.api.datasource.ApiLoginDataSourceImpl
import com.ddd.attendance.data.api.datasource.ApiOnboardingDataSourceImpl
import com.ddd.attendance.data.datasource.ApiLoginDataSource
import com.ddd.attendance.data.datasource.ApiOnboardingDataSource
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.dddstudy.site/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthenticationApiService(retrofit: Retrofit): AuthenticationApi {
        return retrofit.create(AuthenticationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOnboardingService(retrofit: Retrofit): OnboardingApi {
        return retrofit.create(OnboardingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideApiLoginDataSource(impl: ApiLoginDataSourceImpl): ApiLoginDataSource = impl

    @Provides
    @Singleton
    fun provideApiOnboardingDataSource(impl: ApiOnboardingDataSourceImpl): ApiOnboardingDataSource = impl
}