package com.fatherofapps.androidbase.di

import com.fatherofapps.androidbase.BuildConfig
import com.fatherofapps.androidbase.common.AuthInterceptor
import com.fatherofapps.androidbase.data.apis.*

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

//    @Provides
//    fun provideCustomerAPI(@Named("MainSite") retrofit: Retrofit): CustomerAPI {
//        return retrofit.create(CustomerAPI::class.java)
//    }

    @Provides
    fun providerMainSiteAPI(@Named("AuthSite") retrofit: Retrofit): AuthAPI {
        return retrofit.create(AuthAPI::class.java)
    }

    @Provides
    fun providerDoctorAPI(@Named("MainSite") retrofit: Retrofit): DoctorAPI {
        return retrofit.create(DoctorAPI::class.java)
    }

    @Provides
    fun providerPatientAPI(@Named("MainSite") retrofit: Retrofit): PatientAPI {
        return retrofit.create(PatientAPI::class.java)
    }

    @Provides
    fun providerSpecialistAPI(@Named("MainSite") retrofit: Retrofit): SpecialistAPI {
        return retrofit.create(SpecialistAPI::class.java)
    }

    @Provides
    fun providerPaymentAPI(@Named("MainSite") retrofit: Retrofit): AppointmentAPI {
        return retrofit.create(AppointmentAPI::class.java)
    }

    @Provides
    fun providerSmartCardAPI(@Named("MainSite") retrofit: Retrofit): SmartCardAPI {
        return retrofit.create(SmartCardAPI::class.java)
    }



    @Provides
    @Singleton
    @Named("AuthSite")
    fun provideRetrofitAuthSite(
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {

        return Retrofit.Builder().addConverterFactory(moshiConverterFactory)
            .baseUrl(BuildConfig.BASE_URL)
            .build()
    }


    @Provides
    @Singleton
    @Named("MainSite")
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {

        return Retrofit.Builder().addConverterFactory(moshiConverterFactory)
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Named("FatherOfApps")
    fun provideRetrofitNewYorkTime(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder().addConverterFactory(moshiConverterFactory)
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        interceptor: AuthInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES).build()
    }


    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return MoshiConverterFactory.create(moshi)
    }

}