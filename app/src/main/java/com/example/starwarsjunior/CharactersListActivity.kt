package com.example.starwarsjunior

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.starwarsjunior.databinding.ActivityCharactersListBinding

class CharactersListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharactersListBinding
    lateinit var openCharactersBottomSheetButton: Button
    lateinit var bottomSheetFragment: BottomSheetFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharactersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //só para testar enquanto não tenho a lista
        binding.starWarsLogoSmall.setOnClickListener {
            startActivity(Intent(this, CharacterDetailActivity::class.java))
        }

        binding.backButton.setOnClickListener {
            finish()
        }


        binding.resetButton.setOnClickListener {
            binding.searchBox.setText("")
        }

        //fragment bottom sheet
        binding.openCharactersBottomSheetButton.setOnClickListener {
            bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, "BSDialogFragment")

        }

        binding.ascButton.setOnClickListener {
            binding.ascButton.setImageResource(R.drawable.arrow_up_yellow_group)
            binding.descButton.setImageResource(R.drawable.arrow_down_white_group)
            binding.ascButton.isClickable = false
            binding.descButton.isClickable = true
        }

        binding.descButton.setOnClickListener {
            binding.ascButton.setImageResource(R.drawable.arrow_up_white_group)
            binding.descButton.setImageResource(R.drawable.arrow_down_yellow_group)
            binding.ascButton.isClickable = true
            binding.descButton.isClickable = false
        }

        binding.nameButton.setOnClickListener {
            binding.nameButton.isClickable = false
            binding.yearButton.isClickable = true
            binding.yearButton.isChecked = false
        }

        binding.yearButton.setOnClickListener {
            binding.yearButton.isClickable = false
            binding.nameButton.isClickable = true
            binding.nameButton.isChecked = false
        }

    }

}