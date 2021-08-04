package id.yuana.demo.currencyconversion.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.yuana.demo.currencyconversion.R
import kotlinx.android.synthetic.main.fragment_splash.*

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchSupportredCurrencies({
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }, { message ->
            tvSplashLabel.text = message
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            tvSplashLabel.text = getString(if (it) R.string.label_loading else R.string.app_name)
        })

    }
}