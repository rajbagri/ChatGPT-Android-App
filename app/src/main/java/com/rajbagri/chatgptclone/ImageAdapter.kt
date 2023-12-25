package com.rajbagri.chatgptclone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class ImageAdapter(val context: Context, val userChat: ArrayList<ChatData>): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userTextView: TextView
        val imageContainer: ImageView

        init {
            userTextView = itemView.findViewById(R.id.user_text_view)
            imageContainer = itemView.findViewById(R.id.image_container_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        var view: View? = null

        var context = LayoutInflater.from(parent.context)
        if(viewType == 0){
            view = context.inflate(R.layout.user_chat_layout, parent, false)
        }
        else{
            view = context.inflate(R.layout.image_generate_layout, parent, false)
        }

        return ImageViewHolder(view)

    }

    override fun getItemCount(): Int {
        return userChat.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentData = userChat[position]
        if(currentData.userType == "user"){
            return 0
        }
        else{
            return 1
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentData = userChat[position]

        if(currentData.userType == "chatBot"){
            Glide.with(context)
                .load(currentData.chat)
                .into(holder.imageContainer)
        }
        else if(currentData.userType == "user"){
            holder.userTextView.text = currentData.chat
        }

    }
}