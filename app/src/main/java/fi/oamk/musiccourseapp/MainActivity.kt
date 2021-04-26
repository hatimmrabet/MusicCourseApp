package fi.oamk.musiccourseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    //private lateinit var bottomNav: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        setupBottomNavMenu(navController)

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

//        val viewModel: ActivityViewModel by viewModels()
//        viewModel.user.observe(this, { user ->
//            if(user == null) {
//                bottomNav.menu.getItem(3).isVisible = false
//                bottomNav.menu.getItem(4).isVisible = false
//            }
//        })

       // myRef.setValue("Hello, World!")

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupBottomNavMenu(navController: NavController) {
        //Setup Bottom Nav with NavigationUI
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
//        bottomNav.menu.getItem(3).isVisible = false
        bottomNav.setupWithNavController(navController)
    }
}