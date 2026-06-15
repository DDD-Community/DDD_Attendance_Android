package com.ddd.attendance.feature.member.vote.step

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.component.DddLargeSizeButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography

@Composable
internal fun VoteCompleteScreen(
    onConfirm: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(ButtonEnabled),
                contentAlignment = Alignment.Center
            ) {
                DddText(text = "✓", style = Typography.headlineLargeB, color = TextPrimary)
            }

            Spacer(modifier = Modifier.height(24.dp))
            DddText(text = "투표 완료!", style = Typography.titleLargeB, color = TextPrimary)

            Spacer(modifier = Modifier.height(12.dp))
            DddText(
                text = "소중한 한 표를 보내주셔서 감사해요.\nDDD 13기 모두 정말 고생 많았어요 🎉",
                style = Typography.bodySmallM,
                color = TextSecondaryDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
            DddText(
                text = "결과는 최종 발표에서 공개될 예정이에요.",
                style = Typography.bodySmallR,
                color = TextDisabled,
                textAlign = TextAlign.Center
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {
            DddLargeSizeButton(
                text = "확인",
                isEnabled = true,
                onClick = onConfirm
            )
        }
    }
}
