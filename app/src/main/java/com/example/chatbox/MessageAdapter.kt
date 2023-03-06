package com.example.chatbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(
    private val context: Context,
    private val messages: ArrayList<Message>
): RecyclerView.Adapter<ViewHolder>() {

    private val ITEM_RECEIVED: Int = 1
    private val ITEM_SENT: Int = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType == 1){
            val view: View = LayoutInflater.from(context).inflate(R.layout.received_message, parent, false)
            return ReceivedMessageViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false)
            return SentMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder.javaClass == SentMessageViewHolder::class.java){
            val viewHolder = holder as SentMessageViewHolder
            viewHolder.sentMessage.text = messages[position].message
        } else {
            val viewHolder = holder as ReceivedMessageViewHolder
            viewHolder.receivedMessage.text = messages[position].message
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messages[position]
        return if(FirebaseAuth.getInstance().currentUser!!.uid == currentMessage.senderId){
            ITEM_SENT
        } else {
            ITEM_RECEIVED
        }
    }
    class SentMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage: TextView = itemView.findViewById(R.id.tvSentMessage)
    }

    class ReceivedMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val receivedMessage: TextView = itemView.findViewById(R.id.tvReceivedMessage)
    }
}