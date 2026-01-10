package com.ddd.attendance.feature.core.contributor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ddd.attendance.feature.core.R
import com.ddd.attendance.feature.designsystem.component.DddLargeSizeButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundDefault
import com.ddd.attendance.feature.designsystem.theme.BorderInactive
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContributorBottomSheet(
    modifier: Modifier = Modifier,
    isShow: Boolean = true,
    dragHandleColor: Color = Color(0xFF323537),
    backgroundColor: Color = BackgroundDefault,
    onFeedback: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!isShow) return

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(color = backgroundColor),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .background(
                            color = dragHandleColor,
                            shape = RoundedCornerShape(11.dp)
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                DddText(
                    text = stringResource(R.string.contributors),
                    style = Typography.titleMediumB
                )

                Spacer(modifier = Modifier.height(24.dp))

                ContributorBlock(
                    role = "PM",
                    name = persistentListOf<String>("이경서", "최현희")
                )

                Spacer(modifier = Modifier.height(24.dp))

                ContributorBlock(
                    role = "Design",
                    name = persistentListOf<String>("강동길, 이지윤, 조재인")
                )

                Spacer(modifier = Modifier.height(24.dp))

                ContributorBlock(
                    role = "iOS",
                    name = persistentListOf<String>("서원지, 홍은표")
                )

                Spacer(modifier = Modifier.height(24.dp))

                ContributorBlock(
                    role = "Android",
                    name = persistentListOf<String>("오세민, 이상훈")
                )

                Spacer(modifier = Modifier.height(24.dp))

                ContributorBlock(
                    role = "Server",
                    name = persistentListOf<String>("조지원, 이준석")
                )

                Spacer(modifier = Modifier.height(36.dp))

                DddLargeSizeButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    height = 58.dp,
                    text = stringResource(R.string.app_feedback),
                    borderColor = BorderInactive,
                    enabledColor = BackgroundDefault,
                    disabledColor = BackgroundDefault,
                    borderWidth = 1.dp,
                    onClick = onFeedback
                )

                Spacer(modifier = Modifier.height(8.dp))

                DddLargeSizeButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    height = 58.dp,
                    text = stringResource(R.string.confirm),
                    onClick = onDismiss
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun ContributorBlock(
    modifier: Modifier = Modifier,
    role: String,
    name: ImmutableList<String>
) {
    Column(
        modifier = modifier
    ) {
        DddText(
            modifier = Modifier.fillMaxWidth(),
            text = role,
            color = TextSecondaryDark,
            style = Typography.bodySmallR,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        DddText(
            modifier = Modifier.fillMaxWidth(),
            text = name.joinToString(", "),
            style = Typography.titleSmallM,
            textAlign = TextAlign.Center
        )
    }
}