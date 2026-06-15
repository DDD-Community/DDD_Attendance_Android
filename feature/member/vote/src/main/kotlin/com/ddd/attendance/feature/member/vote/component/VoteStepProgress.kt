package com.ddd.attendance.feature.member.vote.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BorderDisabled
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.Typography

/**
 * "STEP n / total" 라벨 + 진행 바.
 */
@Composable
internal fun VoteStepProgress(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val fraction = (current.toFloat() / total).coerceIn(0f, 1f)
    Column(modifier = modifier.fillMaxWidth()) {
        DddText(
            text = "STEP $current / $total",
            style = Typography.bodySmallB,
            color = ButtonEnabled
        )
        Spacer(modifier = Modifier.height(8.dp))
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(BorderDisabled)
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(ButtonEnabled)
            )
        }
    }
}
