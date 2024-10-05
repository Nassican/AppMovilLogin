package com.nassican.appmovilesnassican

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polygon

class OpenStreetMapActivity : AppCompatActivity() {
    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        setContentView(R.layout.activity_open_street_map)
        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)

        val mapController = map.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(1.21313, -77.27828)
        mapController.setCenter(startPoint)

        // Definir los puntos del polígono
        val polygonPoints = listOf(
            GeoPoint(1.206655, -77.281150),
            GeoPoint(1.210916, -77.283121),
            GeoPoint(1.211678, -77.281512),
            GeoPoint(1.217331, -77.283958),
            GeoPoint(1.217513, -77.283711),
            GeoPoint(1.216714, -77.283025),
            GeoPoint(1.217315, -77.282333),
            GeoPoint(1.219347, -77.278111),
            GeoPoint(1.218682, -77.277424),
            GeoPoint(1.217728, -77.277167),
            GeoPoint(1.21336, -77.27532),
            GeoPoint(1.212424, -77.275525),
            GeoPoint(1.210337, -77.273862),
            GeoPoint(1.209919, -77.274013),
            GeoPoint(1.208385, -77.276180)
        )

        // Dibujar el polígono
        val polygon = Polygon()
        polygon.points = polygonPoints
        polygon.fillColor = Color.argb(128, 255, 0, 0)
        polygon.strokeColor = Color.RED
        polygon.strokeWidth = 5f
        map.overlays.add(polygon)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}