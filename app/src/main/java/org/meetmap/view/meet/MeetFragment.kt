package org.meetmap.view.meet

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import org.meetmap.R
import org.meetmap.common.MAP_NOT_READY
import org.meetmap.common.requestPermission
import org.meetmap.data.model.domain.Familiar
import org.meetmap.util.showSnackbar
import org.meetmap.util.showToast
import org.meetmap.view.meet.buildMeet.MeetInjector
import timber.log.Timber


class MeetFragment : Fragment(),OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener{

    private lateinit var viewModel: MeetViewModel
    private lateinit var familiar: Familiar
    private var mMap: GoogleMap? = null

    private val args: MeetFragmentArgs by navArgs()

    private val readLocationPermissionResult: ActivityResultLauncher<String> by requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,
        granted = {
            Timber.d("Granted")
        }, denied = {
            Timber.d("Denied")
            requireActivity().finish()
        })

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap?.setOnMarkerClickListener(this)
        mMap?.setOnMapLongClickListener(this)
        enableMyLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.let {
            this.familiar = it.familiar ?: Familiar(0,"","","")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_meet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel = ViewModelProvider(
            this,
            MeetInjector(requireActivity().application).provideMeetViewModelFactory()
        ).get(MeetViewModel::class.java)

        observeViewModel()

        viewModel.handleEvent(MeetEvent.OnStartGetPointsFamiliar(familiar))
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
        viewModel.handleEvent(MeetEvent.OnLongClickAddMarker(latLng))
    }


    private fun observeViewModel() {
        viewModel.msgCollectionReference.observe(viewLifecycleOwner, { it ->
            viewModel.pointsFamiliarGoLiveData(it!!).observe(viewLifecycleOwner, { list ->
                clearMap()
                list.forEach {  lats ->
                    if(lats.emailMyUser == familiar.emailCreator)
                        addMarker(LatLng(lats.latitude,lats.longitude),BitmapDescriptorFactory.HUE_AZURE)
                    else
                        addMarker(LatLng(lats.latitude,lats.longitude),BitmapDescriptorFactory.HUE_BLUE)
                }
            })
        })

        viewModel.errorTx.observe(viewLifecycleOwner, {
            showErrorState(it)
        })
    }

    private fun checkReady() : Boolean {
        if (mMap == null) {
            requireContext().showToast(MAP_NOT_READY, Toast.LENGTH_SHORT)
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

    private fun addMarker(_p: LatLng, bitmapDescriptorFactory: Float) {
        if(!checkReady()) {
            return
        }
        mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(_p.latitude, _p.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(bitmapDescriptorFactory)))
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

}