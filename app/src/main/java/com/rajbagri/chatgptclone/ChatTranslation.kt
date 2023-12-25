package com.rajbagri.chatgptclone

import com.rajbagri.chatgptclone.ApiInterface
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.rajbagri.chatgptclone.databinding.ActivityChatTranslationBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ChatTranslation : AppCompatActivity() {
    private lateinit var binding: ActivityChatTranslationBinding

    private lateinit var audioRecord: AudioRecord
    private lateinit var audioData: ShortArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatTranslationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sampleRate = 44100
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                0
            )
            return
        }
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        audioData = ShortArray(bufferSize / 2) // Buffer to store audio data

        binding.play.setOnClickListener {
            startRecording()
        }

        binding.pause.setOnClickListener {
            stopRecording()
        }
    }

    private fun startRecording() {
        audioRecord.startRecording()

        Thread {
            while (true) {
                audioRecord.read(audioData, 0, audioData.size)
                // Process or use audioData as needed
            }
        }.start()
    }

    private fun stopRecording() {
        audioRecord.stop()
        audioRecord.release()
        sentButtonClickListner()
    }

    fun convertShortArrayToByteArray(shortArray: ShortArray): ByteArray {
        val byteBuffer = java.nio.ByteBuffer.allocate(shortArray.size * 2) // 2 bytes for each short
        byteBuffer.asShortBuffer().put(shortArray)
        return byteBuffer.array()
    }




    private fun sentButtonClickListner() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(ApiInterface::class.java)

        val apiKey = "Bearer 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"


        val audioRequestBody = RequestBody.create(
            MediaType.parse("audio/mp4"),
            convertShortArrayToByteArray(audioData)
        )

        val audioPart = MultipartBody.Part.createFormData(
            "audio", "recorded_audio.wav", audioRequestBody
        )

        val requestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            Gson().toJson("whisper-1")
        )

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = retrofit.audioGenerateInterface(audioPart, requestBody, apiKey)



                val textResponse = response.text

                withContext(Dispatchers.Main) {
                    binding.response.text = textResponse
                }
            } catch (e: Exception) {
                // Handle any exceptions or errors here
                Log.d("Failed", (e.message).toString())
            }
        }
    }

}