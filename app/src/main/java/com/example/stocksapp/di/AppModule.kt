package com.example.stocksapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.stocksapp.data.local.LocalStockDatabase
import com.example.stocksapp.data.local.RemoteKeysDao
import com.example.stocksapp.data.local.StockDao
import com.example.stocksapp.data.remote.Properties.BASE_URL
import com.example.stocksapp.data.remote.Properties.CACHE_SIZE
import com.example.stocksapp.data.remote.Properties.MAX_AGE
import com.example.stocksapp.data.remote.Properties.MAX_STALE
import com.example.stocksapp.data.remote.RemoteService
import com.example.stocksapp.data.remote.hasNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
////        val logging = HttpLoggingInterceptor()
////        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//        return OkHttpClient.Builder()
//            .build()
//    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideStockDao(database: LocalStockDatabase): StockDao = database.stockDao()

    @Provides
    @Singleton
    fun provideRemoteKeysDao(database: LocalStockDatabase): RemoteKeysDao =
        database.remoteKeyDao()

    @Provides
    @Singleton
    fun provideRetrofitApiInterface(retrofit: Retrofit): RemoteService {
        return retrofit.create(RemoteService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {

        val cacheSize = CACHE_SIZE
        val myCache = Cache(context.cacheDir, cacheSize)

        return OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (hasNetwork(context)) {
                    request
                        .newBuilder()
                        .cacheControl(
                            CacheControl.Builder()
                                .maxAge(MAX_AGE, TimeUnit.MINUTES)
                                .build()
                        )
                        .build()
                } else {
                    request
                        .newBuilder()
                        .cacheControl(
                            CacheControl.Builder()
                                .maxStale(MAX_STALE, TimeUnit.DAYS)
                                .build()
                        )
                        .build()
                }
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideMyDataBase(app: Application): LocalStockDatabase {
        return Room.databaseBuilder(
            app,
            LocalStockDatabase::class.java,
            "LocalStockDatabase"
        )
//            .addMigrations() later add migrations if u change the table
            .build()
    }
}