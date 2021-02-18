package com.androiddevs.mvvmnewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.NewsApplication
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

class NewsViewModel @ViewModelInject constructor(
    app: Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    private val app = getApplication<NewsApplication>()

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    val businessNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val entertainmentNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val healthNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val scienceNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val sportNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val techNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    init {
        getBreakingNews("ru")
    }

    suspend fun getNewsByCategory(category: String) = newsRepository.getNewsByCategory(category)

    fun getAllCategories() = viewModelScope.launch {
        businessNews.postValue(handleNewsResponse(getNewsByCategory("business")))
        entertainmentNews.postValue(handleNewsResponse(getNewsByCategory("entertainment")))
        healthNews.postValue(handleNewsResponse(getNewsByCategory("health")))
        scienceNews.postValue(handleNewsResponse(getNewsByCategory("science")))
        sportNews.postValue(handleNewsResponse(getNewsByCategory("sports")))
        techNews.postValue(handleNewsResponse(getNewsByCategory("technology")))
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
            Timber.d(response.errorBody().toString())
        }
        return Resource.Error(response.message());
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeCall(action: (suspend () -> Resource<NewsResponse>)): Resource<NewsResponse> {
        return try {
            if (hasInternetConnection()) {
                action()
            } else {
                Resource.Error(app.getString(R.string.error_no_internet_connection))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> Resource.Error(app.getString(R.string.error_network_failure))
                else -> Resource.Error(app.getString(R.string.error_conversion_failure))
            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        val resource = safeCall {
            val response = newsRepository.searchNews(searchQuery)
            handleNewsResponse(response)
        }
        searchNews.postValue(resource)
    }


    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        val resource = safeCall {
            val response = newsRepository.getBreakingNews(countryCode)
            handleNewsResponse(response)
        }
        breakingNews.postValue(resource)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = app.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}