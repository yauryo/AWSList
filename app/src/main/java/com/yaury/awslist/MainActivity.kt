package com.yaury.awslist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yaury.awslist.data.MobilesRepositoryImplementation
import com.yaury.awslist.model.MobileItem
import com.yaury.awslist.presentation.MobilesViewModel
import com.yaury.awslist.ui.theme.AWSListTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: MobilesViewModel by viewModels<MobilesViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MobilesViewModel(MobilesRepositoryImplementation(RetrofitInstance.api)) as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AWSListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val mobilesList = viewModel.mobiles.collectAsState().value
                        .filter { !it.name.isNullOrBlank() }
                        .sortedBy { it.name.replace("Item ", "").toInt() }
                        .sortedBy { it.listId }

                    val context = LocalContext.current

                    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
                        viewModel.showErrorToastChannel.collectLatest { showError ->
                            if (showError) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    if (mobilesList.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            items(mobilesList.size) { index ->
                                MobileItem(mobilesList[index])
                            }
                        }

                    } else {
                        Text(text = "No mobiles found")
                    }
                }
            }
        }
    }
}

@Composable
fun MobileItem(mobile: MobileItem) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .height(100.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(Color.LightGray)
    ) {
        Text(
            text = "ID: " + mobile.id.toString(),
            modifier = Modifier.padding(start = 32.dp, top = 6.dp),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "List ID: " + mobile.listId.toString(),
            modifier = Modifier.padding(start = 32.dp),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Name: " + mobile.name,
            modifier = Modifier.padding(start = 32.dp),
            fontWeight = FontWeight.SemiBold
        )
    }
}







