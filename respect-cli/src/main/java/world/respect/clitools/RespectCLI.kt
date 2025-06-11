package world.respect.clitools

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.helper.HelpScreenException
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.inf.Namespace
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import world.respect.domain.opds.model.ReadiumLink
import world.respect.domain.opds.validator.OpdsFeedValidator
import world.respect.domain.opds.validator.ValidateLinkUseCaseImpl
import world.respect.domain.opds.validator.OpdsPublicationValidator
import world.respect.domain.respectappmanifest.validator.RespectAppManifestValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import world.respect.domain.getfavicons.GetFavIconsUseCaseImpl
import world.respect.domain.opds.validator.ValidateOpdsPublicationUseCase
import world.respect.domain.respectdir.model.RespectAppManifest
import world.respect.domain.validator.ListAndPrintlnValidatorReporter
import world.respect.domain.validator.ValidateHttpResponseForUrlUseCase
import world.respect.domain.validator.ValidateLinkUseCase


@Suppress("unused")
class RespectCLI {

    companion object {

        const val CMD_VALIDATE = "validate"

        /**
         * DO NOT ATTEMPT TO RUN USING THE PLAY BUTTON IN ANDROID STUDIO! Resources will not be
         * found and it will not work.
         *
         * It can be run using as a Gradle task (respect-cli:run --args='..').
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val json = Json {
                encodeDefaults = false
                ignoreUnknownKeys = true
            }

            val okHttpClient = OkHttpClient.Builder()
                .dispatcher(
                    Dispatcher().also {
                        it.maxRequests = 30
                        it.maxRequestsPerHost = 10
                    }
                ).build()

            val httpClient = HttpClient(OkHttp) {
                install(ContentNegotiation) {
                    json(json = json)
                }
                engine {
                    preconfigured = okHttpClient
                }
            }

            val reporter = ListAndPrintlnValidatorReporter()

            val parser = ArgumentParsers.newFor("respect-cli").build()
            val subparsers = parser.addSubparsers()
                .title("subcommands")
                .description("valid subcommands: ")
                .dest("subparser_name")
                .help("additional help")
                .metavar("COMMAND")

            subparsers.addParser(CMD_VALIDATE).also {
                it.addArgument("-u", "--url")
                    .required(true)
                    .help("OPDS feed URL")
                it.addArgument("-f", "--nofollow")
                    .required(false)
                    .setDefault("false")
                    .help("Don't follow links")
                it.addArgument("-t", "--type")
                    .setDefault("manifest")
                    .help("manifest|feed")
            }.help("Validate a RESPECT App Manifest or OPDS Feed of Learning Units")


            val ns: Namespace
            try {
                ns = parser.parseArgs(args)
                val subCommand = ns?.getString("subparser_name")
                when(subCommand) {
                    CMD_VALIDATE -> {
                        val url = ns.getString("url")
                        val noFollow = ns.getString("nofollow").ifEmpty { "true" }

                        val validateHttpResponseForUrlUseCase = ValidateHttpResponseForUrlUseCase(
                            httpClient
                        )
                        val validateOpdsPublicationUseCase = ValidateOpdsPublicationUseCase(
                            validateHttpResponseForUrlUseCase
                        )

                        val validator = ValidateLinkUseCaseImpl(
                            opdsFeedValidatorUseCase = OpdsFeedValidator(
                                json = json,
                                httpClient = httpClient,
                                validateOpdsPublicationUseCase = validateOpdsPublicationUseCase,
                            ),
                            opdsPublicationValidatorUseCase = OpdsPublicationValidator(
                                httpClient = httpClient
                            ),
                            respectAppManifestValidatorUseCase = RespectAppManifestValidator(
                                json = json,
                                validateHttpResponseForUrlUseCase = validateHttpResponseForUrlUseCase,
                                getFavIconUseCase = GetFavIconsUseCaseImpl(),
                                httpClient = httpClient,
                            ),
                        )

                        runBlocking {
                            validator(
                                link = ReadiumLink(
                                    href = url,
                                    type = RespectAppManifest.MIME_TYPE,
                                ),
                                options = ValidateLinkUseCase.ValidatorOptions(
                                    followLinks = !(noFollow?.toBoolean() ?: false)
                                ),
                                baseUrl = url,
                                reporter = reporter,
                                visitedUrls = mutableListOf(),
                            )
                        }

                        val numErrors = reporter.messages.count { it.isError }
                        println("Errors: $numErrors")

                        if(numErrors > 0) {
                            System.exit(1)
                        }else {
                            System.exit(0)
                        }
                    }
                }
            }catch(e : ArgumentParserException) {
                parser.handleError(e)
                System.exit(if(e is HelpScreenException) 0 else 1)
            }
        }
    }
}