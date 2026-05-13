
package com.example.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composedemo.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                MainScreen()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen() {
    var currentTask by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LiteRT AI Demo") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "更多"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavigationTabRow(currentTask) { currentTask = it }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            when (currentTask) {
                0 -> TaskOneScreen()
                1 -> TaskTwoScreen()
                2 -> TaskThreeScreen()
            }
        }
    }
}

@Composable
fun NavigationTabRow(currentTask: Int, onTaskChange: (Int) -> Unit) {
    TabRow(selectedTabIndex = currentTask) {
        Tab(
            selected = currentTask == 0,
            onClick = { onTaskChange(0) },
            text = { Text("任务一") }
        )
        Tab(
            selected = currentTask == 1,
            onClick = { onTaskChange(1) },
            text = { Text("任务二") }
        )
        Tab(
            selected = currentTask == 2,
            onClick = { onTaskChange(2) },
            text = { Text("任务三") }
        )
    }
}

@Composable
fun TaskOneScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                text = "首个 Kotlin 应用",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Greeting(name = "Android")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        fontSize = 24.sp
    )
}

@Composable
fun TaskTwoScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Compose 布局实践",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Hello", fontSize = 20.sp)
                Button(onClick = {}) {
                    Text("Show more")
                }
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Compose", fontSize = 20.sp)
                Button(onClick = {}) {
                    Text("Show more")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "布局示例",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.LightGray)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Blue)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Green)
            )
        }
    }
}

@Composable
fun TaskThreeScreen() {
    var modelName by remember { mutableStateOf("MobileNet") }
    var result by remember { mutableStateOf("") }
    var confidence by remember { mutableStateOf("") }
    var inferenceTime by remember { mutableStateOf("") }
    
    fun takePhoto() {
        result = "Cat"
        confidence = "96.2%"
        inferenceTime = "28 ms"
    }
    
    fun importGallery() {
        result = "Dog"
        confidence = "88.5%"
        inferenceTime = "32 ms"
    }
    
    fun switchModel() {
        modelName = if (modelName == "MobileNet") "ResNet" else "MobileNet"
    }
    
    fun clearResults() {
        result = ""
        confidence = ""
        inferenceTime = ""
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 预览区 - 使用 Box 占位
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.Gray
                )
                Text(
                    text = "Camera Preview",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
        
        // 结果区 - 使用 Card + Column 展示信息
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Model: $modelName",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (result.isNotEmpty()) "Result: $result" else "Result: -",
                    fontSize = 16.sp
                )
                Text(
                    text = if (confidence.isNotEmpty()) "Confidence: $confidence" else "Confidence: -",
                    fontSize = 16.sp
                )
                Text(
                    text = if (inferenceTime.isNotEmpty()) "Time: $inferenceTime" else "Time: -",
                    fontSize = 16.sp
                )
            }
        }
        
        // 按钮区 - 使用 Row / Column 排列按钮
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { takePhoto() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("拍照识别")
                }
                Button(
                    onClick = { importGallery() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Default.PhotoAlbum, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("相册导入")
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { switchModel() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                ) {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("切换模型")
                }
                Button(
                    onClick = { clearResults() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("清空结果")
                }
            }
        }
    }
}

