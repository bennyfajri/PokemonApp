package com.drsync.core.di

import android.content.Context
import androidx.room.Room
import com.drsync.core.BuildConfig
import com.drsync.core.data.local.room.PokemonDao
import com.drsync.core.data.local.room.PokemonDatabase
import com.drsync.core.data.remote.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val hostName = "pokeapi.co"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostName, "sha256/D7nVX3nnTE0NXEmTWhim6nTMVFFIJWRuXenQGaeSRIw=")
            .add(hostName, "sha256/FEzVOUp4dF3gI0ZVPRJhFbSJVXR+uQmMH65xhs1glH4=")
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()

        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit
    }

    @Provides
    @Singleton
    fun providesApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase = Room.databaseBuilder(
        context,
        PokemonDatabase::class.java, "Pokemon.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providePokemonDao(
        database: PokemonDatabase
    ): PokemonDao = database.pokemonDao()
}