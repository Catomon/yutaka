package io.github.catomon.yutaka.domain

data class Post(
    val id: String,
    val host: String,
    val largeUri: String,
    val smallUri: String,
    val originalUri: String,
    val title: String,
    val details: String,
    val author: String,
    val source: String,
    val score: Int,
    val rating: String,
    val tags: List<String>,
    val fileExt: String,
)