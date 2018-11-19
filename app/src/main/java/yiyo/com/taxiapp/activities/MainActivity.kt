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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import yiyo.com.taxiapp.R
import yiyo.com.taxiapp.api.MyTaxiApi.Companion.HAMBURG_1
import yiyo.com.taxiapp.api.MyTaxiApi.Companion.HAMBURG_2
import yiyo.com.taxiapp.databinding.ActivityMainBinding
import yiyo.com.taxiapp.items.ExpandableHeaderItem
import yiyo.com.taxiapp.items.VehicleItem
import yiyo.com.taxiapp.items.VehicleItem.Companion.SPANS
import yiyo.com.taxiapp.models.Vehicle.Companion.POOLING
import yiyo.com.taxiapp.models.Vehicle.Companion.TAXI
import yiyo.com.taxiapp.viewmodels.MainViewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val width by lazy { resources.displayMetrics.widthPixels }
    private val height by lazy { resources.displayMetrics.heightPixels }
    private val mapPadding by lazy { (width * 0.12).toInt() }
    private val markerIcon by lazy { BitmapDescriptorFactory.fromResource(R.drawable.taxi_marker) }

    private val binding by lazy { DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main) }
    private val adapter = GroupAdapter<ViewHolder>().apply { spanCount = SPANS }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        initRecyclerView()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.groupedVehicles().observe(this, Observer { vehicles -> showData(vehicles) })
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, adapter.spanCount)
        layoutManager.spanSizeLookup = adapter.spanSizeLookup

        with(binding.bottomSheet.recyclerView) {
            this.layoutManager = layoutManager
            adapter = this@MainActivity.adapter
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Move the camera to Hamburg, Germany
        val bounds = LatLngBounds.Builder()
            .include(HAMBURG_1)
            .include(HAMBURG_2)
            .build()
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, mapPadding))

        binding.viewModel?.loadData()
    }

    private fun showData(vehiclesMap: Map<String, List<VehicleItem>>) {
        adapter.clear()
        map.clear()

        val taxiVehicles = vehiclesMap[TAXI]
        taxiVehicles?.let { taxis ->
            val header = ExpandableHeaderItem(TAXI, taxis.size)
            val expandableGroup = ExpandableGroup(header, true)
            expandableGroup.add(Section(taxis))
            adapter.add(expandableGroup)
        }

        val poolingVehicles = vehiclesMap[POOLING]
        poolingVehicles?.let { pool ->
            val header = ExpandableHeaderItem(POOLING, pool.size)
            val expandableGroup = ExpandableGroup(header, true)
            expandableGroup.add(Section(pool))
            adapter.add(expandableGroup)
        }

        vehiclesMap.values
            .flatten()
            .asSequence()
            .map {
                MarkerOptions()
                    .position(it.vehicle.coordinate)
                    .title(it.vehicle.fleetType)
                    .icon(markerIcon)
            }
            .forEach { map.addMarker(it) }
    }
}
