package yiyo.com.taxiapp.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import yiyo.com.taxiapp.api.VehiclesRepository
import yiyo.com.taxiapp.helpers.plusAssign

class MainViewModel : ViewModel() {

    private val repository by lazy { VehiclesRepository() }
    private val vehicles by lazy { MutableLiveData<List<VehicleItem>>() }
    private val compositeDisposable by lazy { CompositeDisposable() }

    fun loadData() {
        compositeDisposable += repository.getVehicles()
            .map { data -> data.vehicles.map { VehicleItem(it) } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data -> vehicles.value = data }
    }

    fun getVehicles(): LiveData<List<VehicleItem>> = vehicles
}