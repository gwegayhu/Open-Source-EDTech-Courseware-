package world.respect.shared.domain.launchapp

import android.content.Context
import io.ktor.http.Url
import world.respect.datalayer.compatibleapps.model.RespectAppManifest
import world.respect.shared.domain.account.RespectAccount
import world.respect.shared.navigation.LearningUnitViewer
import world.respect.shared.navigation.NavCommand

class LaunchAppUseCaseAndroid(
    private val appContext: Context,
): LaunchAppUseCase {

    override fun invoke(
        app: RespectAppManifest,
        account: RespectAccount,
        learningUnitId: Url?,
        navigateFn: (NavCommand) -> Unit
    ) {
        navigateFn(
            NavCommand.Navigate(
                LearningUnitViewer.create(
                    learningUnitId = learningUnitId ?: Url(app.defaultLaunchUri.toString())
                )
            )
        )
    }
}