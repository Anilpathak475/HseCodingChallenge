package com.anil.hse.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.anil.hse.R
import com.anil.hse.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.fragment_product_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsFragment : Fragment() {

    private val productsViewModel: ProductsViewModel by viewModel()

    private val productId by lazy {
        arguments?.let { ProductDetailsFragmentArgs.fromBundle(it).productId }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productId?.let { productsViewModel.fetchProductProductDetail(it) }
        productsViewModel.product.observe(viewLifecycleOwner, Observer {
            productName.text = it.title
        })
    }

}
