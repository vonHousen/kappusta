package com.vonHousen.kappusta

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
            R.id.navigation_notifications,
            R.id.navigation_history))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        add_button.setOnClickListener {
            hideThingsForReporting()
            navController.navigate(R.id.navigation_reporting)
        }
    }

    private fun hideThingsForReporting() {
        nav_view.visibility = View.GONE
        add_button.hide()
    }

    fun showThingsAfterReporting(withFragmentChange: Boolean = true) {
        nav_view.visibility = View.VISIBLE
        add_button.show()

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

}
