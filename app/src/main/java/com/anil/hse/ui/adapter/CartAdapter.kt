package com.anil.hse.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anil.hse.R
import com.anil.hse.persistance.entitiy.CartEntity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cart.view.*

class CartAdapter(
    val onRemove: (CartEntity) -> Unit,
    val onSelected: (CartEntity) -> Unit,
    val onAdded: (CartEntity) -> Unit
) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    var cartItems = mutableListOf<CartEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CartViewHolder(layoutInflater.inflate(R.layout.item_cart, parent, false))
    }

    override fun onBindViewHolder(vh: CartViewHolder, position: Int) {
        val cartEntity = cartItems[position]
        vh.itemView.textViewCartProductName.text = cartEntity.productName
        vh.itemView.textViewCartPrice.text = vh.itemView.resources.getString(
            R.string.price,
            cartEntity.price
        )
        Glide
            .with(vh.itemView)
            .load("https://pic.hse24-dach.net/media/de/products/" + cartEntity.imageUrl + "pics480.jpg")
            .centerCrop()
            .placeholder(R.drawable.loading)
            .into(vh.itemView.imageViewCartProduct)
        vh.itemView.setOnClickListener { this.onSelected(cartEntity) }
        vh.itemView.textviewAdd.setOnClickListener { this.onAdded(cartEntity) }
        vh.itemView.textViewQuantity.text = cartEntity.quantity.toString()
        vh.itemView.textviewRemove.setOnClickListener { this.onRemove(cartEntity) }

    }

    override fun getItemCount() = cartItems.size

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}