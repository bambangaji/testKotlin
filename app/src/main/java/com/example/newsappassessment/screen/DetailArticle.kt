package com.example.newsappassessment.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.newsappassessment.model.News
import com.example.newsappassessment.viewModel.NewsViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetail(navController: NavController,viewModel: NewsViewModel) {
    val articleDetailState = viewModel.articleDetail.observeAsState()

    val news = articleDetailState.value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail Article") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (news != null) {
                Text(text = news.title, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (news != null) {
                Text(text = "Author: ${news.author}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            SelectionContainer {
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            if (news != null) {
                                append(news.publishedAt)
                            }
                        }
                    },
                    onClick = { offset ->
                        // Handle click event if necessary
                    }
                )
            }
        }
    }
}