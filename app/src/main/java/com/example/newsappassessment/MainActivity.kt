package com.example.newsappassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsappassessment.model.News
import com.example.newsappassessment.model.NewsList
import com.example.newsappassessment.screen.NewsDetail
import com.example.newsappassessment.screen.NewsScreen
import com.example.newsappassessment.ui.theme.NewsAppAssessmentTheme
import com.example.newsappassessment.viewModel.NewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NewsAppAssessmentTheme {
                val viewModel: NewsViewModel = viewModel()
                // A surface container using the 'background' color from the theme
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            NewsScreen(navController,viewModel)
                        }
                    }
                    composable("detail") {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            NewsDetail(navController,viewModel)
                        }
                    }
                }

            }
        }
    }
}