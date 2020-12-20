package com.ryl.securedcamera.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ryl.securedcamera.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity(R.layout.activity_main) {

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.mainScreenFragmentContainer) as NavHostFragment
    }
    private val navController: NavController by lazy { navHostFragment.navController }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViews()
    }

    private fun setViews() {
        mainScreenBottomNavigationView.setupWithNavController(navController)
    }

}