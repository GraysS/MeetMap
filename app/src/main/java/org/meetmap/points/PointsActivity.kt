package org.meetmap.points

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.meetmap.R

class PointsActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points)

        //Initialize Bottom Navigation View.
        val navView = findViewById<BottomNavigationView>(R.id.bottomNav_view)
        //Initialize NavController.
        val navController = Navigation.findNavController(this, R.id.navHostFragment)

        //Pass the ID's of Different destinations
         appBarConfiguration = AppBarConfiguration.Builder(
            R.id.points_fragment_dest,
            R.id.navigation_settings,
            R.id.navigation_chats,
        ).build()
        setupWithNavController(navView, navController)
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()

}