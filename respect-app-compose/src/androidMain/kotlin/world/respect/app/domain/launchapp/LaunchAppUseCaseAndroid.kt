package world.respect.app.domain.launchapp

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import io.ktor.http.Url
import world.respect.WebViewActivity
import world.respect.datalayer.compatibleapps.model.RespectAppManifest
import world.respect.shared.domain.account.RespectAccount
import world.respect.shared.domain.launchapp.LaunchAppUseCase
import world.respect.shared.navigation.NavCommand

/**
 * Implementation of LaunchAppUseCase for Android. This use case is in respect-app-compose because
 * it needs access to the WebViewActivity class.
 */
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
                //In future - apps will need an action we can query to check if it is installed.
                //didn't work - maybe wasn't installed.
            }
        }

        val intent = Intent(appContext, WebViewActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(WebViewActivity.Companion.EXTRA_URL, launchUrl.toString())
        appContext.startActivity(intent)
    }
}