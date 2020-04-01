package com.anil.hse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.anil.hse.datasource.HseDataSourceFactory
import com.anil.hse.model.product.Product
import com.anil.hse.networking.HseClient
import com.anil.hse.persistance.CartDao
import com.anil.hse.persistance.entitiy.CartEntity
import com.anil.hse.viewmodel.ProductsViewModel
import com.github.harmittaa.koinexample.model.TempData
import com.github.harmittaa.koinexample.model.Weather
import com.github.harmittaa.koinexample.networking.Resource
import com.github.harmittaa.koinexample.repository.WeatherRepository
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock


@RunWith(JUnit4::class)
class ProductsViewModelTest {
    private lateinit var viewModel: ProductsViewModel
    @Mock
    lateinit var hseClient: HseClient
    @Mock
    lateinit var cartDao: CartDao
    @Mock
    lateinit var products: LiveData<List<Product>>
    @Mock
    lateinit var product: LiveData<Product>
    @Mock
    lateinit var cart: LiveData<List<CartEntity>>
    @Mock
    lateinit var hseDataSourceFactory: HseDataSourceFactory

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = ProductsViewModel(hseClient, cartDao, hseDataSourceFactory)
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when getWeather is called with valid location, then observer is updated with success`() =
        runBlocking {
            viewModel.weather.observeForever(weatherObserver)
            viewModel.getWeather(validLocation)
            delay(10)
            verify(weatherRepository).getWeather(validLocation)
            verify(weatherObserver, timeout(50)).onChanged(Resource.loading(null))
            verify(weatherObserver, timeout(50)).onChanged(successResource)
        }

    @Test
    fun `when getWeather is called with invalid location, then observer is updated with failure`() =
        runBlocking {
            viewModel.weather.observeForever(weatherObserver)
            viewModel.getWeather(invalidLocation)
            delay(10)
            verify(weatherRepository).getWeather(invalidLocation)
            verify(weatherObserver, timeout(50)).onChanged(Resource.loading(null))
            verify(weatherObserver, timeout(50)).onChanged(errorResource)
        }
}