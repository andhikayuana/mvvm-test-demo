package id.yuana.demo.currencyconversion

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import id.yuana.demo.currencyconversion.worker.ExchangeRatesUpdateWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        setupWorker()
    }

    private fun setupWorker() {
        val exchangeRatesWorkerConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val exchangeRatesWorkRequest =
            PeriodicWorkRequestBuilder<ExchangeRatesUpdateWorker>(30, TimeUnit.MINUTES)
                .setConstraints(exchangeRatesWorkerConstraint)
                .addTag(ExchangeRatesUpdateWorker.TAG)
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                ExchangeRatesUpdateWorker.TAG,
                ExistingPeriodicWorkPolicy.KEEP, exchangeRatesWorkRequest
            )

    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(Log.INFO)
        .build()
}