package id.yuana.demo.currencyconversion.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.yuana.demo.currencyconversion.R
import id.yuana.demo.currencyconversion.data.model.ConvertedRate
import kotlinx.android.synthetic.main.item_exchange_rates.view.*

class ExchangeRateAdapter() : RecyclerView.Adapter<ExchangeRateAdapter.ViewHolder>() {

    var exchangeRates: List<ConvertedRate> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exchange_rates, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(exchangeRates[position])
    }

    override fun getItemCount(): Int = exchangeRates.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindView(convertedRate: ConvertedRate) {
            with(itemView) {
                tvLabel.text = convertedRate.currency.label
                tvCode.text = convertedRate.currency.code
                tvRate.text =
                    "1USD = ${"%.2f".format(convertedRate.rate)}${convertedRate.currency.code}"
                tvValue.text = "%.2f".format(convertedRate.value)
            }
        }


    }
}