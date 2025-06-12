package world.respect.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import world.respect.domain.getfavicons.GetFavIconUseCase
import world.respect.domain.getfavicons.GetFavIconsUseCaseImpl
import world.respect.domain.opds.validator.OpdsFeedValidator
import world.respect.domain.opds.validator.OpdsPublicationValidator
import world.respect.domain.opds.validator.ValidateLinkUseCaseImpl
import world.respect.domain.opds.validator.ValidateOpdsPublicationUseCase
import world.respect.domain.respectappmanifest.validator.RespectAppManifestValidator
import world.respect.domain.validator.ValidateHttpResponseForUrlUseCase
import world.respect.domain.validator.ValidateLinkUseCase

val JvmCoreDiMOdule = DI.Module("RespectJvmCore") {
    bind<Json>() with singleton {
        Json {
            encodeDefaults = false
            ignoreUnknownKeys = true
        }
    }

    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder()
        .dispatcher(
            Dispatcher().also {
                it.maxRequests = 30
                it.maxRequestsPerHost = 10
            }
        ).build()
    }

    bind<HttpClient>() with singleton {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(json = instance())
            }
            engine {
                preconfigured = instance()
            }
        }
    }

    bind<ValidateHttpResponseForUrlUseCase>() with singleton {
        ValidateHttpResponseForUrlUseCase(
            httpClient = instance()
        )
    }

    bind<ValidateOpdsPublicationUseCase>() with singleton {
        ValidateOpdsPublicationUseCase(
            validateHttpResponseForUrlUseCase = instance()
        )
    }

    bind<OpdsFeedValidator>() with singleton {
        OpdsFeedValidator(
            json = instance(),
            httpClient = instance(),
            validateOpdsPublicationUseCase = instance()
        )
    }

    bind<OpdsPublicationValidator>() with singleton {
        OpdsPublicationValidator(
            httpClient = instance()
        )
    }

    bind<RespectAppManifestValidator>() with singleton {
        RespectAppManifestValidator(
            json = instance(),
            validateHttpResponseForUrlUseCase = instance(),
            getFavIconUseCase = instance(),
            httpClient = instance(),
        )
    }

    bind<ValidateLinkUseCase>() with singleton {
        ValidateLinkUseCaseImpl(
            opdsFeedValidatorUseCase = instance(),
            opdsPublicationValidatorUseCase = instance(),
            respectAppManifestValidatorUseCase = instance(),
        )
    }

    bind<GetFavIconUseCase>() with singleton {
        GetFavIconsUseCaseImpl()
    }
}