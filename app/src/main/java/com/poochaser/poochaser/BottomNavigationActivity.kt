package com.poochaser.poochaser

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.poochaser.poochaser.ui.calendar.CalendarFragment
import com.poochaser.poochaser.ui.clock.ClockFragment
import com.poochaser.poochaser.ui.settings.SettingsFragment

class BottomNavigationActivity : AppCompatActivity() {
    private val clockFragment = ClockFragment()
    private val calendarFragment = CalendarFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, clockFragment)
            .commit()

        navView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.clock -> {
                    changeFragment(clockFragment)
                    true
                }
                R.id.calendar -> {
                    changeFragment(calendarFragment)
                    true
                }
                R.id.settings -> {
                    changeFragment((settingsFragment))
                    true
                }
                else -> false
            }
        }
    }

    private fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
