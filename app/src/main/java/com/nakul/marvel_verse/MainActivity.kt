package com.nakul.marvel_verse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nakul.marvel_verse.fragments.CharacterFragment
import com.nakul.marvel_verse.fragments.ComicFragment
import com.nakul.marvel_verse.utils.BackPressedEvent

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
    }

    private fun initUI() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frame, CharacterFragment())
            .commit()
        bottomNav = findViewById(R.id.bottom_nav)
        bottomNav.selectedItemId = R.id.characters
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.characters -> supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, CharacterFragment()).commit()
                R.id.comics -> supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frame, ComicFragment()).commit()
            }

            return@setOnItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.main_frame)

        (fragment as? BackPressedEvent)?.onBackPressed()?.let {
            if (it)
                if ((fragment is CharacterFragment))
                    super.onBackPressed()
                else
                    bottomNav.selectedItemId = R.id.characters
        }

    }
}