package com.nassican.appmovilesnassican

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    // Global variables
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var mProgressBar: ProgressDialog

    // Firebase authentication
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialise()
    }

    // Initialize UI elements and Firebase authentication
    private fun initialise() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        mProgressBar = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
    }

    // Login with Firebase
    private fun loginUser() {
        // Get user email and password
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        // Check if fields are not empty
        if (email.isNotEmpty() && password.isNotEmpty()) {

            // Show progress dialog
            mProgressBar.setMessage("Registering User...")
            mProgressBar.show()

            // Sign in with email and password
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // If sign-in is successful, go to Home
                        goHome()
                    } else {
                        // If sign-in fails, notify the user
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    // Go to HomeActivity
    private fun goHome() {
        // Hide progress bar
        mProgressBar.hide()

        // Start HomeActivity
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    // OnClick method for login button
    fun login(view: View) {
        loginUser()
    }

    // OnClick method for forgot password button
    fun forgotPassword(view: View) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    // OnClick method for register button
    fun register(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
