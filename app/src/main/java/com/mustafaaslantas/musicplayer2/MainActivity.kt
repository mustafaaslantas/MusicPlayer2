package com.mustafaaslantas.musicplayer2

import android.content.res.AssetFileDescriptor
import android.content.res.Resources
import android.media.Image
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mustafaaslantas.musicplayer2.MusicPlayerViewModel
import com.mustafaaslantas.musicplayer2.R
import com.mustafaaslantas.musicplayer2.ui.theme.MusicPlayer2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayer2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MusicPlayerViewModel = viewModel()
                    MusicPlayerScreen(viewModel)
                }
            }
        }
    }
}


@Composable
fun MusicPlayer(resources: Resources) {
    val rawFiles = resources.obtainTypedArray(R.array.raw_files)
    val mediaPlayer = MediaPlayer()
    val rawFileNames = resources.assets.list("raw")


    rawFileNames?.forEach { fileName ->
        mediaPlayer.apply {
            reset()
            val descriptor: AssetFileDescriptor = resources.assets.openFd("raw/$fileName")
            setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
            prepare()
            start()
            setOnCompletionListener { mp -> mp.release() }
        }
    }

    for (i in 0 until rawFiles.length()) {
        val rawId = rawFiles.getResourceId(i, 0)
        mediaPlayer.apply {
            reset()
            setDataSource(resources.openRawResourceFd(rawId))
            prepare()
            start()
            setOnCompletionListener { mp -> mp.release() }
        }
    }


    rawFiles.recycle()
}
private fun playPreviousSong() {
}
private fun playNextSong(){}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(viewModel: MusicPlayerViewModel) {
    val isPlaying by viewModel.isPlaying.observeAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Music Player")
                },
            )
        },
    )
    {
        it

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Favorites")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Tracks")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Folders")

                }
            }
            Surface(
                modifier = Modifier.height(485.dp).width(300.dp).padding(1.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.unknown),
                    contentDescription = "unknown")
            }

            MusicControlButtons(
                isPlaying = isPlaying,
                onPlayPauseToggle = { viewModel.togglePlayPause() },
                onPrevious = { playPreviousSong() },
                onNext = { playNextSong() },
            )
        }

    }
}




@Composable
fun MusicControlButtons(
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(painterResource(id = R.drawable.unknown) , contentDescription = "Album Cover" ,
            Modifier.size(30.dp))

        IconButton(
            onClick = { onPrevious() }
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            onClick = { onPlayPauseToggle() }
        ) {
            if (isPlaying) {
                Icon(Icons.Default.Close, contentDescription = "Pause")
            } else {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            onClick = { onNext() }
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next")
        }

        Spacer(modifier = Modifier.width(100.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MusicPlayer2Theme {
        MusicPlayer(Resources.getSystem())
    }
}

