package com.anil.hse.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.anil.hse.R
import com.anil.hse.model.product.Product
import com.anil.hse.recyclerviewcell.ProductCell
import com.anil.hse.viewmodel.MainViewModel
import com.anil.hse.base.recyclerview.CellRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_products.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var navigation: NavController

    private val categoryId by lazy {
        arguments?.let { ProductsFragmentArgs.fromBundle(it).categoryId }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigation = Navigation.findNavController(view)
        mainViewModel.products.observe(viewLifecycleOwner, Observer {
            val items = it.map { product ->
                ProductCell(product = product,
                    onSelected = { selectedCategory -> this.onProductDetail(selectedCategory) },
                    addToCart = { addProductCart -> onProductAddToCart(addProductCart) },
                    removeFromCart = { removeProductCart -> onProductRemoveFromCart(removeProductCart)},
                    presentInDb = mainViewModel.isProductPresentInCart(product))
            }
            recyclerviewProducts.apply {
                adapter = CellRecyclerAdapter(items)

                layoutManager = LinearLayoutManager(context)
            }
        })
        mainViewModel.cart.observe(viewLifecycleOwner, Observer {
            val quantity = it.map { cartEntity -> cartEntity.quantity }.sum()
            textViewCartItems.text = quantity.toString()
        })
        categoryId?.let { mainViewModel.fetchProductsByCat(it) }

        layoutCart.setOnClickListener { navigation.navigate(ProductsFragmentDirections.actionProductsFragmentToCartFragment()) }
    }

    private fun onProductDetail(product: Product) {
        val directions =
            ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
                product.sku
            )
        navigation.navigate(directions)
    }

    private fun onProductAddToCart(product: Product) {
        Log.d("Adding in cart", product.nameShort)
        mainViewModel.addItemInCart(product, 1)
    }

    private fun onProductRemoveFromCart(product: Product) {
        Log.d("Removing from cart", product.nameShort)
        mainViewModel.removeItemFromCart(product)
    }
}
