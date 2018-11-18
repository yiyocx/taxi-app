package yiyo.com.taxiapp.api

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MyTaxiRepository {

    fun getData() {
        val service = RetrofitBuilder.createService(MyTaxiApi::class.java)
        service.getCars()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                println(data)
            }
    }
}