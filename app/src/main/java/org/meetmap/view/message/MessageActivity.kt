package org.meetmap.view.message

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import org.meetmap.R
import org.meetmap.data.model.domain.Familiar
import org.meetmap.view.familiar.OBJECT_FAMILIAR
import timber.log.Timber

class MessageActivity : AppCompatActivity() {

    private lateinit var nav: NavController
   private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        nav = Navigation.findNavController(this, R.id.navHostFragment)

        val familiar: Familiar? = intent.getParcelableExtra(OBJECT_FAMILIAR)

        Timber.d("familiar: %s", familiar.toString())
        val bundle = Bundle()
        bundle.putParcelable("familiar",familiar)
        nav.setGraph(nav.graph,bundle)

        appBarConfiguration = AppBarConfiguration(nav.graph)
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.navHostFragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
}