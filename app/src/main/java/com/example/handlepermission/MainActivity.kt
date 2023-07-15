package com.example.handlepermission

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.handlepermission.ui.theme.HandlePermissionTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HandlePermissionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   val permissionState = rememberMultiplePermissionsState(permissions = listOf(
                       Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO
                   ))
val lifecycleowner =  LocalLifecycleOwner.current
                    DisposableEffect(key1 = lifecycleowner, effect = {
                        val observer = LifecycleEventObserver{ _ , event ->
                            if (event == Lifecycle.Event.ON_RESUME){
                                permissionState.launchMultiplePermissionRequest()
                            }
                        }
                        lifecycleowner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleowner.lifecycle.removeObserver(observer)
                        }
                    }
                     )
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                        permissionState.permissions.forEach(){perm ->
                            when(perm.permission){
                                Manifest.permission.CAMERA ->{
                                    when{
                                        perm.hasPermission
                                     -> { Text(text = "Camera permission granted!")}
                                        perm.shouldShowRationale ->{
                                        Text(text = "please grant camera permission!")}
                                        perm.isPermanentlyDenied() ->{
                                            Text(text = "Camera Permission denied multiple time grant from settings.")
                                        }


                                    }
                                }

                                Manifest.permission.RECORD_AUDIO ->{
                                    when{
                                        perm.hasPermission
                                        -> { Text(text = " Record permission granted!")}
                                        perm.shouldShowRationale ->{
                                            Text(text = "please grant record permission!")}
                                        perm.isPermanentlyDenied() ->{
                                            Text(text = " Record Permission denied multiple time grant from settings.")
                                        }


                                    }
                                }
                            }

                        }

                    }
                }
            }
        }
    }
}

