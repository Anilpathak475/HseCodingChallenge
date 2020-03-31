package com.anil.hse.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anil.hse.R
import com.anil.hse.persistance.entitiy.CartEntity
import com.anil.hse.recyclerviewcell.CartCell
import com.anil.hse.viewmodel.CartViewModel
import com.anil.hse24.base.recyclerview.CellRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_cart.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {

    private val cartViewModel: CartViewModel by viewModel()
    private lateinit var navigation: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel.cart.observe(viewLifecycleOwner, Observer {
            val items = it.map { cart ->
                CartCell(cart,
                    onSelected = { select -> this.onSelected(select) },
                    onRemove = { remove -> onRemove(remove) },
                    onAdded = { add -> onAdded(add) })
            }
            recyclerViewCartItems.apply {
                adapter = CellRecyclerAdapter(items)
                layoutManager = LinearLayoutManager(context)
            }
            val total = it.map { it.price.toDouble() }.sumByDouble { it }
            textViewTotalAmount.text = total.toString()
        })
    }

    private fun onAdded(cartEntity: CartEntity) {
        cartEntity.quantity = cartEntity.quantity + 1
        cartViewModel.updateCart(cartEntity)
    }

    private fun onRemove(cartEntity: CartEntity) {
        cartEntity.quantity = cartEntity.quantity - 1
        cartViewModel.updateCart(cartEntity)

    }

    private fun onSelected(cartEntity: CartEntity) {
        //val direction= CartFragment
    }
}
