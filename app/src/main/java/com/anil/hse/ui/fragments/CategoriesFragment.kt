package com.anil.hse.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anil.hse.R
import com.anil.hse.ui.adapter.CategoryAdapter
import com.anil.hse.viewmodel.CategoryViewMode
import kotlinx.android.synthetic.main.fragment_categories.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFragment : Fragment() {
    private val categoryViewMode: CategoryViewMode by viewModel()
    private val adapter by lazy {
        CategoryAdapter { onCategorySelected(it.categoryId.toString()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_categories, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewMode.categories.observe(viewLifecycleOwner, Observer {
            adapter.categories = it
            adapter.notifyDataSetChanged()
        })

        recyclerviewCategories.apply {
            adapter = this@CategoriesFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
        categoryViewMode.fetchCategories()
    }

    private fun onCategorySelected(categoryId: String) {
        val directions =
            CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(
                categoryId
            )
        findNavController().navigate(directions)
    }
}
