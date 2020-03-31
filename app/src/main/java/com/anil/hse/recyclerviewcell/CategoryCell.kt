package com.anil.hse.recyclerviewcell

import android.view.View
import android.view.ViewGroup
import com.anil.hse.R
import com.anil.hse24.base.recyclerview.BaseCell
import com.anil.hse24.base.recyclerview.LayoutViewHolder
import com.anil.hse.model.category.Category
import kotlinx.android.synthetic.main.item_category.view.*


class CategoryCell(
    private val category: Category,
    onSelected: (Category) -> Unit
) : BaseCell<CategoryCell.ViewHolder>() {

    override val viewHolderFactory: (ViewGroup) -> ViewHolder = { ViewHolder(it) }

    private val selectListener = View.OnClickListener {
        onSelected(this.category)
    }

    override fun onViewAttached(vh: ViewHolder) {
        super.onViewAttached(vh)
        vh.rootView.textViewCategoryName.text = category.displayName
        vh.rootView.setOnClickListener(selectListener)
    }

    class ViewHolder(parent: ViewGroup) : LayoutViewHolder(parent, R.layout.item_category)
}
