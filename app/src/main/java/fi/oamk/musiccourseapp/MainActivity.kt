package fi.oamk.musiccourseapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.findTeacherFragment, R.id.postsFragment, R.id.scheduleFragment, R.id.chatsFragment, R.id.accountInfoFragment,
            R.id.loginFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupBottomNavMenu(navController)

        val viewModel: ActivityViewModel by viewModels()
        viewModel.user.observe(this) { user ->
            if (user == null) {
                bottomNav.menu.getItem(3).isVisible = false
                bottomNav.menu.getItem(4).isVisible = false
            } else {
                bottomNav.menu.getItem(3).isVisible = true
                bottomNav.menu.getItem(4).isVisible = true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupBottomNavMenu(navController: NavController) {
        //Setup Bottom Nav with NavigationUI
        bottomNav = findViewById(R.id.bottom_nav_view)
        bottomNav.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }
}