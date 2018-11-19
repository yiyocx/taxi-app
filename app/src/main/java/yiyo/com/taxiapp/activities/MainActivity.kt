package yiyo.com.taxiapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import yiyo.com.taxiapp.R
import yiyo.com.taxiapp.api.MyTaxiApi.Companion.HAMBURG_1
import yiyo.com.taxiapp.api.MyTaxiApi.Companion.HAMBURG_2
import yiyo.com.taxiapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val width by lazy { resources.displayMetrics.widthPixels }
    private val height by lazy { resources.displayMetrics.heightPixels }
    private val mapPadding by lazy { (width * 0.12).toInt() }

    private val binding by lazy { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }
    private val adapter = GroupAdapter<ViewHolder>().apply { spanCount = 2 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        initRecyclerView()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getVehicles().observe(this, Observer { vehicles -> showData(vehicles) })
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, adapter.spanCount)

        with(binding.bottomSheet.recyclerView) {
            this.layoutManager = layoutManager
            adapter = this@MainActivity.adapter
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        binding.viewModel?.loadData()

        // Move the camera to Hamburg, Germany
        val bounds = LatLngBounds.Builder()
            .include(HAMBURG_1)
            .include(HAMBURG_2)
            .build()
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, mapPadding))
    }

    private fun showData(vehicles: List<VehicleItem>) {
        adapter.clear()
        map.clear()

        adapter.addAll(vehicles)
        vehicles.asSequence()
            .map { MarkerOptions().position(it.vehicle.coordinate).title(it.vehicle.fleetType) }
            .forEach { map.addMarker(it) }
    }
}
