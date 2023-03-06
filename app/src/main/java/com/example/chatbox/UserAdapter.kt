package com.example.chatbox

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(
    private val context: Context,
    private val users: ArrayList<User>
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.name.text = users[position].name.toString()

        holder.itemView.setOnClickListener {
            val intent: Intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", users[position].name.toString())
            intent.putExtra("uid", users[position].uid.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.tvUserName)
    }
}