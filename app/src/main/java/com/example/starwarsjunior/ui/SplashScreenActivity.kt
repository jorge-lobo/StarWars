package com.example.starwarsjunior.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.starwarsjunior.R
import com.example.starwarsjunior.data.personage.PersonageRepository
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel = ViewModelProvider(this).get(SplashScreenViewModel::class.java)

        checkCachedData()
    }

    private fun checkCachedData() {
        lifecycleScope.launch {
            //check if the data are cached
            val cachedData = PersonageRepository.getPersonages().result

            if (cachedData != null) {
                //cached data available -> go to MainActivity
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            } else {
                //data not in cache -> do preload
                viewModel.preloadDataFromAPI(refresh = true)
            }
        }
        viewModel.preloadComplete.observe(this, Observer { isComplete ->
            if (isComplete) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }
}