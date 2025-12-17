package com.ddd.attendance.data.google.di

import com.ddd.attendance.data.datasource.GoogleLoginDataSource
import com.ddd.attendance.data.google.datasource.GoogleLoginDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class GoogleModule {
    
    @Binds
    abstract fun bindGoogleLoginDataSource(
        impl: GoogleLoginDataSourceImpl
    ): GoogleLoginDataSource
}