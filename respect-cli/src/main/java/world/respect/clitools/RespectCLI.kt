package world.respect.clitools

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.helper.HelpScreenException
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.inf.Namespace
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import world.respect.di.JvmCoreDiMOdule
import world.respect.domain.opds.model.ReadiumLink
import world.respect.domain.respectdir.model.RespectAppManifest
import world.respect.domain.validator.ListAndPrintlnValidatorReporter
import world.respect.domain.validator.ValidateLinkUseCase
import world.respect.domain.validator.ValidatorMessage


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
            val di = DI {
                import(JvmCoreDiMOdule)
            }

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
                it.addArgument("-o", "--output")
                    .choices("error", "warn", "verbose", "debug")
                    .setDefault("warn")
                    .help("Output verbosity")

            }.help("Validate a RESPECT App Manifest or OPDS Feed of Learning Units")


            val ns: Namespace
            try {
                ns = parser.parseArgs(args)
                val subCommand = ns?.getString("subparser_name")
                when(subCommand) {
                    CMD_VALIDATE -> {
                        val url = ns.getString("url")
                        val minLevel = ValidatorMessage.Level.valueOf(
                            (ns.getString("output") ?: "warn").uppercase()
                        )
                        val noFollow = ns.getString("nofollow")?.ifEmpty { null }

                        val reporter = ListAndPrintlnValidatorReporter(
                            filter = {
                                it.level.ordinal >= minLevel.ordinal
                            }
                        )

                        val validator: ValidateLinkUseCase = di.direct.instance()

                        runBlocking {
                            validator(
                                link = ReadiumLink(
                                    href = url,
                                    type = RespectAppManifest.MIME_TYPE,
                                ),
                                options = ValidateLinkUseCase.ValidatorOptions(
                                    followLinks = !(noFollow?.toBoolean() ?: false)
                                ),
                                refererUrl = url,
                                reporter = reporter,
                                visitedUrls = mutableListOf(),
                            )
                        }

                        val numErrors = reporter.messages.count {
                            it.level == ValidatorMessage.Level.ERROR
                        }
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