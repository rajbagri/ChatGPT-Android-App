package com.rajbagri.chatgptclone

data class Choice(
    val finish_reason: String,
    val index: Int,
    val message: Message
)