package com.example.starwarsjunior.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
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

        startLogoAnimation()

        viewModel.preloadComplete.observe(this, Observer { preloadProgress ->
            if (preloadProgress == 4) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }

    //logo animation
    private fun startLogoAnimation() {
        val logoView: View = findViewById(R.id.star_wars_logo_small)
        val loadingProgressBar: ProgressBar = findViewById(R.id.loading)

        val introAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        introAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                loadingProgressBar.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })

        logoView.startAnimation(introAnimation)
    }
}