package com.nassican.appmovilesnassican

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        // Obtener el SupportMapFragment y notificar cuando el mapa esté listo para ser usado
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Verificar permisos de ubicación
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupMap()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun setupMap() {
        try {
            // Habilitar la capa de mi ubicación
            mMap.isMyLocationEnabled = true

            // Definir los puntos del polígono (ajusta estas coordenadas según tus necesidades)
            val polygonPoints = listOf(
                LatLng(1.206633, -77.281110),
                LatLng(1.210913, -77.283213),
                LatLng(1.211702, -77.281529),
                LatLng(1.217512, -77.284048),
                LatLng(1.217679, -77.283881),
                LatLng(1.216769, -77.283062),
                LatLng(1.217345, -77.282348),
                LatLng(1.219363, -77.278145),
                LatLng(1.218650, -77.277448),
                LatLng(1.217724, -77.277220),
                LatLng(1.213416, -77.275399),
                LatLng(1.212400, -77.275596),
                LatLng(1.210369, -77.273930),
                LatLng(1.209916, -77.274160),
                LatLng(1.208430, -77.276233),
            )

            // Dibujar el polígono
            mMap.addPolygon(
                PolygonOptions()
                    .addAll(polygonPoints)
                    .fillColor(Color.argb(128, 255, 0, 0))
                    .strokeColor(Color.RED)
            )

            // Agregar un marcador en el centro del polígono
            val center = LatLng(1.213888, -77.278833)
            mMap.addMarker(MarkerOptions().position(center).title("Centro del área"))

            // Mover la cámara al centro del polígono
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15f))

            // Configurar los controles del mapa
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isCompassEnabled = true

        } catch (e: SecurityException) {
            Toast.makeText(this, "Error al configurar el mapa: ${e.message}", Toast.LENGTH_LONG).show()
        }
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
                    setupMap()
                } else {
                    Toast.makeText(this, "Se requiere permiso de ubicación para mostrar tu ubicación", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
}