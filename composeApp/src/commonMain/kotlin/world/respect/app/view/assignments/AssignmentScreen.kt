package world.respect.app.view.assignments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.stringResource
import respect.composeapp.generated.resources.Res
import respect.composeapp.generated.resources.assignment
import world.respect.app.viewmodel.assignments.AssignmentScreenViewModel


@Composable
fun AssignmentScreen (navController: NavHostController,
                      viewModel: AssignmentScreenViewModel){
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text=stringResource(Res.string.assignment))
    }
}