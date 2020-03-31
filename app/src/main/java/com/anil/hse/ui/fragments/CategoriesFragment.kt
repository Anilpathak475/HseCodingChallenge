package com.anil.hse.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anil.hse.R
import com.anil.hse24.base.recyclerview.CellRecyclerAdapter
import com.anil.hse.recyclerviewcell.CategoryCell
import com.anil.hse.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_categories.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var navigation: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.categories.observe(viewLifecycleOwner, Observer {
            val items = it.map { category ->
                CategoryCell(category) { selectedCategory ->
                        navigation = Navigation.findNavController(view)
                        val directions =
                            CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(
                                selectedCategory.categoryId.toString()
                            )
                        navigation.navigate(directions)

                }
            }
            recyclerviewCategories.apply {
                adapter = CellRecyclerAdapter(items)
                layoutManager = LinearLayoutManager(context)
            }
        })
        mainViewModel.fetchCategories()
    }
}
