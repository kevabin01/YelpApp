package com.codequark.yelp.ui.activities

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.codequark.yelp.BuildConfig
import com.codequark.yelp.R
import com.codequark.yelp.databinding.ActivityMainBinding
import com.codequark.yelp.managers.NetworkManager.NetworkStateDef
import com.codequark.yelp.ui.dialogs.LoadingDialog
import com.codequark.yelp.utils.Constants
import com.codequark.yelp.utils.Constants.MainStateDef
import com.codequark.yelp.utils.LogUtils
import com.codequark.yelp.viewModels.MainViewModel
import com.codequark.yelp.viewModels.NetworkViewModel
import com.codequark.yelp.viewModels.ViewModelFactory

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory()
    }

    private val networkViewModel by viewModels<NetworkViewModel> {
        ViewModelFactory()
    }

    private lateinit var loadingBuilder: LoadingDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.layoutToolbar.toolbar)
        val actionBar = supportActionBar

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
            binding.layoutToolbar.toolbar.navigationIcon = null
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment? ?: throw RuntimeException("NavHostFragment is null")

        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            manageDestination(destination)
        }

        binding.layoutToolbar.appBarLayout.outlineProvider = null

        binding.bottomNavigationView.setOnItemReselectedListener {}

        NavigationUI.setupActionBarWithNavController(this, navController, viewModel.navConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        loadingBuilder = LoadingDialog.Builder(this)

        networkViewModel.network.observe(this) { integer ->
            when (integer) {
                NetworkStateDef.DEFAULT -> {
                    LogUtils.print("Default case for initialize NetworkCheck")
                }

                NetworkStateDef.CONNECTED -> {

                }

                NetworkStateDef.DISCONNECTED -> {

                }
            }
        }

        viewModel.destination.observe(this) { destination ->
            if(destination == 0) {
                return@observe
            }

            navController.navigate(destination)
        }

        viewModel.mainState.observe(this) { state ->
            when (state) {
                MainStateDef.STATE_FINISH -> {
                    finish()
                }

                MainStateDef.STATE_GPS_ON -> {
                    LogUtils.print("GPS ON")
                }

                MainStateDef.STATE_GPS_OFF -> {
                    LogUtils.print("GPS OFF")

                    val action: String = Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    val intent = Intent(action)
                    startActivity(intent)
                }
            }
        }

        viewModel.userLocation.observe(this) { userLocation ->
            if(userLocation.latitude == 0.0 && userLocation.longitude == 0.0) {
                return@observe
            }

            LogUtils.print("Latitude: ${userLocation.latitude}")
            LogUtils.print("Longitude: ${userLocation.longitude}")
        }

        viewModel.getUpdating().observe(this) { updating ->
            hideKeyboard()

            if(updating) {
                loadingBuilder.create()
            } else {
                loadingBuilder.cancel()
            }
        }

        viewModel.getException().observe(this) { ex ->
            ex?.let {
                Toast.makeText(this@MainActivity, R.string.textError, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getConnection().observe(this) { connection ->
            if(connection) {
                Toast.makeText(this@MainActivity, R.string.textCheckInternet, Toast.LENGTH_SHORT).show()
            }
        }

        binding.fab.setOnClickListener {
            val searchView: View = findViewById(R.id.actionSearch)
            searchView.performClick()
        }
    }

    override fun onStart() {
        super.onStart()

        if(viewModel.checkPermissions()) {
            viewModel.getLastLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), Constants.REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == Constants.REQUEST_PERMISSIONS_REQUEST_CODE) {
            when (PackageManager.PERMISSION_GRANTED) {
                grantResults[0] -> {
                    // Permission granted.
                    viewModel.getLastLocation()
                }

                else -> {
                    Toast.makeText(this, R.string.permissionDeniedExplanation, Toast.LENGTH_SHORT).show()

                    val intent = Intent()
                    val uri: Uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)

                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                    startActivity(intent)

                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 1000)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.actionSearch)
        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding.fab.visibility = View.VISIBLE
                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                binding.fab.visibility = View.GONE
                return true
            }
        })

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.setQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.setQuery(newText)
                return true
            }
        })

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val navDestination = navController.currentDestination ?: return false

        when (navDestination.id) {
            R.id.navigationLogin -> {
                disableMenu(menu)
            }

            R.id.navigationRegister -> {
                disableMenu(menu)
            }

            R.id.navigationSearch -> {
                enableMenu(menu)
            }

            R.id.navigationMap -> {
                enableMenu(menu)
            }

            R.id.navigationHistory -> {
                disableMenu(menu)
            }

            else -> {
                throw RuntimeException("Unknown Nav: " + navDestination.label)
            }
        }

        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.actionList -> {
                viewModel.setDestination(R.id.navigationSearch)

                return true
            }

            R.id.actionMap -> {
                viewModel.setDestination(R.id.navigationMap)

                return true
            }

            R.id.actionLogout -> {
                viewModel.logout()

                return true
            }

            else -> {
                return false
            }
        }
    }

    override fun onBackPressed() {
        val navDestination = navController.currentDestination

        if(navDestination != null && (navDestination.id == R.id.navigationLogin || navDestination.id == R.id.navigationSearch || navDestination.id == R.id.navigationMap || navDestination.id == R.id.navigationHistory)) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun enableMenu(@NonNull menu: Menu) {
        menu.findItem(R.id.actionSearch).isEnabled = true
        menu.findItem(R.id.actionSearch).isVisible = true
        menu.findItem(R.id.actionList).isEnabled = true
        menu.findItem(R.id.actionList).isVisible = true
        menu.findItem(R.id.actionMap).isEnabled = true
        menu.findItem(R.id.actionMap).isVisible = true
        menu.findItem(R.id.actionLogout).isEnabled = true
        menu.findItem(R.id.actionLogout).isVisible = true
    }

    private fun disableMenu(@NonNull menu: Menu) {
        menu.findItem(R.id.actionSearch).isEnabled = false
        menu.findItem(R.id.actionSearch).isVisible = false
        menu.findItem(R.id.actionList).isEnabled = false
        menu.findItem(R.id.actionList).isVisible = false
        menu.findItem(R.id.actionMap).isEnabled = false
        menu.findItem(R.id.actionMap).isVisible = false
        menu.findItem(R.id.actionLogout).isEnabled = false
        menu.findItem(R.id.actionLogout).isVisible = false
    }

    private fun manageDestination(@NonNull destination: NavDestination) {
        invalidateOptionsMenu()

        if(destination.id == R.id.navigationLogin || destination.id == R.id.navigationRegister || destination.id == R.id.navigationHistory) {
            binding.fab.visibility = View.GONE
        } else {
            binding.fab.visibility = View.VISIBLE
        }

        if(destination.id == R.id.navigationLogin || destination.id == R.id.navigationRegister) {
            binding.bottomNavigationView.visibility = View.GONE
        } else {
            binding.bottomNavigationView.visibility = View.VISIBLE
        }

        viewModel.setQuery("")
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        val currentFocus: View? = currentFocus

        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(
                it.windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    }
}