package com.ddd.attendance.data.di

import com.ddd.attendance.data.repository.AdminMyPageRepositoryImpl
import com.ddd.attendance.data.repository.OnboardingRepositoryImpl
import com.ddd.attendance.data.repository.UserRepositoryImpl
import com.ddd.attendance.domain.repository.AdminMyPageRepository
import com.ddd.attendance.domain.repository.OnboardingRepository
import com.ddd.attendance.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindOnboardingRepository(onboardingRepositoryImpl: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    @Singleton
    abstract fun bindAdminMyPageRepository(adminMyPageRepositoryImpl: AdminMyPageRepositoryImpl): AdminMyPageRepository
}