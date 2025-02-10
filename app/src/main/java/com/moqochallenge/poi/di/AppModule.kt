package com.moqochallenge.poi.di


import android.content.Context
import androidx.room.Room
import com.moqochallenge.poi.data.local.POIDao
import com.moqochallenge.poi.data.local.POIDatabase
import com.moqochallenge.poi.data.remote.POIApi
import com.moqochallenge.poi.data.repository.POIRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://prerelease.moqo.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePOIApi(retrofit: Retrofit): POIApi {
        return retrofit.create(POIApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): POIDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            POIDatabase::class.java,
            "poi_database1"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providePOIDao(database: POIDatabase): POIDao {
        return database.poiDao()
    }

    @Provides
    @Singleton
    fun providePOIRepository(api: POIApi, dao: POIDao): POIRepository {
        return POIRepository(api, dao)
    }
}