package com.androiddevs.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_news.*

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        // API 40651216c90047d8bdb0f6d0eb80ac2a

//        val newsRepository = NewsRepository(ArticleDatabase(this))
//        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
//        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}
