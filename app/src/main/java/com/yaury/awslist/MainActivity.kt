package com.yaury.awslist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
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
        //enableEdgeToEdge()
        setContent {
            AWSListTheme {
                Surface(){
                   val mobilesList = viewModel.mobiles.collectAsState().value
                    val context = LocalContext.current

                    LaunchedEffect(Unit) {
                        viewModel.showErrorToastChannel.collectLatest { showError ->
                            if (showError) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    if (mobilesList.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(mobilesList.size) { index ->
                                MobileItem(mobilesList[index])
                                Spacer(modifier = Modifier.padding(8.dp))
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
        modifier = Modifier.clip(RoundedCornerShape(8.dp))
            .height(150.dp)
            .fillMaxWidth()
    ) {
        Text(text = mobile.listId.toString())
        Text(text = mobile.id.toString())
        Text(text = mobile.name)
    }
}







