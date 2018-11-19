package yiyo.com.taxiapp.activities

import com.xwray.groupie.databinding.BindableItem
import yiyo.com.taxiapp.R
import yiyo.com.taxiapp.databinding.ItemVehicleBinding
import yiyo.com.taxiapp.models.Vehicle

class VehicleItem(val vehicle: Vehicle) : BindableItem<ItemVehicleBinding>() {

    override fun bind(viewBinding: ItemVehicleBinding, position: Int) {
        viewBinding.vehicle = vehicle
    }

    override fun getLayout(): Int = R.layout.item_vehicle
}