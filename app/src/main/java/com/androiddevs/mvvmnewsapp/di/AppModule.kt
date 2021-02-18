package com.androiddevs.mvvmnewsapp.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.mvvmnewsapp.adapters.CategoryViewPagerAdapter
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.api.NewsApi
import com.androiddevs.mvvmnewsapp.db.ArticleDao
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.util.Constants
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideArticleDatabase(
        @ApplicationContext context: Context
    ) = Room
        .databaseBuilder(context, ArticleDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()


    @Singleton
    @Provides
    fun provideArticleDao(db: ArticleDatabase) = db.getArticleDao()

    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(
    ) = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideApi(
        retrofit: Retrofit
    ) = retrofit.create(NewsApi::class.java)

    @Singleton
    @Provides
    fun provideNewsAdapter() = NewsAdapter()

    @Singleton
    @Provides
    fun provideCategoryViewPagerAdapter(
        newsAdapter: NewsAdapter
    ) = CategoryViewPagerAdapter(newsAdapter)
}