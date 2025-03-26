package eu.iamkonstantin.gadulkaplayer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import eu.iamkonstantin.kotlin.gadulka.GadulkaPlayer
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            AudioPlayer()
        }
    }
}

@Composable
fun AudioPlayer(player: GadulkaPlayer = GadulkaPlayer()) {
    val url = remember { mutableStateOf("https://download.samplelib.com/wav/sample-12s.wav") }

    Row {
        Button(
            onClick = {
                player.play(
                    url.value
                )
            }) {
            Text("Play")
        }
        Button(
            onClick = {
                player.stop()
            }) {
            Text("Stop")
        }
    }
}