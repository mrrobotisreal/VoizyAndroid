package io.winapps.voizy.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.winapps.voizy.data.local.PostsCache
import io.winapps.voizy.data.local.SecureStorage
import io.winapps.voizy.data.repository.AuthRepository
import io.winapps.voizy.data.repository.PostsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSecureStorage(@ApplicationContext context: Context): SecureStorage {
        return SecureStorage(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(secureStorage: SecureStorage): AuthRepository {
        return AuthRepository(secureStorage)
    }

    @Provides
    @Singleton
    fun providePostsRepository(): PostsRepository {
        return PostsRepository()
    }

    @Provides
    @Singleton
    fun providePostsCache(): PostsCache {
        return PostsCache()
    }
}