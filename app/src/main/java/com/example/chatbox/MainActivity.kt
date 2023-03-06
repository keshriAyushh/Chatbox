package com.example.chatbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatbox.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var users: ArrayList<User>
    private lateinit var dbRef: DatabaseReference
    private lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()
        users = ArrayList()

        userAdapter = UserAdapter(this@MainActivity, users)
        binding.usersRecyclerView.adapter = userAdapter
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)


        dbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()

                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (currentUser != null) {
                        if(currentUser.uid != auth.currentUser?.uid){
                            users.add(currentUser)
                        } else {
                            binding.tvGreet.text = "Welcome, ${currentUser.name.toString()}"
                        }
                    }
                }

                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.ivLogOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@MainActivity, LogIn::class.java))
            finish()
        }

    }
}