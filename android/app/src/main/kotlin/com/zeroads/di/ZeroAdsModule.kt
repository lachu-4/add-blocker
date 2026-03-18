package com.zeroads.di

import android.content.Context
import androidx.room.Room
import com.zeroads.data.db.ZeroAdsDao
import com.zeroads.data.db.ZeroAdsDatabase
import com.zeroads.data.heuristic.HeuristicEngine
import com.zeroads.data.manager.AppControlManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module for ZeroAds.
 * Provides database, DAO, and manager instances.
 */
@Module
@InstallIn(SingletonComponent::class)
object ZeroAdsModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ZeroAdsDatabase {
        return Room.databaseBuilder(
            context,
            ZeroAdsDatabase::class.java,
            "zeroads_db"
        ).build()
    }

    @Provides
    fun provideDao(database: ZeroAdsDatabase): ZeroAdsDao {
        return database.zeroAdsDao()
    }

    @Provides
    @Singleton
    fun provideAppControlManager(@ApplicationContext context: Context): AppControlManager {
        return AppControlManager(context)
    }

    @Provides
    @Singleton
    fun provideHeuristicEngine(): HeuristicEngine {
        return HeuristicEngine()
    }
}
