package com.ddd.attendance.feature.member.vote.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BorderInactive
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryLight
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.member.vote.model.TeamUiModel

/**
 * 팀 선택 행. 본인 팀은 비활성 + "본인 팀" 뱃지.
 */
@Composable
internal fun TeamSelectRow(
    team: TeamUiModel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val enabled = !team.isOwnTeam
    val titleColor = if (enabled) TextPrimary else TextDisabled
    val subtitleColor = if (enabled) TextSecondaryDark else TextDisabled

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            DddText(text = team.name, style = Typography.bodyMediumM, color = titleColor)
            if (!team.serviceName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                DddText(text = team.serviceName, style = Typography.bodySmallR, color = subtitleColor)
            }
        }

        if (team.isOwnTeam) {
            OwnTeamBadge()
        } else {
            CheckBox(isSelected = isSelected)
        }
    }
}

@Composable
private fun CheckBox(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) ButtonEnabled else androidx.compose.ui.graphics.Color.Transparent)
            .border(
                width = 1.dp,
                color = if (isSelected) ButtonEnabled else BorderInactive,
                shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(TextPrimary)
            )
        }
    }
}

@Composable
private fun OwnTeamBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(TextDisabled)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        DddText(text = "본인 팀", style = Typography.bodySmallR, color = TextSecondaryLight)
    }
}
