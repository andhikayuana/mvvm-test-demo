package id.yuana.demo.currencyconversion.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import id.yuana.demo.currencyconversion.data.repository.CurrencyLayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

@HiltWorker
class ExchangeRatesUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: CurrencyLayerRepository
) : CoroutineWorker(
    context,
    workerParams
) {

    companion object {
        const val TAG = "ExchangeRatesUpdateWorker"
    }

    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO) {
            val response = repository.getExchangeRates()
            return@withContext if (response.isSuccess()) {
                Result.success()
            } else {
                Result.retry()
            }
        }

    }
}