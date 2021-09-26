package com.transport.mall.ui.addnewdhaba

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.transport.mall.R
import com.transport.mall.databinding.ActivityGoogleMapsBinding
import com.transport.mall.model.LocationAddressModel
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.GlobalUtils.getCurrentLocation
import com.transport.mall.utils.common.GlobalUtils.refreshLocation


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class GoogleMapsActivity : BaseActivity<ActivityGoogleMapsBinding, BaseVM>(), OnMapReadyCallback {
    override val binding: ActivityGoogleMapsBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_google_maps
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(application))
        set(value) {}
    override val context: Context
        get() = this

    lateinit var selectedAddress: LocationAddressModel

    companion object {
        val REQUEST_CODE_MAP = 789
        fun start(fragment: Fragment) {
            val intent = Intent(fragment.activity, GoogleMapsActivity::class.java)
            fragment.startActivityForResult(intent, REQUEST_CODE_MAP)
        }
    }

    override fun bindData() {
        binding.context = this
        setupToolbar()
        refreshLocation(this)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setTitle(getString(R.string.choose_location))
    }

    override fun initListeners() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnCancel.setOnClickListener {
            finish()
        }
        binding.btnContinue.setOnClickListener {
            val intent = Intent()
            intent.putExtra("data", selectedAddress)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        getCurrentLocation(this, GenericCallBack {
            if (it != null) {
                showAddress(LatLng(it.latitude, it.longitude), googleMap)
            } else {
                //by default show new delhi
                showAddress(LatLng(28.6139, 77.2090), googleMap)
            }
        })
        googleMap.setOnMapClickListener {
            showAddress(it, googleMap)
        }
    }

    private fun showAddress(
        it: LatLng,
        mMap: GoogleMap
    ) {
        selectedAddress = GlobalUtils.getAddressUsingLatLong(this, it.latitude, it.longitude)
        binding.tvSelectedLocation.text = selectedAddress.fullAddress
        binding.tvSelectedLocation.visibility = View.VISIBLE

        mMap.clear()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(it.latitude, it.longitude),
                12.0f
            )
        )
        mMap.addMarker(
            MarkerOptions()
                .position(LatLng(it.latitude, it.longitude))
                .title(selectedAddress.fullAddress)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}