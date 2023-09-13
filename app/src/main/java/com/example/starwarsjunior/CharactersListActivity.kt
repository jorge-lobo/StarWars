package com.example.starwarsjunior

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityCharactersListBinding

class CharactersListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharactersListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharactersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //só para testar enquanto não tenho a lista
        binding.starWarsLogoSmall.setOnClickListener {
            startActivity(Intent(this, CharacterDetailActivity::class.java))
        }

        binding.filterButton.setOnClickListener {
            binding.filterSection.visibility = View.VISIBLE
        }


        val toggle: ToggleButton = findViewById(R.id.human_button)
        toggle.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, if (isChecked) "Geek Mode ON" else "Geek Mode OFF", Toast.LENGTH_SHORT).show()
        }


//        binding.humanButton.setOnClickListener {
//            binding.humanButton.
//        }
    }


}