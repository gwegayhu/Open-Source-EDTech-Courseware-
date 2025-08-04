package world.respect.server

import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.helper.HelpScreenException
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.inf.Namespace
import kotlin.system.exitProcess

const val CMD_RUN_SERVER = "runserver"

fun main(args: Array<String>) {
    val parser = ArgumentParsers.newFor("respect-server").build()

    val subparsers = parser.addSubparsers()
        .title("subcommands")
        .description("valid subcommands")
        .dest("subparser_name")
        .help("additional help")
        .metavar("COMMAND")

    subparsers.addParser(CMD_RUN_SERVER).help("Run RESPECT http server")

    val ns: Namespace?
    try {
        ns = parser.takeIf {
            args.isNotEmpty() && args.firstOrNull() != CMD_RUN_SERVER
        }?.parseArgs(args)
        val subCommand = ns?.getString("subparser_name") ?: CMD_RUN_SERVER

        when(subCommand) {
            CMD_RUN_SERVER -> {
                //As per https://ktor.io/docs/server-create-a-new-project.html#change-the-port-via-yaml
                io.ktor.server.netty.EngineMain.main(args)
            }
        }
    }catch (e: ArgumentParserException) {
        parser.handleError(e)
        exitProcess(if (e is HelpScreenException) 0 else 1)
    }
}