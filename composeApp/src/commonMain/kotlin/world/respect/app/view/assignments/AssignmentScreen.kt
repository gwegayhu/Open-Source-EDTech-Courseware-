package world.respect.app.view.assignments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.assignment


@Composable
fun AssignmentScreen (){
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text=stringResource(Res.string.assignment))
    }
}