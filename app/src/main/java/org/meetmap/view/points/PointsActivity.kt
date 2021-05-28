package org.meetmap.view.points

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import org.meetmap.R

class PointsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var nav: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points)
        FirebaseApp.initializeApp(applicationContext)

        //Initialize Bottom Navigation View.
        val navView = findViewById<BottomNavigationView>(R.id.bottomNav_view)
        //Initialize NavController.
        nav = Navigation.findNavController(this, R.id.navHostFragment)

        //Pass the ID's of Different destinations
         appBarConfiguration = AppBarConfiguration.Builder(
            R.id.points_fragment_dest,
            R.id.familiar_fragment_dest,
            R.id.settings_fragment_dest,
        ).build()
        setupWithNavController(navView, nav)
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()

}