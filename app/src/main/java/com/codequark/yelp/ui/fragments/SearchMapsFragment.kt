package com.codequark.yelp.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codequark.yelp.R
import com.codequark.yelp.databinding.FragmentMapsBinding
import com.codequark.yelp.models.business.Business
import com.codequark.yelp.ui.activities.DetailActivity
import com.codequark.yelp.utils.LogUtils
import com.codequark.yelp.viewModels.MainViewModel
import com.codequark.yelp.viewModels.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SearchMapsFragment: Fragment() {
    private lateinit var binding: FragmentMapsBinding

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory()
    }

    @NonNull
    private val businesses: MutableList<Business> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it

            if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return@getMapAsync
            }

            googleMap.isMyLocationEnabled = true

            googleMap.setOnInfoWindowClickListener { marker ->
                val id = marker.tag as String
                LogUtils.print("Marker: $id")

                val item = getItem(id) ?: return@setOnInfoWindowClickListener
                viewModel.setModel(item)
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }

            viewModel.businesses.observe(viewLifecycleOwner) { list ->
                if(list == null) {
                    return@observe
                }

                fillMap(list)
            }

            viewModel.userLocation.observe(viewLifecycleOwner) { userLocation ->
                if(userLocation == null) {
                    return@observe
                }

                val currentLocation = LatLng(userLocation.latitude, userLocation.longitude)
                //googleMap.addMarker(MarkerOptions().position(currentLocation).title("Usuario"))
                //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12.0f))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12.0f))
            }
        }
    }

    private fun fillMap(@NonNull list: List<Business>) {
        this.businesses.clear()
        this.businesses.addAll(list)

        if(this::googleMap.isInitialized) {
            LogUtils.print("googleMap.isInitialized")
        } else {
            LogUtils.print("googleMap.isNotInitialized")
            return
        }

        googleMap.clear()

        list.forEach {
            val latLng = LatLng(it.coordinates.latitude, it.coordinates.longitude)
            val markerOptions = MarkerOptions().position(latLng).title(it.name)
            googleMap.addMarker(markerOptions)?.tag = it.id
        }
    }

    @Nullable
    private fun getItem(@NonNull id: String): Business? {
        businesses.forEach {
            if(it.id.contains(id)) {
                return it
            }
        }

        return null
    }
}