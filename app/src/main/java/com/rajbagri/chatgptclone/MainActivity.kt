package com.rajbagri.chatgptclone

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.rajbagri.chatgptclone.databinding.ActivityMainBinding
import com.rajbagri.chatgptclone.ChatTranslation

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

                window.apply {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    statusBarColor = Color.TRANSPARENT
                }

                animation.playAnimation()

                cardView1.setOnClickListener { chatClickListener() }
                cardView2.setOnClickListener { imageGenerationClickListener() }
                cardView3.setOnClickListener { chatTranslationClickListener()}

        }
    }

    private fun chatTranslationClickListener() {
        val intent = Intent(this, ChatTranslation::class.java)
        startActivity(intent)
    }

    private fun imageGenerationClickListener() {
        val intent = Intent(this, ImageGeneration::class.java)
        startActivity(intent)

    }

    private fun chatClickListener() {
        val intent = Intent(this, Chat::class.java)
        startActivity(intent)
    }
}