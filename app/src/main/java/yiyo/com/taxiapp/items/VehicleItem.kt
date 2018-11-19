package yiyo.com.taxiapp.items

import com.xwray.groupie.databinding.BindableItem
import yiyo.com.taxiapp.R
import yiyo.com.taxiapp.databinding.ItemVehicleBinding
import yiyo.com.taxiapp.models.Vehicle
import yiyo.com.taxiapp.models.Vehicle.Companion.TAXI

class VehicleItem(val vehicle: Vehicle) : BindableItem<ItemVehicleBinding>() {

    override fun bind(viewBinding: ItemVehicleBinding, position: Int) {
        viewBinding.vehicle = vehicle
        if (vehicle.fleetType == TAXI) {
            viewBinding.imageViewLogo.setImageResource(R.drawable.taxi)
        } else {
            viewBinding.imageViewLogo.setImageResource(R.drawable.pooling)
        }
    }

    override fun getLayout(): Int = R.layout.item_vehicle

    override fun getSpanSize(spanCount: Int, position: Int): Int = spanCount / SPANS

    companion object {
        const val SPANS = 2
    }
}