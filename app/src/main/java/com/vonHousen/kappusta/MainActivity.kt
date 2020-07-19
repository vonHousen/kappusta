package com.vonHousen.kappusta

import android.app.Activity
import android.content.Context
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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vonHousen.kappusta.db.ReportDB
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var db: ReportDB
    }

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

        configureGoogleSignIn()

        configureDatabase()
        add_button.setOnClickListener {
            hideThings()
            navController.navigate(R.id.navigation_reporting)
        }
    }

    override fun onStart() {
        super.onStart()

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        // updateUI(account)    TODO
    }

    private fun configureGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    fun hideThings() {
        nav_view.visibility = View.GONE
        add_button.hide()
    }

    fun showThingsAfterReporting(withFragmentChange: Boolean = true) {
        nav_view.visibility = View.VISIBLE
        add_button.show()
        hideKeyboard()

        if (withFragmentChange)
            findNavController(R.id.nav_host_fragment).popBackStack()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        showThingsAfterReporting(withFragmentChange = false)
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

    private fun configureDatabase() {
        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    drop table 'BUDGET';
                """)
                database.execSQL("""
                    create table 'BUDGET' (
                        first_day_date  INTEGER not null
                    ,   budget_worth    REAL not null
                    ,   primary key (first_day_date)
                    );
                """)
            }
        }

        db = Room.databaseBuilder(
            applicationContext,
            ReportDB::class.java,
            "ReportDB"
        )
            .allowMainThreadQueries()   // TODO make it asynchronous
            .addMigrations(MIGRATION_5_6)
            .fallbackToDestructiveMigration()
            .build()
    }
}
