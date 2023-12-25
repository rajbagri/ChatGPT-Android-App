package com.rajbagri.chatgptclone

data class ChatRequest(
    val messages: List<Message>,
    val model: String
)