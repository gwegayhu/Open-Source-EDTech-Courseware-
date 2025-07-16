package world.respect.app.view.curriculum

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import world.respect.shared.generated.resources.Res
import world.respect.shared.generated.resources.*
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.ui.unit.dp

object TabConstants {
    const val FOR_YOU = 0
    const val BY_CURRICULUM = 1
}
@Composable
fun AppsByCurriculumScreen(
    curricula: List<Any> = emptyList(),
    selectedTab: Int = TabConstants.BY_CURRICULUM,
    onTabSelected: (Int) -> Unit = {},
    onAddCurriculumClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onBottomNavClick: (Any) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var currentTab by remember { mutableIntStateOf(selectedTab) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(Res.string.apps),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            }
        }

        TabRow(
            selectedTabIndex = currentTab,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = currentTab == TabConstants.FOR_YOU,
                onClick = {
                    currentTab = TabConstants.FOR_YOU
                    onTabSelected(TabConstants.FOR_YOU)
                },
                text = { Text(stringResource(Res.string.for_you)) }
            )
            Tab(
                selected = currentTab == TabConstants.BY_CURRICULUM,
                onClick = {
                    currentTab = TabConstants.BY_CURRICULUM
                    onTabSelected(TabConstants.BY_CURRICULUM)
                },
                text = { Text(stringResource(Res.string.by_curriculum)) }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (currentTab) {
                TabConstants.FOR_YOU -> {
                    Text("For You Content")
                }

                TabConstants.BY_CURRICULUM -> {
                    Text("By Curriculum Content")
                }
            }
        }

        if (currentTab == TabConstants.BY_CURRICULUM) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onAddCurriculumClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(Res.string.curriculum))
                    }
                }
            }
        }
    }
}