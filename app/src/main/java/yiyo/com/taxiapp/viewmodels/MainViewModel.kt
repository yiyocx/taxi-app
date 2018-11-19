package yiyo.com.taxiapp.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import yiyo.com.taxiapp.api.VehiclesRepository
import yiyo.com.taxiapp.helpers.plusAssign
import yiyo.com.taxiapp.items.VehicleItem
import yiyo.com.taxiapp.models.Vehicle.Companion.POOLING
import yiyo.com.taxiapp.models.Vehicle.Companion.TAXI

class MainViewModel : ViewModel(), OnItemClickListener {

    private val repository by lazy { VehiclesRepository() }
    private val groupedVehicles by lazy { MutableLiveData<Map<String, List<VehicleItem>>>() }
    private val taxiVehicles by lazy { MutableLiveData<List<VehicleItem>>() }
    private val poolingVehicles by lazy { MutableLiveData<List<VehicleItem>>() }
    private val compositeDisposable by lazy { CompositeDisposable() }

    fun loadData() {
        compositeDisposable += repository.getVehicles()
            .map { data -> data.vehicles.map { VehicleItem(it) }.groupBy { it.vehicle.fleetType } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                groupedVehicles.value = data
                taxiVehicles.value = data[TAXI]
                poolingVehicles.value = data[POOLING]
            }
    }

    fun groupedVehicles(): LiveData<Map<String, List<VehicleItem>>> = groupedVehicles

    fun taxiVehicles(): LiveData<List<VehicleItem>> = taxiVehicles

    fun poolingVehicles(): LiveData<List<VehicleItem>> = poolingVehicles

    override fun onItemClick(item: Item<*>, view: View) {
        if (item is VehicleItem) {
            taxiVehicles.value?.let {
                val newList = it.map { vehicle -> vehicle.copy(isSelected = (vehicle == item) && !item.isSelected) }
                taxiVehicles.value = newList
            }
            poolingVehicles.value?.let {
                val newList = it.map { vehicle -> vehicle.copy(isSelected = (vehicle == item) && !item.isSelected) }
                poolingVehicles.value = newList
            }
        }
    }
}