package com.example.bharatcheck

import android.R.attr.category
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bharatcheck.Constant.apiKey
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.NewsApiClient.ArticlesResponseCallback
import com.kwabenaberko.newsapilib.models.Article
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import kotlinx.coroutines.launch

class NewsScreenViewModel : ViewModel() {
    private val _articles = MutableLiveData<List<Article>>()
    val articles: MutableLiveData<List<Article>> = _articles

    private val newsApiClient = NewsApiClient(apiKey)

    init {
        getNews("general")
    }

    fun getNews(category: String) {
        val request = TopHeadlinesRequest.Builder()
            .language("en")
            .category(category)
            .build()

        newsApiClient.getTopHeadlines(request, object : ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                response?.articles?.let {
                    _articles.postValue(it)
                }
            }

            override fun onFailure(throwable: Throwable?) {
                throwable?.printStackTrace()
            }
        })
    }

    fun searchNewsByText(query: String, onSummaryReady: (String) -> Unit) {
        val request = TopHeadlinesRequest.Builder()
            .q(query)
            .language("en")
            .build()

        newsApiClient.getTopHeadlines(request, object : ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                val summary = response?.articles?.take(3)?.joinToString("\n\n") { article ->
                    "- Title: ${article.title}\nSource: ${article.source.name}\nPublished: ${article.publishedAt}"
                } ?: "No matching headlines found."

                onSummaryReady(summary)
            }

            override fun onFailure(throwable: Throwable?) {
                onSummaryReady("No results due to error: ${throwable?.localizedMessage}")
            }
        })
    }
}
