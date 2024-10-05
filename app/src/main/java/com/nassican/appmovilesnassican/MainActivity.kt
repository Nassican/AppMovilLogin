package com.nassican.appmovilesnassican

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

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

        if (!hasLocationPermission()) {
            requestLocationPermission()
        }


    }

    // Initialize UI elements and Firebase authentication
    private fun initialise() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        mProgressBar = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Solicitar permisos de ubicación
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permiso concedido, puedes proceder con la funcionalidad que requiere ubicación
                } else {
                    // Permiso denegado, maneja esto apropiadamente (por ejemplo, mostrando un mensaje al usuario)
                    Toast.makeText(this, "Se requiere permiso de ubicación para algunas funciones", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
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
