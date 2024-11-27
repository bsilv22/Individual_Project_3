// GameMenu.kt
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.media.SoundPool
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import com.zybooks.individual_project3_game.R

class SoundHelper(context: Context) {
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .build()

    private val buttonClickSound = soundPool.load(context, R.raw.button_click, 1)

    fun playClickSound() {
        // Parameters: soundID, leftVolume, rightVolume, priority, loop, rate
        soundPool.play(buttonClickSound, 10f, 10f, 5, 0, 1f)
    }

}
@Composable
fun GameMenu(
    onStartGame: () -> Unit,
    onExitGame: () -> Unit
) {
    val context = LocalContext.current
    val soundHelper = remember { SoundHelper(context) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "PLATFORM GAME",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Button(
                onClick = {
                    soundHelper.playClickSound()
                    onStartGame()
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,  // Instead of backgroundColor
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "START GAME",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = {
                    soundHelper.playClickSound()
                    onExitGame()
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,  // Instead of backgroundColor
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "EXIT",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}