package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.FirestoreService
import com.example.ui.MainSovereignPortal
import com.example.ui.MainViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize Firestore Secure Synchronization Engine
        FirestoreService.initialize(applicationContext)

        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel()
                Surface {
                    MainSovereignPortal(viewModel = viewModel)
                }
            }
        }
    }
}
