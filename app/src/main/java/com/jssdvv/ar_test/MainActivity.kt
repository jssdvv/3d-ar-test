package com.jssdvv.ar_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.jssdvv.ar_test.ui.theme.ARTestTheme
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ARTestTheme {
                Surface() {
                    Scaffold { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            val engine = rememberEngine()
                            val view = rememberView(engine)
                            val childNodes = rememberNodes()
                            val cameraNode = rememberARCameraNode(engine)
                            val modelLoader = rememberModelLoader(engine)

                            var planeRenderer by remember { mutableStateOf(false) }
                            var frame by remember { mutableStateOf<Frame?>(null) }

                            ARScene(
                                modifier = Modifier.fillMaxSize(),
                                engine = engine,
                                childNodes = childNodes,
                                modelLoader = modelLoader,
                                planeRenderer = planeRenderer,
                                /**
                                 * Set the AR Session configuration listed in ARCore documentation.
                                 *
                                 * For instant placement: https://developers.google.com/ar/develop/java/hit-test/developer-guide
                                 * For light Estimation: https://developers.google.com/ar/develop/java/lighting-estimation/developer-guide
                                 * For depth: https://developers.google.com/ar/develop/java/depth/developer-guide
                                 */
                                sessionConfiguration = { session, config ->
                                    config.setInstantPlacementMode(Config.InstantPlacementMode.LOCAL_Y_UP)
                                    config.lightEstimationMode = Config.LightEstimationMode.DISABLED
                                    config.depthMode =
                                        when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                                            true -> Config.DepthMode.AUTOMATIC
                                            else -> Config.DepthMode.DISABLED
                                        }
                                    session.configure(config)
                                },
                                onSessionCreated = {

                                },
                                onSessionUpdated = { session, updatedFrame ->


                                },
                                onSessionFailed = {},


                                ) {

                            }

                            Button(
                                onClick = { planeRenderer = !planeRenderer }
                            ) {
                                Text("Change Plande Renderization")
                            }
                        }
                    }
                }
            }
        }
    }
}