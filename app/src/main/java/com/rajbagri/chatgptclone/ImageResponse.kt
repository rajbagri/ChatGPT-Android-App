package com.rajbagri.dall_eclone

import com.rajbagri.chatgptclone.Data

data class ImageResponse(
    val created: Int,
    val `data`: List<Data>
)