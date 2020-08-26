package com.vonHousen.kappusta

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import com.vonHousen.kappusta.db.authDB.AuthDB
import com.vonHousen.kappusta.db.authDB.LocalAuthRepository
import com.vonHousen.kappusta.db.reportDB.Migrations
import com.vonHousen.kappusta.db.reportDB.ReportDB
import com.vonHousen.kappusta.etc.RC_SIGN_IN
import com.vonHousen.kappusta.ui.authentication.AuthenticateFragment
import com.vonHousen.kappusta.ui.authentication.AuthenticateFragmentDirections
import com.vonHousen.kappusta.etc.HashUtils
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var db: ReportDB
        lateinit var auth_db: AuthDB
        lateinit var auth_repo: LocalAuthRepository
    }
    private var isLoggedOut: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_dashboard,
            R.id.navigation_settings,
            R.id.navigation_notifications,
            R.id.navigation_authentication,
            R.id.navigation_history
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        goToLoginFragment()

        configureAuthDatabase()
        add_button.setOnClickListener {
            hideThings()
            navController.navigate(R.id.navigation_reporting)
        }
    }

    fun goToLoginFragment(doSignOut: Boolean = false) {
        if (doSignOut) {
            isLoggedOut = true
        }
        val action = AuthenticateFragmentDirections.startAuthenticationFragment(doSignOut)
        findNavController(R.id.nav_host_fragment).navigate(action)
        hideThings()
    }

    fun hideThings() {
        nav_view.visibility = View.GONE
        add_button.hide()
    }

    fun showThings(withFragmentChange: Boolean = true) {
        nav_view.visibility = View.VISIBLE
        add_button.show()
        hideKeyboard()

        if (withFragmentChange)
            findNavController(R.id.nav_host_fragment).popBackStack()
    }

    override fun onBackPressed() {
        if(isLoggedOut) {
            // do nothing
        } else {
            super.onBackPressed()
            showThings(withFragmentChange = false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun hideKeyboardPublic() {
        hideKeyboard()
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun configureReportingDatabase(path_to_db: String = "ReportDB") {
        val migrations = Migrations()                       // TODO make it static
        val migration_5_6 = migrations.MIGRATION_5_6
        val migration_6_7 = migrations.MIGRATION_6_7

        db = Room.databaseBuilder(
            applicationContext,
            ReportDB::class.java,
            path_to_db
        ).allowMainThreadQueries()   // TODO make it asynchronous
         .addMigrations(migration_5_6, migration_6_7)
         .fallbackToDestructiveMigration()
         .build()
    }

    private fun configureAuthDatabase() {
        auth_db = Room.databaseBuilder(
            applicationContext,
            AuthDB::class.java,
            "AuthDB"
        ).allowMainThreadQueries()
         .fallbackToDestructiveMigration()
         .build()
        auth_repo = LocalAuthRepository(applicationContext)
    }

    private fun getAuthFragment(): AuthenticateFragment? {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) ?: return null
        val authFragment =
            navHostFragment.childFragmentManager.fragments[0] ?: return null

        return authFragment as AuthenticateFragment
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            // pass Google Authentication task to AuthenticateFragment
            val authFragment = getAuthFragment() ?: return
            authFragment.handleSignInResult(task)
        }
    }

    fun startWithUser(user: FirebaseUser) {
        val uidHashed = HashUtils.sha256(user.uid)
        val dbName = auth_repo.getDBNameForUser(uidHashed)
        configureReportingDatabase(dbName)
        showThings()
        isLoggedOut = false
    }
}
