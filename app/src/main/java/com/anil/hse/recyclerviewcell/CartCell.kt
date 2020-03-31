package com.anil.hse.recyclerviewcell

import android.view.View
import android.view.ViewGroup
import com.anil.hse.R
import com.anil.hse.persistance.entitiy.CartEntity
import com.anil.hse.base.recyclerview.BaseCell
import com.anil.hse.base.recyclerview.LayoutViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_product.view.imageViewProduct
import kotlinx.android.synthetic.main.item_product.view.textViewPrice
import kotlinx.android.synthetic.main.item_product.view.textViewProductName


class CartCell(
    private val cartEntity: CartEntity,
    onRemove: (CartEntity) -> Unit,
    onSelected: (CartEntity) -> Unit,
    onAdded: (CartEntity) -> Unit
) : BaseCell<CartCell.ViewHolder>() {

    override val viewHolderFactory: (ViewGroup) -> ViewHolder = {
        ViewHolder(
            it
        )
    }

    private val removeFromCart = View.OnClickListener {
        onRemove(this.cartEntity)
    }

    private val onSelected = View.OnClickListener {
        onSelected(this.cartEntity)
    }

    private val addToCart = View.OnClickListener {
        onAdded(this.cartEntity)
    }

    override fun onViewAttached(vh: ViewHolder) {
        super.onViewAttached(vh)
        vh.rootView.textViewProductName.text = cartEntity.productName
        vh.rootView.textViewPrice.text = cartEntity.price
        Glide
            .with(vh.rootView)
            .load("https://pic.hse24-dach.net/media/de/products/" + cartEntity.imageUrl + "pics480.jpg")
            .centerCrop()
            .placeholder(R.drawable.loading)
            .into(vh.rootView.imageViewProduct)
        vh.rootView.setOnClickListener(onSelected)
        vh.rootView.textviewAdd.setOnClickListener(addToCart)
        vh.rootView.textViewQuantity.text = cartEntity.quantity.toString()
        vh.rootView.textviewRemove.setOnClickListener(removeFromCart)
    }

    class ViewHolder(parent: ViewGroup) : LayoutViewHolder(parent, R.layout.item_cart)
}
