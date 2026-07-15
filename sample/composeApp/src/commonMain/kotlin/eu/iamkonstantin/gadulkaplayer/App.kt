package eu.iamkonstantin.gadulkaplayer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import eu.iamkonstantin.gadulkaplayer.compose.resources.Res
import eu.iamkonstantin.gadulkaplayer.compose.resources.flag_be
import eu.iamkonstantin.gadulkaplayer.compose.resources.flag_de
import eu.iamkonstantin.gadulkaplayer.di.AudioStorage
import eu.iamkonstantin.kotlin.gadulka.GadulkaPlayerState
import eu.iamkonstantin.kotlin.gadulka.rememberGadulkaLiveState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


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
fun AudioPlayer() {

    val audioStorage = koinInject<AudioStorage>()
    val playerState = rememberGadulkaLiveState()
    val url = remember { mutableStateOf("https://download.samplelib.com/wav/sample-12s.wav") }

    var isPlaying by remember { mutableStateOf(false) }

    var belgianAnthem by remember { mutableStateOf<String?>(null) }
    var germanAnthem by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        playerState.player.setRate(1.0f)
        belgianAnthem = audioStorage.prepareAudio("anthem-belgium.mp3")
        germanAnthem = audioStorage.prepareAudio("anthem-germany.mp3")
    }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {
            Column {
                Text("Gadulka Demo", style = MaterialTheme.typography.headlineMedium)

                Text(getPlatform().name, style = MaterialTheme.typography.bodyMedium)

                Text(playerState.state.name)

                Text("Volume: ${playerState.volume}")

                Text("Position: ${playerState.position / 1000}s / ${playerState.duration / 1000}s")
            }
        }

        Row {
            TextField(value = url.value, onValueChange = { url.value = it })
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    if (playerState.state == GadulkaPlayerState.PAUSED) {
                        // resume from current position
                        playerState.player.play()
                    } else {
                        // play something new
                        playerState.player.play(url.value)
                    }
                }) {
                Text("Play")
            }
            Button(
                onClick = {
                    playerState.player.pause()
                },
                enabled = playerState.state == GadulkaPlayerState.PLAYING) {
                Text("Pause")
            }
            Button(
                onClick = {
                    playerState.player.stop()
                }) {
                Text("Stop")
            }
        }

        Text(
            text = "Let's play some national anthems",
            style = MaterialTheme.typography.titleMedium
        )

        Row {
            VolumeSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                initialVolume = 1.0f
            ) { newVolume ->
                playerState.player.setVolume(newVolume)
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            belgianAnthem?.let { audio ->
                Text(
                    text = "Belgium",
                    style = MaterialTheme.typography.labelMedium
                )
                Image(
                    modifier = Modifier
                        .height(50.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .hoverable(interactionSource)
                        .clickable {
                            if (!isPlaying) {
                                isPlaying = true
                                playerState.player.play(audio)
                            } else {
                                isPlaying = false
                                playerState.player.stop()
                            }
                        },
                    painter = painterResource(Res.drawable.flag_be),
                    contentDescription = "Play Belgian National Anthem",
                    contentScale = ContentScale.Fit,
                )
            }

            germanAnthem?.let { audio ->
                Text(
                    text = "Germany",
                    style = MaterialTheme.typography.labelMedium
                )
                Image(
                    modifier = Modifier
                        .height(50.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .hoverable(interactionSource)
                        .clickable {
                            if (!isPlaying) {
                                isPlaying = true
                                playerState.player.play(audio)
                            } else {
                                isPlaying = false
                                playerState.player.stop()
                            }
                        },
                    painter = painterResource(Res.drawable.flag_de),
                    contentDescription = "Play Belgian National Anthem",
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}


@Composable
fun VolumeSlider(
    modifier: Modifier = Modifier,
    initialVolume: Float = 0.5f,
    onVolumeChange: (Float) -> Unit = {}
) {
    var volume by remember { mutableStateOf(initialVolume) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        // Triangle visual representation
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            val path = Path().apply {
                moveTo(0f, size.height) // Bottom-left corner
                lineTo(size.width * volume, size.height) // Bottom-right corner (scaled by volume)
                lineTo(0f, 0f) // Top-left corner
                close()
            }
            drawPath(path, color = Color.Blue)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Slider for volume control
        Slider(
            value = volume,
            onValueChange = {
                volume = it
                onVolumeChange(it)
            },
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
