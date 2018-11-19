package yiyo.com.taxiapp.items

import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.databinding.BindableItem
import yiyo.com.taxiapp.R
import yiyo.com.taxiapp.databinding.ItemExpandableHeaderBinding

class ExpandableHeaderItem(private val title: String, private val size: Int) :
    BindableItem<ItemExpandableHeaderBinding>(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewBinding: ItemExpandableHeaderBinding, position: Int) {
        viewBinding.title = "${title.toLowerCase().capitalize()} ($size)"
        viewBinding.root.setOnClickListener {
            expandableGroup.onToggleExpanded()
            viewBinding.imageViewIcon.setImageResource(getToggleIcon())
        }
    }

    override fun getLayout(): Int = R.layout.item_expandable_header

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun getToggleIcon(): Int {
        return if (expandableGroup.isExpanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
    }
}