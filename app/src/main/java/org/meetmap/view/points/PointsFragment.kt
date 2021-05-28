package org.meetmap.view.points

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import org.meetmap.R
import org.meetmap.common.MAP_NOT_READY
import org.meetmap.common.requestPermission
import org.meetmap.util.showSnackbar
import org.meetmap.util.showToast
import org.meetmap.view.points.buildPoints.PointsInjector
import timber.log.Timber

class PointsFragment : Fragment(),OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener {

    private lateinit var viewModel: PointsViewModel
    // Loading
    private lateinit var llPbs: LinearLayout

    private val readLocationPermissionResult: ActivityResultLauncher<String> by requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
        granted = {
            Timber.d("Granted")
        }, denied = {
            Timber.d("Denied")
            requireActivity().finish()
        })

    private var mMap: GoogleMap? = null

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.setOnMarkerClickListener(this)
        mMap?.setOnMapLongClickListener(this)
        enableMyLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_points, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llPbs = view.findViewById(R.id.ll_pbs)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            PointsInjector(requireActivity().application).providePointsViewModelFactory()
        ).get(PointsViewModel::class.java)

        observeViewModel()

        viewModel.handleEvent(PointsEvent.OnStart)
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        if(!checkReady()) {
            return false
        }

        marker.showInfoWindow()
        mMap?.animateCamera(CameraUpdateFactory.newLatLng(marker.position), 250, null)
        return true
    }

    override fun onMapLongClick(latLng: LatLng) {
        if(!checkReady()) {
            return
        }
        Timber.d("VIPOLNIS")
        viewModel.handleEvent(PointsEvent.OnLongClickAddMarker(latLng))
    }

    private fun observeViewModel() {
        viewModel.stAnimation.observe(
            viewLifecycleOwner,
            {
                llPbs.visibility = View.VISIBLE
            }
        )
        viewModel.edAnimation.observe(
            viewLifecycleOwner,
            {
                llPbs.visibility = View.INVISIBLE
            }
        )

        viewModel.successTx.observe(viewLifecycleOwner,{
            showSuccessState(it)
        })

        viewModel.listPoints.observe(viewLifecycleOwner,{ point ->
            point?.forEach { _p ->
                //clearMap()
                addMarker(LatLng(_p.latitude,_p.longitude))
            }
        })

        viewModel.point.observe(viewLifecycleOwner, { latLng ->
            addMarker(LatLng(latLng.latitude,latLng.longitude))
        })

        viewModel.errorTx.observe(
            viewLifecycleOwner,
            {
                showErrorState(it)
            }
        )
    }

    private fun checkReady() : Boolean {
        if (mMap == null) {
            requireContext().showToast(MAP_NOT_READY,Toast.LENGTH_SHORT)
            return false
        }
        return true
    }

    private fun clearMap() {
        if(!checkReady()) {
            return
        }
        mMap?.clear()
    }

    private fun addMarker(_p: LatLng) {
        if(!checkReady()) {
            return
        }
        mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(_p.latitude, _p.longitude))
        )
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if(!checkReady()) {
            return
        }

        if (isPermissionGranted()) {
            mMap?.isMyLocationEnabled = true
        } else {
            readLocationPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    @Suppress("DEPRECATED_IDENTITY_EQUALS")
    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
    }

    private fun showErrorState(errorMessage: String) = view?.showSnackbar(errorMessage, Snackbar.LENGTH_SHORT)
    private fun showSuccessState(successMessage: Int) = view?.showSnackbar(successMessage, Snackbar.LENGTH_SHORT)


}