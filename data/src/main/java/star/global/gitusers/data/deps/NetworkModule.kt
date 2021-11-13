package star.global.gitusers.data.deps

import star.global.gitusers.data.remote.RemoteSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

internal const val API_KEY_CONFIG = "DATA_PROVIDER_API_CONFIG"
private const val URL_CONFIG = "DATA_PROVIDER_URL_CONFIG"

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideGsonBuilder(): GsonBuilder = GsonBuilder()

    @Provides
    @Singleton
    fun provideGson(builder: GsonBuilder): Gson = builder.create()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named(URL_CONFIG)
    fun provideBaseUrl(dataConfigs: DataConfigs): String = dataConfigs.baseUrl

    @Provides
    @Singleton
    @Named(API_KEY_CONFIG)
    fun provideApiKey(dataConfigs: DataConfigs): String = dataConfigs.apiKey

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named(URL_CONFIG) baseUrl: String,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideRemoteSource(retrofit: Retrofit): RemoteSource {
        return retrofit.create(RemoteSource::class.java)
    }

}