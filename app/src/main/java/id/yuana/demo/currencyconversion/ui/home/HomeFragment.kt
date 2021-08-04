package id.yuana.demo.currencyconversion.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.yuana.demo.currencyconversion.R
import id.yuana.demo.currencyconversion.data.model.Currency
import id.yuana.demo.currencyconversion.util.Status
import kotlinx.android.synthetic.main.home_content.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {

    private lateinit var _currencySelectorAdapter: ArrayAdapter<Currency>
    private lateinit var _exchangeRateAdapter: ExchangeRateAdapter
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserver()
        initDefaultData()

    }

    private fun initDefaultData() {
        val defaultCurrency = viewModel.selectedCurrency.value!!
        etCurrency.setText(defaultCurrency.toString())
    }

    private fun initView() {
        etCurrency.setOnItemClickListener { adapterView, _, i, _ ->
            val selectedCurrency = adapterView.adapter.getItem(i) as Currency
            viewModel.selectedCurrency.value = selectedCurrency

            val amount = etAmount.text.toString().toFloat()
            viewModel.amount.value = amount

            _currencySelectorAdapter.filter.filter("")
        }
        etAmount.addTextChangedListener { amount ->
            viewModel.viewModelScope.launch {
                delay(500)
                viewModel.amount.value = amount.toString().toFloatOrNull() ?: 0F
            }
        }
        _exchangeRateAdapter = ExchangeRateAdapter()
        rvExchangeRates.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = _exchangeRateAdapter
        }

    }

    private fun initObserver() {
        viewModel.supportedCurrencies.observe(viewLifecycleOwner, Observer {
            _currencySelectorAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line, it
            )
            etCurrency.setAdapter(_currencySelectorAdapter)
        })
        viewModel.convertedRates.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    topAppBar.title = getString(R.string.label_loading)
                }
                Status.SUCCESS -> {
                    topAppBar.title = getString(R.string.app_name)
                    it.data?.let { data ->
                        _exchangeRateAdapter.apply {
                            exchangeRates = data
                            notifyDataSetChanged()
                        }
                    }

                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.onInputChanged.observe(viewLifecycleOwner, Observer {
            //do nothing
        })

    }

}