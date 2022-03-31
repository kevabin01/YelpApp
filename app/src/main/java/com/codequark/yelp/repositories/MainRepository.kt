package com.codequark.yelp.repositories

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.navigation.ui.AppBarConfiguration
import com.codequark.yelp.R
import com.codequark.yelp.executors.AppExecutors
import com.codequark.yelp.models.business.DetailItem
import com.codequark.yelp.models.location.UserLocation
import com.codequark.yelp.models.review.Review
import com.codequark.yelp.room.models.LocalBusiness
import com.codequark.yelp.utils.Constants.ItemDef
import com.codequark.yelp.utils.Constants.MainStateDef
import com.codequark.yelp.utils.LogUtils
import com.codequark.yelp.viewModels.SecureLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainRepository private constructor(@NonNull context: Context): LoginRepository(context) {
    companion object {
        @Volatile
        private var instance: MainRepository? = null

        fun getInstance(@NonNull context: Context): MainRepository = instance ?: synchronized(this) {
            instance ?: MainRepository(context).also {
                instance = it
            }
        }
    }

    @NonNull
    private val destination = SecureLiveData(0)

    @NonNull
    private val mainState = SecureLiveData(MainStateDef.STATE_DEFAULT)

    @NonNull
    private val userLocation = SecureLiveData(UserLocation(0.0, 0.0))

    @NonNull
    private var query = ""

    @NonNull
    val navConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(
        R.id.navigationLogin,
        R.id.navigationRegister,
        R.id.navigationSearch,
        R.id.navigationMap,
        R.id.navigationHistory
    ).build()

    @NonNull
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Nullable
    private var lastLocation: Location? = null

    @NonNull
    fun getDestination(): LiveData<Int> {
        return destination
    }

    @NonNull
    fun getMainState(): LiveData<Int> {
        return mainState
    }

    @NonNull
    fun getUserLocation(): LiveData<UserLocation> {
        return userLocation
    }

    @NonNull
    fun getQuery(): String {
        return query
    }

    fun setDestination(@IdRes destination: Int) {
        this.destination.value = destination
        this.destination.postValue(0)
    }

    fun setMainState(@MainStateDef mainState: Int) {
        this.mainState.value = mainState
        this.mainState.postValue(MainStateDef.STATE_DEFAULT)
    }

    private fun setUserLocation(@NonNull latitude: Double, @NonNull longitude: Double) {
        this.userLocation.value = UserLocation(latitude, longitude)
    }

    fun setQuery(@NonNull query: String) {
        this.query = query
    }

    fun logout() {
        logoutFirebase()
        deleteAll()
        setDestination(R.id.navigationLogin)
    }

    fun getInfoDetail(@NonNull model: LocalBusiness): List<DetailItem> {
        val list: MutableList<DetailItem> = ArrayList()

        list.add(DetailItem(ItemDef.HEADER, "Detalles"))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Nombre:", model.name))
        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Categoría:", model.categories))
        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Dirección:", model.displayAddress))
        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Teléfono:", model.displayPhone))

        return list
    }

    fun getInfoLocation(@NonNull model: LocalBusiness): List<DetailItem> {
        val list: MutableList<DetailItem> = ArrayList()

        list.add(DetailItem(ItemDef.HEADER, "Negocio"))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "ID:", model.id))
        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Reseñas:", model.reviewCount.toString()))
        list.add(DetailItem(ItemDef.DIVIDER))

        if(model.isClosed) {
            list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Estado:", "Abierto"))
        } else {
            list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Estado:", "Cerrado"))
        }

        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Calificación:", model.rating.toString()))

        if(model.price != null) {
            list.add(DetailItem(ItemDef.DIVIDER))
            list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Precio:", model.price))
        }

        list.add(DetailItem(ItemDef.HEADER, "Ubicación"))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Ciudad:", model.city))
        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Código Postal:", model.zipCode))
        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "País:", model.country))
        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Estado:", model.state))
        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Distancia:", "${model.distance.toInt()} metros"))

        list.add(DetailItem(ItemDef.HEADER, "Coordenadas"))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Latitud:", model.latitude.toString()))
        list.add(DetailItem(ItemDef.DIVIDER))
        list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Longitud:", model.longitude.toString()))

        return list
    }

    fun getInfoReviews(@NonNull reviews: List<Review>): List<DetailItem> {
        val list: MutableList<DetailItem> = ArrayList()

        reviews.forEach {
            list.add(DetailItem(ItemDef.HEADER, it.user.name))
            list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Calificación:", it.rating.toString()))
            list.add(DetailItem(ItemDef.DIVIDER))
            list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, "Fecha:", it.timeCreated))
            list.add(DetailItem(ItemDef.DIVIDER))
            list.add(DetailItem(ItemDef.CONTENT, R.drawable.ic_next, it.text))
        }

        return list
    }

    fun getLastLocation(@NonNull context: Context) {
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        if(this::fusedLocationClient.isInitialized) {
            LogUtils.print("fusedLocationClient.isInitialized")
        } else {
            LogUtils.print("fusedLocationClient.isNotInitialized")
            this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            getLastLocation(context)
            return
        }

        fusedLocationClient.lastLocation.addOnCompleteListener(AppExecutors.mainThread()) { task ->
            if(task.isSuccessful && task.result != null) {
                lastLocation = task.result
                val lastLocation = lastLocation ?: return@addOnCompleteListener
                val latitude = lastLocation.latitude
                val longitude = lastLocation.longitude

                setUserLocation(latitude, longitude)
            } else {
                LogUtils.print("getLastLocation:exception: " + task.exception)

                val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLastLocation(context)
                } else {
                    Toast.makeText(context, R.string.noLocationDetected, Toast.LENGTH_SHORT).show()
                    setMainState(MainStateDef.STATE_GPS_OFF)
                }
            }
        }
    }

    fun checkPermissions(@NonNull context: Context): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
}