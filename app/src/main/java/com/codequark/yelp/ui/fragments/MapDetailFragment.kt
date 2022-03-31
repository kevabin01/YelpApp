package com.codequark.yelp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codequark.yelp.R
import com.codequark.yelp.databinding.FragmentMapsBinding
import com.codequark.yelp.room.models.LocalBusiness
import com.codequark.yelp.utils.LogUtils
import com.codequark.yelp.viewModels.MainViewModel
import com.codequark.yelp.viewModels.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapDetailFragment: Fragment() {
    private lateinit var binding: FragmentMapsBinding

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory()
    }

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

            viewModel.localBusiness.observe(viewLifecycleOwner) { model ->
                if(model == null) {
                    return@observe
                }

                fillMap(model)
            }

            viewModel.userLocation.observe(viewLifecycleOwner) { userLocation ->
                if(userLocation == null) {
                    return@observe
                }

                val currentLocation = LatLng(userLocation.latitude, userLocation.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 11.0f))
            }
        }
    }

    private fun fillMap(@NonNull model: LocalBusiness) {
        if(this::googleMap.isInitialized) {
            LogUtils.print("googleMap.isInitialized")
        } else {
            LogUtils.print("googleMap.isNotInitialized")
            return
        }

        googleMap.clear()

        val latLng = LatLng(model.latitude, model.longitude)
        val markerOptions = MarkerOptions().position(latLng).title(model.name)
        googleMap.addMarker(markerOptions)?.tag = model.id
    }
}