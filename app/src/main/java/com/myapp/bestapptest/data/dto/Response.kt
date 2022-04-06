package com.myapp.bestapptest.data.dto

import com.myapp.bestapptest.domain.model.Article

data class Response(
    val articles: List<Article>,
)