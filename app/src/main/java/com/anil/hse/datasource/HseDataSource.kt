package com.anil.hse.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.anil.hse.model.product.Product
import com.anil.hse.networking.ApiResponse
import com.anil.hse.networking.HseClient

enum class State {
    DONE, LOADING, ERROR
}

class HseDataSource(
    private val hseClient: HseClient
) : PageKeyedDataSource<Int, Product>() {

    var state: MutableLiveData<State> = MutableLiveData()
    var catId: String = ""
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Product>
    ) {
        updateState(State.LOADING)
        hseClient.fetchProductByCategory(catId, 1, params.requestedLoadSize) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    response.data?.let {
                        updateState(State.DONE)
                        callback.onResult(
                            response.data.products,
                            null,
                            2
                        )
                    }
                }
                is ApiResponse.Failure.Error -> {
                    updateState(State.ERROR)
                }
                is ApiResponse.Failure.Exception -> {
                    updateState(State.ERROR)
                }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        updateState(State.LOADING)
        hseClient.fetchProductByCategory(catId, params.key, params.requestedLoadSize) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    response.data?.let {
                        updateState(State.DONE)
                        callback.onResult(
                            it.products,
                            params.key + 1
                        )
                    }
                }
                is ApiResponse.Failure.Error -> {
                    updateState(State.ERROR)
                }
                is ApiResponse.Failure.Exception -> {
                    updateState(State.ERROR)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }
}