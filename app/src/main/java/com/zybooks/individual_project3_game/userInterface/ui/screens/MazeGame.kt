package com.zybooks.individual_project3_game.userInterface.ui.screens

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.yourappname.data.models.Platform
import com.zybooks.individual_project3_game.BackgroundMusicController
import com.zybooks.individual_project3_game.Data.Game1Level2
import com.zybooks.individual_project3_game.Data.Game2Level2
import com.zybooks.individual_project3_game.Data.Game3Level2
import com.zybooks.individual_project3_game.R
import com.zybooks.individual_project3_game.levels.Game1Level1
import com.zybooks.individual_project3_game.levels.Game2Level1
import com.zybooks.individual_project3_game.levels.Game3Level1
import com.zybooks.individual_project3_game.userInterface.ui.components.DirectionalArrow
import com.zybooks.individual_project3_game.utils.LogUtils
import kotlinx.coroutines.delay
import kotlin.math.abs

enum class GameState {
    PLAYING,
    LEVEL_COMPLETE,
    GAME_OVER,
    TIME_UP  // Add new state for timer expiration
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MazeGame(playerName: String, navController: NavController, modifier: Modifier = Modifier)

{
    var playerX by remember { mutableStateOf(10f * 6f) }
    var playerY by remember { mutableStateOf((80f + (20f / 2)) * 6f) }
    var direction by remember { mutableStateOf("none") }
    var showLevelTransition by remember { mutableStateOf(false) }
    var gameState by remember { mutableStateOf(GameState.PLAYING) }
    var isGameDialogVisible by remember { mutableStateOf(false) }
    // Timer state
    var timeLeft by remember { mutableStateOf(50) }
    var isTimerRunning by remember { mutableStateOf(true) }
    val scale = 6f
    var playerRotation by remember { mutableStateOf(400f) }
    var showCongratulationsScreen by remember { mutableStateOf(false) }
    var initialTimeLeft = 50
    var showGameCompleteScreen by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val musicController = remember { BackgroundMusicController(context) }
    val coinBitmap = remember(context) {
        BitmapFactory.decodeResource(context.resources, R.drawable.goldcoin4)
            ?.let { originalBitmap ->
                Bitmap.createScaledBitmap(originalBitmap, 160, 160, true)
            }
    }
    LaunchedEffect(isPaused) {
        if (isPaused) {
            musicController.pauseMusic()
        } else {
            musicController.resumeMusic()
        }
    }

    val playerBitmap = remember(context) {
        BitmapFactory.decodeResource(context.resources, R.drawable.smiley)
            ?.let { originalBitmap ->
                Bitmap.createScaledBitmap(originalBitmap, 100, 100, true)
            }
    }

    @Composable
    fun SessionHeader(
        playerName: String,
        currentLevel: Int,
        totalScore: Int,
        timeLeft: Int
    ) {

        LaunchedEffect(Unit) {
            musicController.startMusic()
        }
        DisposableEffect(Unit) {
            onDispose {
                musicController.stopMusic()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Player: $playerName",
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = "Level: $currentLevel/6",
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = "Score: $totalScore",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }

    var currentLevel by remember { mutableStateOf(6) }

    var platforms by remember(currentLevel) {
        mutableStateOf(
            when (currentLevel) {
                1 -> Game1Level1.getPlatforms(scale)
                2 -> Game2Level1.getPlatforms(scale)
                3 -> Game3Level1.getPlatforms(scale)
                4 -> Game1Level2.getPlatforms(scale)
                5 -> Game2Level2.getPlatforms(scale)
                6 -> Game3Level2.getPlatforms(scale)
                else -> Game1Level1.getPlatforms(scale)
            }
        )
    }

    var coins by remember(currentLevel) {
        val coinsList = when (currentLevel) {
            1 -> Game1Level1.getCoins(scale)
            2 -> Game2Level1.getCoins(scale)
            3 -> Game3Level1.getCoins(scale)
            4 -> Game1Level2.getCoins(scale)
            5 -> Game2Level2.getCoins(scale)
            6 -> Game3Level2.getCoins(scale)
            else -> Game1Level1.getCoins(scale)
        }
        mutableStateOf(coinsList)
    }

    var score by remember { mutableStateOf(0) }
    var totalScore by remember { mutableStateOf(0) }

    // Add background resource selector
    val backgroundResource = when (currentLevel) {
        4 -> R.drawable.background2
        5 -> R.drawable.background2
        6 -> R.drawable.background2
        else -> R.drawable.grass_03
    }

    fun resetLevel() {
        score = 0
        playerX = 30f * scale - 5
        playerY = (80f + (20f / 2)) * scale
        direction = "none"
        gameState = GameState.PLAYING
        isGameDialogVisible = false

        // Update platforms and coins for the current level
        platforms = when (currentLevel) {
            1 -> Game1Level1.getPlatforms(scale)
            2 -> Game2Level1.getPlatforms(scale)
            3 -> Game3Level1.getPlatforms(scale)
            4 -> Game1Level2.getPlatforms(scale)
            5 -> Game2Level2.getPlatforms(scale)
            6 -> Game3Level2.getPlatforms(scale)
            else -> Game1Level1.getPlatforms(scale)
        }

        coins = when (currentLevel) {
            1 -> Game1Level1.getCoins(scale)
            2 -> Game2Level1.getCoins(scale)
            3 -> Game3Level1.getCoins(scale)
            4 -> Game1Level2.getCoins(scale)
            5 -> Game2Level2.getCoins(scale)
            6 -> Game3Level2.getCoins(scale)
            else -> Game1Level1.getCoins(scale)
        }
    }

    LaunchedEffect(currentLevel, isTimerRunning, isPaused) {
        while (isTimerRunning && timeLeft > 0 && !isPaused) {  // Added !isPaused check
            delay(1000L)
            timeLeft--

            if (timeLeft <= 0) {
                gameState = GameState.TIME_UP
                isGameDialogVisible = true
                isTimerRunning = false
            }
        }
    }

    LaunchedEffect(score) {
        val coinsInLevel = coins.size
        if (score > 0 && score == coinsInLevel) {
            when {
                currentLevel < 3 -> {
                    currentLevel++
                    resetLevel()
                    // Timer continues running for Challenge 1
                }
                currentLevel == 3 -> {
                    showCongratulationsScreen = true
                    isGameDialogVisible = true
                    gameState = GameState.LEVEL_COMPLETE
                    LogUtils.writeGameProgressToFile(
                        context = context,
                        currentLevel = currentLevel,
                        totalScore = totalScore,
                        timeLeft = timeLeft,
                        status = "Challenge 1 Completed"
                    )
                    isTimerRunning = false
                }
                currentLevel in 4..5 -> {
                    currentLevel++
                    resetLevel()
                    // Timer continues running for Challenge 2
                }
                currentLevel == 6 -> {
                    showGameCompleteScreen = true
                    isGameDialogVisible = true
                    gameState = GameState.LEVEL_COMPLETE
                    LogUtils.writeGameProgressToFile(
                        context = context,
                        currentLevel = currentLevel,
                        totalScore = totalScore,
                        timeLeft = timeLeft,
                        status = "Challenge 2 Completed"
                    )
                    isTimerRunning = false
                }
            }
        }
    }

    fun isOnPlatform(x: Float, y: Float, platforms: List<Platform>): Boolean {
        val playerRadius = 50f

        val connectedPlatforms = platforms.filter { platform ->
            x >= platform.x - playerRadius &&
                    x <= platform.x + platform.width + playerRadius &&
                    y >= platform.y - playerRadius &&
                    y <= platform.y + platform.height + playerRadius
        }

        if (connectedPlatforms.size > 1) {
            val minX = connectedPlatforms.minOf { it.x }
            val maxX = connectedPlatforms.maxOf { it.x + it.width }
            val minY = connectedPlatforms.minOf { it.y }
            val maxY = connectedPlatforms.maxOf { it.y + it.height }

            return x >= minX + playerRadius &&
                    x <= maxX - playerRadius &&
                    y >= minY + playerRadius &&
                    y <= maxY - playerRadius
        }

        return platforms.any { platform ->
            x >= platform.x + playerRadius &&
                    x <= platform.x + platform.width - playerRadius &&
                    y >= platform.y + playerRadius &&
                    y <= platform.y + platform.height - playerRadius
        }
    }

    LaunchedEffect(direction) {
        if (!isPaused) {  // Only allow movement if not paused
            when (direction) {
                "up" -> {
                    var velocity = 8f
                    repeat(90) {
                        delay(16)
                        val newY = playerY - velocity
                    playerRotation += 4f  // Add rotation
                    if (isOnPlatform(playerX, newY, platforms)) {
                        playerY = newY
                        coins.forEach { coin ->
                            if (!coin.collected && abs(playerX - coin.x) < 55f && abs(playerY - coin.y) < 55f) {
                                coin.collected = true
                                score += 1
                                totalScore += 1
                            }
                        }
                    } else {
                        velocity *= 0.9f
                    }
                }
            }

            "down" -> {
                var velocity = 8f
                repeat(90) {
                    delay(16)
                    val newY = playerY + velocity
                    playerRotation -= 4f  // Add rotation in opposite direction
                    if (isOnPlatform(playerX, newY, platforms)) {
                        playerY = newY
                        coins.forEach { coin ->
                            if (!coin.collected && abs(playerX - coin.x) < 55f && abs(playerY - coin.y) < 55f) {
                                coin.collected = true
                                score += 1
                                totalScore += 1
                            }
                        }
                    } else {
                        velocity *= 0.9f
                    }
                }
            }

            "right" -> {
                var velocity = 8f
                repeat(90) {
                    delay(16)
                    val newX = playerX + velocity
                    playerRotation += 4f  // Add rotation
                    if (isOnPlatform(newX, playerY, platforms)) {
                        playerX = newX
                        coins.forEach { coin ->
                            if (!coin.collected && abs(playerX - coin.x) < 55f && abs(playerY - coin.y) < 55f) {
                                coin.collected = true
                                score += 1
                                totalScore += 1
                            }
                        }
                    } else {
                        velocity *= 0.9f
                    }
                }
            }
        }
        direction = "none"
    }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.winter_background_top),
                contentDescription = "Winter Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(end = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(8.dp)
                            .border(1.dp, Color.Black)
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { event ->
                                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                },
                                target = remember {
                                    object : DragAndDropTarget {
                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            direction = "up"
                                            return true
                                        }
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Up",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            DirectionalArrow(direction = "up")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(8.dp)
                            .border(1.dp, Color.Black)
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { event ->
                                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                },
                                target = remember {
                                    object : DragAndDropTarget {
                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            direction = "down"
                                            return true
                                        }
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Down",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            DirectionalArrow(direction = "down")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(8.dp)
                            .border(1.dp, Color.Black)
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { event ->
                                    event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                },
                                target = remember {
                                    object : DragAndDropTarget {
                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            direction = "right"
                                            return true
                                        }
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Right",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            DirectionalArrow(direction = "right")
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        var isDragging by remember { mutableStateOf(false) }

                        val scale by animateFloatAsState(
                            targetValue = if (isDragging) 1.2f else 1f,
                            label = "drag scale"
                        )

                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Star",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(150.dp)
                                .scale(scale)
                                .alpha(
                                    animateFloatAsState(
                                        targetValue = if (isDragging) 0.4f else 1f,
                                        label = "fade animation"
                                    ).value
                                )
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = { offset ->
                                            isDragging = true
                                            startTransfer(
                                                transferData = DragAndDropTransferData(
                                                    clipData = ClipData.newPlainText(
                                                        "text",
                                                        ""
                                                    )
                                                )
                                            )
                                            isDragging = false
                                        }
                                    )
                                }
                        )
                    }

                    Box(
                        modifier = Modifier.padding(start = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "TOTAL SCORE",
                                    color = Color.Yellow,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "$totalScore",
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    style = androidx.compose.ui.text.TextStyle(
                                        shadow = Shadow(
                                            color = Color.Black,
                                            blurRadius = 8f,
                                            offset = Offset(2f, 2f)
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.60f)
        ) {
            Image(
                painter = painterResource(id = backgroundResource),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(modifier = Modifier.fillMaxSize()) {
                SessionHeader(
                    playerName = playerName,
                    currentLevel = currentLevel,
                    totalScore = totalScore,
                    timeLeft = timeLeft
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = when {
                                    currentLevel <= 3 -> "Challenge 1: Complete levels 1-3 in 50 seconds!"
                                    else -> "Challenge 2: Complete levels 4-6 in 50 seconds!"
                                },
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Button(
                                onClick = { isPaused = !isPaused },  // Toggle pause state
                                modifier = Modifier
                                    .size(140.dp, 45.dp)
                                    .border(
                                        width = 2.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(20.dp)
                                    ),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black.copy(alpha = 0.7f),
                                    contentColor = Color.White
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                )
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "PAUSE",  // Correct spelling with 'e' at the end
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Time: $timeLeft",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 12.dp),  // Adjust the padding to make it higher
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            ),
                            color = when {
                                timeLeft <= 10 -> Color.Red
                                timeLeft <= 30 -> Color.Yellow
                                else -> Color.Black
                            }
                        )
                    }



                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp)
                ){
                    platforms.forEach { platform ->
                        drawRect(
                            color = Color(0xFF5B9BD5),
                            topLeft = Offset(platform.x, platform.y),
                            size = Size(platform.width, platform.height)
                        )
                    }

                    coins.forEach { coin ->
                        if (!coin.collected && coinBitmap != null) {
                            drawIntoCanvas { canvas ->
                                canvas.nativeCanvas.drawBitmap(
                                    coinBitmap,
                                    coin.x - 90f,
                                    coin.y - 90f,
                                    null
                                )
                            }
                        }
                    }

                    //player
                    playerBitmap?.let { bitmap ->
                        drawIntoCanvas { canvas ->
                            canvas.nativeCanvas.apply {
                                save()
                                rotate(
                                    playerRotation,
                                    playerX,
                                    playerY
                                )
                                drawBitmap(
                                    bitmap,
                                    playerX - bitmap.width / 2f - 5,
                                    playerY - bitmap.height / 2f,
                                    null
                                )
                                restore()
                            }
                        }
                    }
                }


                    if (isPaused) {
                        Dialog(onDismissRequest = {
                            isPaused = false
                            musicController.resumeMusic()
                        }) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Game Paused",
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Text("Player: $playerName")
                                    Text("Current Level: $currentLevel")
                                    Text("Total Score: $totalScore")
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            isPaused = false
                                            musicController.resumeMusic()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Text("Resume Game")
                                    }
                                    Button(
                                        onClick = {
                                            currentLevel = 1
                                            totalScore = 0
                                            resetLevel()
                                            isPaused = false
                                            musicController.startMusic() // Restart music
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                    ) {
                                        Text("Restart Game")
                                    }
                                    Button(
                                        onClick = {
                                            musicController.stopMusic() // Stop music before exiting
                                            navController.navigate("login") {
                                                popUpTo(0)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                    ) {
                                        Text("Exit Game", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                        }
                    }
                }
        // Add a new flag to track if the timer was reset for Challenge 2
        var hasTimerResetForChallenge2 = false

        if (isGameDialogVisible) {
            Dialog(onDismissRequest = { }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when {
                            showGameCompleteScreen -> {
                                Text(
                                    "Congratulations!",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text("You've completed Challenge 2!")
                                Text("Time remaining: $timeLeft seconds")
                                Text("Final Score: $totalScore")
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = {
                                    currentLevel = 1
                                    timeLeft = 50  // Reset for new game
                                    totalScore = 0
                                    showGameCompleteScreen = false
                                    isGameDialogVisible = false
                                    isTimerRunning = true  // Start timer for new game
                                    resetLevel()
                                }) {
                                    Text("Play Again")
                                }
                            }
                            showCongratulationsScreen -> {
                                // Replace this entire case with your new code
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Challenge 1 Complete!",
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Text("You've completed the first three levels!")
                                    Text("Time remaining: $timeLeft seconds")
                                    Text("Current Score: $totalScore")
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(onClick = {
                                        currentLevel = 4
                                        timeLeft = 50  // Reset timer for Challenge 2
                                        isTimerRunning = true
                                        hasTimerResetForChallenge2 = true
                                        showCongratulationsScreen = false
                                        isGameDialogVisible = false
                                        resetLevel()
                                    }) {
                                        Text("Continue to Challenge 2")
                                    }
                                    Button(
                                        onClick = {
                                            currentLevel = 1
                                            timeLeft = 50
                                            totalScore = 0
                                            hasTimerResetForChallenge2 = false
                                            showCongratulationsScreen = false
                                            isGameDialogVisible = false
                                            isTimerRunning = true
                                            resetLevel()
                                        },
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Text("Start Over")
                                    }
                                }
                            }
                            gameState == GameState.TIME_UP -> {
                                Text(
                                    "Time's Up!",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    if (currentLevel <= 3) {
                                        "Challenge 1 failed! Try to complete levels 1-3 within 50 seconds."
                                    } else {
                                        "Challenge 2 failed! Try to complete levels 4-6 within 50 seconds."
                                    }
                                )
                                Text("Your final score: $totalScore")
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = {
                                    currentLevel = if (currentLevel <= 3) 1 else 4  // Reset to start of current challenge
                                    timeLeft = 50
                                    totalScore = 0
                                    isGameDialogVisible = false
                                    isTimerRunning = true
                                    resetLevel()
                                }) {
                                    Text("Try Again")
                                }
                            }
                        }
                    }
                }
            }

        }}}

//Added the correct timer.