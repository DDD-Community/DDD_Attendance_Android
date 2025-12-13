package com.ddd.attendance.data.google.di

import com.ddd.attendance.data.datasource.LoginDataSource
import com.ddd.attendance.data.google.datasource.GoogleLoginDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleModule {
    
    @Provides
    @Singleton
    fun provideGoogleLoginDataSource(
        impl: GoogleLoginDataSourceImpl
    ): LoginDataSource = impl
}