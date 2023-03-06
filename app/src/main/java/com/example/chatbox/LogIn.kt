package com.example.chatbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chatbox.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            if(binding.etEmail.text == null && binding.etPassword.text == null){
                Toast.makeText(this@LogIn, "Empty fields are not allowed!", Toast.LENGTH_SHORT).show()
            } else {
                logInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            }
        }

        binding.redirect.setOnClickListener {
            val intent: Intent = Intent(this@LogIn, SignUp::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun logInWithEmailAndPassword(email: String, password: String){

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

            if(it.isSuccessful){
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login failed, please try again", Toast.LENGTH_SHORT).show()
                Log.d("failLogin", it.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            startActivity(Intent(this@LogIn, MainActivity::class.java))
            finish()
        }
    }
}