package com.example.chatbox

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbox.databinding.ActivityChatBinding
import com.example.chatbox.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.ByteArrayOutputStream

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var messages: ArrayList<Message>
    private lateinit var name: String
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var receiverUid: String

    var receiverRoom: String? = null
    var senderRoom: String? = null
    var image: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide(

        )
        name = intent.getStringExtra("name").toString()
        receiverUid = intent.getStringExtra("uid").toString()
        binding.tvReceiverName.text = name

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()

        val senderUid = auth.currentUser?.uid
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        messages = ArrayList()
        messageAdapter = MessageAdapter(this@ChatActivity, messages)
        binding.chatsRecyclerView.adapter = messageAdapter
        binding.chatsRecyclerView.layoutManager = LinearLayoutManager(this@ChatActivity)

        //getting chats from database
        dbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for(postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message::class.java)
                    if(message!=null){
                        messages.add(message)
                    }
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        //posting chats to database
        binding.ivSend.setOnClickListener {

            if(binding.etMessage.text.toString().isNotEmpty()){
                val message = binding.etMessage.text.toString().trim()
                val messageObject = Message(message, senderUid)

                dbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                binding.etMessage.text = null
            }

        }

    }
}