package com.rajbagri.chatgptclone

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

import com.rajbagri.chatgptclone.databinding.ActivityChatBinding

class Chat : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var chatData: ArrayList<ChatData> = ArrayList()
    private lateinit var chatRequest: ChatRequest
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
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
        adapter = ChatAdapter(chatData)
        mLinearLayoutManager = LinearLayoutManager(this@Chat)
        mLinearLayoutManager.stackFromEnd = true
        binding.chatRecyclerView.layoutManager = mLinearLayoutManager
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
                    ChatRequest(
                        messages,
                        "gpt-3.5-turbo"
                    )
                )
            )



        val apiKey = "Bearer ${BuildConfig.API_KEY}"


        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val responses = retrofit.createChatCompletion(requestBody, "application/json", apiKey)
                val textResponse = responses.choices.firstOrNull()?.message?.content

                if (textResponse.isNullOrEmpty()) {
                    Log.d("unsuccesfull", "Response is empty or null")
                    // Handle empty or null response
                } else {
                    Log.d("succesfull", textResponse)
                    withContext(Dispatchers.Main) {
                        chatData.add(ChatData(textResponse, "chatBot"))
                        adapter.notifyItemInserted(chatData.size - 1)
                        binding.chatRecyclerView.smoothScrollToPosition(chatData.size - 1)
                    }
                }
            } catch (e: Exception) {
                Log.e("NetworkError", "Error: ${e.message}", e)
                // Handle the network exception here
            }
        }




    }
}