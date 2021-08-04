package id.yuana.demo.currencyconversion.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.yuana.demo.currencyconversion.BuildConfig
import id.yuana.demo.currencyconversion.data.ExchangeRateConverter
import id.yuana.demo.currencyconversion.data.local.Cache
import id.yuana.demo.currencyconversion.data.remote.CurrencyLayerApi
import id.yuana.demo.currencyconversion.data.repository.CurrencyLayerRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.CURRENCY_LAYER_BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideCurrencyLayerApi(retrofit: Retrofit) = retrofit.create(CurrencyLayerApi::class.java)

    @Provides
    @Singleton
    fun provideCurrencyLayerRepository(
        api: CurrencyLayerApi,
        cache: Cache
    ) = CurrencyLayerRepository(api, cache)

    @Provides
    @Singleton
    fun provideExchangeRateConverter() = ExchangeRateConverter()

}