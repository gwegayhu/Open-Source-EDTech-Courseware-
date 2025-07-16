package world.respect.shared.domain.launchapp

import android.content.Context
import android.content.Intent
import io.ktor.http.Url
import world.respect.datalayer.compatibleapps.model.RespectAppManifest
import world.respect.shared.domain.account.RespectAccount
import world.respect.shared.navigation.LearningUnitViewer
import world.respect.shared.navigation.NavCommand
import androidx.core.net.toUri

class LaunchAppUseCaseAndroid(
    private val appContext: Context,
): LaunchAppUseCase {

    override fun invoke(
        app: RespectAppManifest,
        account: RespectAccount,
        learningUnitId: Url?,
        navigateFn: (NavCommand) -> Unit
    ) {
        val androidPackageId = app.android?.packageId
        val launchUrl = learningUnitId ?: Url(app.defaultLaunchUri.toString())

        if(androidPackageId != null) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage(androidPackageId)
            intent.setData(launchUrl.toString().toUri())

            try {
                appContext.startActivity(intent)
                return
            }catch(e: Throwable) {
                e.printStackTrace()
                //In future - apps will need an action we can query.
                //didn't work - maybe wasn't installed.
            }
        }

        navigateFn(
            NavCommand.Navigate(
                LearningUnitViewer.create(learningUnitId = launchUrl)
            )
        )
    }
}