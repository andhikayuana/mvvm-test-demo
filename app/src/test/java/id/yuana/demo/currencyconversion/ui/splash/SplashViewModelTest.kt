package id.yuana.demo.currencyconversion.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.yuana.demo.currencyconversion.data.repository.CurrencyLayerRepository
import id.yuana.demo.currencyconversion.util.CoroutinesTestRule
import id.yuana.demo.currencyconversion.util.Resource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @get:Rule
    val testCoroutineRule = CoroutinesTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: CurrencyLayerRepository
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        repository = mockk()
        viewModel = SplashViewModel(repository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `when splash open up then should loading and success`() {
        testCoroutineRule.runBlockingTest {
            //given
            val loadingObserver: Observer<Boolean> = mockk()
            val onSuccess: () -> Unit = mockk()
            val onError: (message: String?) -> Unit = mockk()

            coEvery { repository.getSupportedCurrenciesCached() } returns emptyList()
            coEvery { repository.getSupportedCurrencies() } returns Resource.success(emptyMap())
            every { loadingObserver.onChanged(any()) } just Runs
            every { onSuccess() } returns Unit

            //when
            viewModel.fetchSupportredCurrencies(onSuccess, onError)
            viewModel.loading.observeForever(loadingObserver)
            advanceTimeBy(3000)

            //then
            coVerifyOrder {
                repository.getSupportedCurrenciesCached().isEmpty()
                repository.getSupportedCurrencies()
            }
            verifyOrder {
                loadingObserver.onChanged(true) //loading true
                loadingObserver.onChanged(false) //loading false
                onSuccess()
            }
            viewModel.loading.removeObserver(loadingObserver)

        }
    }

    @Test
    fun `when splash open up then should loading and error`() {
        testCoroutineRule.runBlockingTest {
            //given
            val loadingObserver: Observer<Boolean> = mockk()
            val onSuccess: () -> Unit = mockk()
            val onError: (message: String?) -> Unit = mockk(relaxed = true)
            val errorMessage = "Oops something went wrong"

            coEvery { repository.getSupportedCurrenciesCached() } returns emptyList()
            coEvery { repository.getSupportedCurrencies() } returns Resource.error(
                msg = errorMessage,
                data = null
            )
            every { loadingObserver.onChanged(any()) } just Runs
            every { onSuccess() } returns Unit

            //when
            viewModel.fetchSupportredCurrencies(onSuccess, onError)
            viewModel.loading.observeForever(loadingObserver)
            advanceTimeBy(3000)

            //then
            coVerifyOrder {
                repository.getSupportedCurrenciesCached()
                repository.getSupportedCurrencies()
            }
            verifyOrder {
                loadingObserver.onChanged(true) //loading true
                onError(errorMessage)
                loadingObserver.onChanged(false) //loading false
            }
            viewModel.loading.removeObserver(loadingObserver)
        }
    }


}