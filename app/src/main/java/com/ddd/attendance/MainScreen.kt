package com.ddd.attendance

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import com.ddd.attendance.app.R
import com.ddd.attendance.domain.model.UsersException
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    onRestart: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    val localContextResource = LocalResources.current

    val onShowErrorSnackBar: (throwable: Throwable?) -> Unit = { throwable ->
        coroutineScope.launch {
            val message = when (throwable) {
                is UsersException -> throwable.errorMessage
                is UnknownHostException -> localContextResource.getString(R.string.error_message_network)
                else -> localContextResource.getString(R.string.error_message_unknown)
            }

            snackBarHostState.showSnackbar(message)
        }
    }

    MainScreenContent(
        navigator = navigator,
        onShowErrorSnackBar = onShowErrorSnackBar,
        snackBarHostState = snackBarHostState,
        onRestart = onRestart
    )
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    snackBarHostState: SnackbarHostState,
    onRestart: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        content = { padding ->
            MainNavHost(
                navigator = navigator,
                padding = padding,
                onShowErrorSnackBar = onShowErrorSnackBar,
                onRestart = onRestart
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    )
}
