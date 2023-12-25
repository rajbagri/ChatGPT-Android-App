package com.rajbagri.dall_eclone

data class ImageRequest(
    val model: String,
    val prompt: String,
    val n: Int,
    val size: String
)