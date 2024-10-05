package com.nassican.appmovilesnassican

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var btnLogout: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializamos FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // Inicializamos los elementos de la UI
        btnLogout = findViewById(R.id.btnLogout)

        // Configuramos la acción para cerrar sesión
        btnLogout.setOnClickListener {
            logout()
        }

        findViewById<Button>(R.id.btnGoogleMaps).setOnClickListener {
            val intent = Intent(this, GoogleMapsActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnOpenStreetMap).setOnClickListener {
            val intent = Intent(this, OpenStreetMapActivity::class.java)
            startActivity(intent)
        }

        // Muestra el nombre del usuario (opcional)
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val welcomeMessage = "Bienvenido, ${currentUser.email}"
            findViewById<TextView>(R.id.tvWelcome).text = welcomeMessage
        }
    }

    private fun logout() {
        mAuth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}
