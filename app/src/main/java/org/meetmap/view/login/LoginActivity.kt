package org.meetmap.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import org.meetmap.R

private const val LOGIN_VIEW = "LOGIN_VIEW"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(applicationContext)

        val view = supportFragmentManager.findFragmentByTag(LOGIN_VIEW) as LoginFragment?
            ?: LoginFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.root_activity_login, view, LOGIN_VIEW)
            .commitNowAllowingStateLoss()
    }
}