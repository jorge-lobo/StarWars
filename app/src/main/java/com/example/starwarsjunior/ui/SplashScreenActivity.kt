package com.example.starwarsjunior.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.starwarsjunior.R

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel = ViewModelProvider(this).get(SplashScreenViewModel::class.java)

        viewModel.preloadDataFromAPI()

        viewModel.preloadComplete.observe(this, Observer { isComplete ->
            if (isComplete == 2) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }
}