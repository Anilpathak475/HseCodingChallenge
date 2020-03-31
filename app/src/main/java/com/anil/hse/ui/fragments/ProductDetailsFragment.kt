package com.anil.hse.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.anil.hse.R
import com.anil.hse.recyclerviewcell.ProductCell
import com.anil.hse.viewmodel.MainViewModel
import com.anil.hse24.base.recyclerview.CellRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.fragment_products.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var navigation: NavController

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
        navigation = Navigation.findNavController(view)
        mainViewModel.product.observe(viewLifecycleOwner, Observer {
            productName.text = it.title
        }
        )
        productId?.let { mainViewModel.fetchProductProductDetail(it) }
    }

}
