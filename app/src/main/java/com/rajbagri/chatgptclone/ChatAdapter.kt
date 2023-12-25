package com.rajbagri.chatgptclone

import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ChatAdapter(val userChat: ArrayList<ChatData>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userTextView: TextView
        val chatBotTextView: TextView

        init {
            userTextView = itemView.findViewById(R.id.user_text_view)
            chatBotTextView = itemView.findViewById(R.id.chatbot_text_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        var view: View? = null

        var context = LayoutInflater.from(parent.context)
        if(viewType == 0){
            view = context.inflate(R.layout.user_chat_layout, parent, false)
        }
        else{
            view = context.inflate(R.layout.chatbot_user_layout, parent, false)
        }

        return ChatViewHolder(view)

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

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentData = userChat[position]

        if(currentData.userType == "chatBot"){
            holder.chatBotTextView.text = currentData.chat
        }
        else if(currentData.userType == "user"){
            holder.userTextView.text = currentData.chat
        }

    }
}