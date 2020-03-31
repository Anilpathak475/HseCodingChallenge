package com.anil.hse.recyclerviewcell

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.anil.hse.R
import com.anil.hse.model.product.Product
import com.anil.hse.base.recyclerview.BaseCell
import com.anil.hse.base.recyclerview.LayoutViewHolder
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product.view.*


class ProductCell(
    private val product: Product,
    onSelected: (Product) -> Unit,
    val addToCart: (Product) -> Unit,
    val removeFromCart: (Product) -> Unit,
    val presentInDb: Boolean
) : BaseCell<ProductCell.ViewHolder>() {

    private var buttonState = if(presentInDb) ButtonState.REMOVE else ButtonState.ADD
    private lateinit var buttonAddRemove: Button

    override val viewHolderFactory: (ViewGroup) -> ViewHolder = { ViewHolder(it) }

    private val selectListener = View.OnClickListener {
        onSelected(this.product)
    }

    private enum class ButtonState(val buttonText: String) {
        ADD("Add to cart"), REMOVE("Remove from cart")
    }


    private val addRemoveFromCart = View.OnClickListener {
        if (buttonState == ButtonState.REMOVE) {
            removeFromCart(this.product)
            buttonState = ButtonState.ADD
        }
        else {
            addToCart(this.product)
            buttonState = ButtonState.REMOVE
        }
        buttonAddRemove.text = buttonState.buttonText
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
        this.buttonAddRemove = vh.rootView.buttonAddRemove
        this.buttonAddRemove.text = buttonState.buttonText
        this.buttonAddRemove.setOnClickListener(addRemoveFromCart)
    }

    class ViewHolder(parent: ViewGroup) : LayoutViewHolder(parent, R.layout.item_product)
}
