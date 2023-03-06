package com.example.chatbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.widget.Toast
import androidx.room.Database
import com.example.chatbox.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.identity.SignInPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference

        binding.redirect.setOnClickListener {
            val intent: Intent = Intent(this@SignUp, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignUp.setOnClickListener {
            if(binding.etName.text != null && binding.etEmail.text != null && binding.etPassword.text != null) {
                signUpWithEmailAndPassword(binding.etName.text.toString(), binding.etEmail.text.toString(), binding.etPassword.text.toString())
            } else {
                Toast.makeText(this@SignUp, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUpWithEmailAndPassword(name: String, email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                addUserToDatabase(name, email, auth.currentUser?.uid!!)
                val intent = Intent(this, LogIn::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT)
                    .show()
                Log.d("failSignup", it.toString())
            }
        }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String){
        dbRef.child("user").child(uid).setValue(User(name, email, uid))
    }

}