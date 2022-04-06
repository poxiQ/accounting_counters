package glebova.rsue.countwater

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import glebova.rsue.countwater.databinding.ActivityMainBinding
import glebova.rsue.countwater.databinding.FragmentSplashBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}