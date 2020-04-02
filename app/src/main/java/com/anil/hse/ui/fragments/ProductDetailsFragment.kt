package com.anil.hse.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.anil.hse.R
import com.anil.hse.model.product.Product
import com.anil.hse.networking.Resource
import com.anil.hse.networking.Status
import com.anil.hse.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.fragment_product_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsFragment : Fragment() {

    private val productsViewModel: ProductsViewModel by viewModel()

    private val productId by lazy {
        arguments?.let { ProductDetailsFragmentArgs.fromBundle(it).productId }
    }

    private val observer = Observer<Resource<Product>> {
        when (it.status) {
            Status.SUCCESS -> it?.let {
                it.data?.let { product ->
                    productName.text = product.title
                }
            }
            Status.ERROR -> it.message?.let { error -> showError(error) }
            Status.LOADING -> showLoading()
        }
    }

    private fun showError(error: String) =
        Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()

    private fun showLoading() {

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
        productsViewModel.product.observe(viewLifecycleOwner, observer)
    }

}
