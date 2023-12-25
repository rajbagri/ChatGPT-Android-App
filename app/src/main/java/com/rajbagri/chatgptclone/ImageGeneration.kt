package com.rajbagri.chatgptclone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AbsListView.RecyclerListener
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rajbagri.chatgptclone.databinding.ActivityImageGenerationBinding
import com.rajbagri.dall_eclone.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ImageGeneration : AppCompatActivity() {
    private lateinit var binding: ActivityImageGenerationBinding
    private var chatData: ArrayList<ChatData> = ArrayList()
    private lateinit var adapter: ImageAdapter
    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageGenerationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#212121");

        binding.sendButton.setOnClickListener {
            if(!binding.chatBox.text!!.isEmpty()){
                sentButtonClickListner()
            }
            else{
                Toast.makeText(this, "UserChat cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun sentButtonClickListner() {
        val userChat = binding.chatBox.text.toString()
        binding.chatBox.text!!.clear()

        chatData.add(ChatData(userChat, "user"))
        adapter = ImageAdapter(this, chatData)
        mLayoutManager = LinearLayoutManager(this@ImageGeneration)
        mLayoutManager.stackFromEnd = true
        binding.chatRecyclerView.layoutManager = mLayoutManager
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.recycledViewPool.clear()

        binding.chatRecyclerView.smoothScrollToPosition(chatData.size - 1);

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // Adjust the timeout duration as needed
                    .readTimeout(30, TimeUnit.SECONDS) // Adjust the timeout duration as needed
                    .build()
            )
            .build()
            .create(ApiInterface::class.java)

        val messages = arrayListOf(
            Message("You are a helpful assistant.", "system"),
            Message(userChat, "user")
        )

        val requestBody = RequestBody.create(MediaType.parse("application/json"),
            Gson().toJson(
                ImageRequest(
                    "dall-e-2",
                    userChat,
                    1,
                    "256x256"
                )
            )
        )


        val apiKey = "Bearer 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"


        lifecycleScope.launch(Dispatchers.IO){

            val responses = retrofit.imageGenerateInterface(
                requestBody,
                "application/json",
                apiKey
            )

            val imageResponse = responses.data.first().url


            withContext(Dispatchers.Main){

                chatData.add(ChatData(imageResponse, "chatBot"))
                adapter.notifyItemInserted(chatData.size - 1)
                binding.chatRecyclerView.smoothScrollToPosition(chatData.size - 1);
            }
        }
    }
}