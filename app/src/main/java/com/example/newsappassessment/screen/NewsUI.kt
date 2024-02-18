package com.example.newsappassessment.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.R
import com.example.newsappassessment.model.News
import com.example.newsappassessment.network.ApiService
import com.example.newsappassessment.repository.NewsRepository
import com.example.newsappassessment.viewModel.NewsViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
private val listCategory: Array<String> = arrayOf("business", "entertainment", "general", "health", "science", "sports", "technology")
private val buffer = 1
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavController,viewModel: NewsViewModel) {
    var newsListState by remember { mutableStateOf<List<News>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf("business") } // Default category
    var searchQuery by remember { mutableStateOf("") } // Default query
    var isLoading by remember { mutableStateOf(false) } // Loading indicator state
    var currentPage by remember { mutableIntStateOf(1) } // Loading indicator state
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    // Function to fetch news based on selected category
    fun fetchNewsByCategory(category: String,page:Int,query: String) {
        println("fetchData;;")
        coroutineScope.launch {
            isLoading = true // Start loading
            val newsRepository = NewsRepository()
            try {
                // Call the suspend function from ViewModel to fetch news
                val news = newsRepository.getNews(category,page,query)
                newsListState += news
                println(newsListState)
            } catch (e: Exception) {
                // Handle exceptions if needed
                e.printStackTrace()
            } finally {
                isLoading = false // Stop loading
            }
        }
    }

    // observe list scrolling
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == scrollState.layoutInfo.totalItemsCount- buffer
        }
    }
    LaunchedEffect(selectedCategory) {
        currentPage = 1;
        searchQuery = "";
        newsListState = emptyList()
        fetchNewsByCategory(selectedCategory,currentPage,searchQuery)
    }
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            currentPage += 1
            fetchNewsByCategory(selectedCategory, currentPage,searchQuery)
            println("sadhjgadhj")
        }
    }
    LaunchedEffect(searchQuery){
        if (searchQuery.length > 5) {
            currentPage = 1;
            newsListState = emptyList()
            println("update qqeuery")
            fetchNewsByCategory(selectedCategory, currentPage, searchQuery)
            // Update the value of searchQuery
            // Optionally trigger fetchNewsByCategory
        }
    }
    // Display news list with loading indicator
    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                contentAlignment = Alignment.Center,

            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        textStyle = TextStyle.Default.copy(textDecoration = TextDecoration.None),
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listCategory.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedCategory = item
                                    searchQuery = ""
                                    fetchNewsByCategory(selectedCategory,currentPage,searchQuery)
                                    expanded = false
                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                contentAlignment = Alignment.Center,

                ){
                TextField(
                    value = searchQuery,
                    onValueChange = { newValue ->
                        println("New value: $newValue")
                        searchQuery = newValue
                                    },
                    textStyle = TextStyle.Default.copy(textDecoration = TextDecoration.None),
                )
            }

            NewsList(newsList = newsListState, scrollState = scrollState,navController,viewModel)
            if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                        content = {
                            CircularProgressIndicator()
                        }
                    )
            } else {
//              NewsScreen()
            }
        }
    }
}

@Composable
fun NewsList(newsList: List<News>, scrollState: LazyListState,navController: NavController,viewModel: NewsViewModel) {
        LazyColumn(state = scrollState) {
        items(newsList) { news ->
            Box(modifier = Modifier
                .fillMaxSize()
                .border(width = 1.dp, color = Color(4)),){
                NewsItem(news = news,navController, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NewsItem(news: News,navController: NavController,viewModel: NewsViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                viewModel.setArticleDetail(news)
                navController.navigate("detail")
            }
    ) {
        Text(text = news.title, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = news.author, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
    }
}