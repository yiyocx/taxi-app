package yiyo.com.vehiclestest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import yiyo.com.vehiclestest.api.MyTaxiRepository

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyTaxiRepository().getData()
    }
}
