package world.respect.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.PatternsCompat
import com.ustadmobile.libuicompose.theme.md_theme_light_primary
import com.ustadmobile.libuicompose.theme.md_theme_light_primaryContainer

@Composable
fun EnterLinkScreen() {
    var link by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {



        Text("Copy/paste the RESPECT app link provided by the app developer")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = link,
            onValueChange = {
                link = it
                if (showError) showError = false // reset error on new input
            },
            label = { Text("Link*") },
            placeholder = { Text("e.g. https://") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                disabledBorderColor = Color.Black,
                disabledLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showError = !PatternsCompat.WEB_URL.matcher(link).matches()
                if (!showError) {
                    // Proceed to next step
                }
            },
            colors = ButtonDefaults.buttonColors(
                md_theme_light_primaryContainer,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }

        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ERROR: Invalid link. Please check the URL and try again.",
                color = Color.Red,
                fontSize = 14.sp
            )
        }
    }
}
