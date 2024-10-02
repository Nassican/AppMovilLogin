package com.nassican.appmovilesnassican

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterActivity : AppCompatActivity() {
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    // Global variables
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initialise()
    }

    // Initialize UI elements, Firebase authentication, and database
    private fun initialise() {
        txtName = findViewById(R.id.etName)
        txtLastName = findViewById(R.id.etLastName)
        txtEmail = findViewById(R.id.etEmail)
        txtPassword = findViewById(R.id.etPassword)
        progressBar = ProgressDialog(this)

        // Initialize Firebase instances
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        databaseReference = database.reference.child("Users")
    }

    // Create a new account
    private fun createNewAccount() {
        // Get text input values
        firstName = txtName.text.toString()
        lastName = txtLastName.text.toString()
        email = txtEmail.text.toString()
        password = txtPassword.text.toString()

        // Check if the fields are not empty
        if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            progressBar.setMessage("Usuario registrado...")
            progressBar.show()

            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Get the current user
                        val user = auth.currentUser!!
                        // Send verification email
                        verifyEmail(user)

                        // Save user info to the database
                        val currentUserDb = databaseReference.child(user.uid)
                        currentUserDb.child("firstName").setValue(firstName)
                        currentUserDb.child("lastName").setValue(lastName)

                        // Go to HomeActivity
                        updateUserInfoAndGoHome()
                    } else {
                        // Show error message if registration fails
                        Toast.makeText(this, "Error en la autenticación.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    // Handle failure
                    Toast.makeText(this, "Error en la autenticación.", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Show error message if fields are empty
            Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    // OnClick method for register button
    fun register(view: View) {
        createNewAccount()
    }

    // Navigate to HomeActivity and hide the progress bar
    private fun updateUserInfoAndGoHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        progressBar.hide()
    }

    // Send email verification
    private fun verifyEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email enviado a: ${user.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al verificar el correo.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
