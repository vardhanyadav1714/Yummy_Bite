package com.yummybitekiet.foodapp.di

import com.yummybitekiet.foodapp.network.FirebaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    @Singleton
//    @Provides
//    fun provideFirebaseManager(): FirebaseManager {
//        return FirebaseManager(/* Add any dependencies if needed */)
//    }
}
