package world.respect

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import okhttp3.OkHttpClient
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RespectApp : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RespectApp)
            modules(appKoinModule)
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        val okHttpClient: OkHttpClient = get()
        return ImageLoader.Builder(applicationContext)
            .components {
                //As per https://coil-kt.github.io/coil/network/#using-a-custom-okhttpclient
                add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient } ))
            }
            .crossfade(true)
            .build()
    }

}
