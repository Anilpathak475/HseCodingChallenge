package com.anil.hse.recyclerviewcell

import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.anil.hse.R
import com.anil.hse.model.product.Product
import com.anil.hse24.base.recyclerview.BaseCell
import com.anil.hse24.base.recyclerview.LayoutViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product.view.*


class ProductCell(
    private val product: Product,
    onSelected: (Product) -> Unit,
    addToCart: (Product) -> Unit
) : BaseCell<ProductCell.ViewHolder>() {

    override val viewHolderFactory: (ViewGroup) -> ViewHolder = { ViewHolder(it) }

    private val selectListener = View.OnClickListener {
        onSelected(this.product)
    }

    private val addToCart = View.OnClickListener {
        addToCart(this.product)
    }

    override fun onViewAttached(vh: ViewHolder) {
        super.onViewAttached(vh)
        vh.rootView.textViewProductName.text = product.nameShort
        vh.rootView.textViewPrice.text = product.productPrice.price.toString()

        Glide
            .with(vh.rootView)
            .load("https://pic.hse24-dach.net/media/de/products/" + product.imageUris.first() + "pics480.jpg")
            .centerCrop()
            .placeholder(R.drawable.loading)
            .into(vh.rootView.imageViewProduct)
        vh.rootView.setOnClickListener(selectListener)
        vh.rootView.buttonAddToCart.setOnClickListener(addToCart)
    }

    class ViewHolder(parent: ViewGroup) : LayoutViewHolder(parent, R.layout.item_product)
}
