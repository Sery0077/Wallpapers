package sery.vlasenko.wallpapers.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import sery.vlasenko.wallpapers.R
import sery.vlasenko.wallpapers.databinding.ActivityMainBinding
import sery.vlasenko.wallpapers.utils.Router

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToolbarActivity {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbar)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        val navController = findNavController(R.id.fragment_container_view)
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        Router.setNavController(navController)
        super.onPostCreate(savedInstanceState)
    }

    override fun onDestroy() {
        Router.setNavController(null)
        super.onDestroy()
    }

    override fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }
}