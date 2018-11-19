package yiyo.com.taxiapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import yiyo.com.taxiapp.api.VehiclesRepository
import yiyo.com.taxiapp.helpers.plusAssign
import yiyo.com.taxiapp.items.VehicleItem

class MainViewModel : ViewModel() {

    private val repository by lazy { VehiclesRepository() }
    private val groupedVehicles by lazy { MutableLiveData<Map<String, List<VehicleItem>>>() }
    private val compositeDisposable by lazy { CompositeDisposable() }

    fun loadData() {
        compositeDisposable += repository.getVehicles()
            .map { data -> data.vehicles.map { VehicleItem(it) }.groupBy { it.vehicle.fleetType } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                groupedVehicles.value = data
            }
    }

    fun groupedVehicles(): LiveData<Map<String, List<VehicleItem>>> = groupedVehicles
}