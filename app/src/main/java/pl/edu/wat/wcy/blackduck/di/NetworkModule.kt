package pl.edu.wat.wcy.blackduck.di

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.edu.wat.wcy.blackduck.BuildConfig
import pl.edu.wat.wcy.blackduck.data.network.Configuration
import pl.edu.wat.wcy.blackduck.data.network.PortalApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@Module
object NetworkModule {

    @Provides
    @JvmStatic
    @Reusable
    fun provideApiService(retrofit: Retrofit): PortalApi = retrofit.create(PortalApi::class.java)

    @Provides
    @JvmStatic
    @Reusable
    fun provideRetrofitInterface(): Retrofit {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Configuration.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(client)
            .build()
    }

}

