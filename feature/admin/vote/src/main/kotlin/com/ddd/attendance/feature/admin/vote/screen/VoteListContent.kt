package com.ddd.attendance.feature.admin.vote.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.admin.vote.R
import com.ddd.attendance.feature.admin.vote.model.VoteListItemUi
import com.ddd.attendance.feature.admin.vote.model.VoteStatusKind
import com.ddd.attendance.feature.designsystem.component.DddLargeSizeButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.BorderDisabled
import com.ddd.attendance.feature.designsystem.theme.ButtonEnabled
import com.ddd.attendance.feature.designsystem.theme.TextDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Typography
import kotlinx.collections.immutable.ImmutableList

private val OpenChipBg = ButtonEnabled
private val ClosedChipBg = Color(0xFF5C5F62)
private val DraftChipBg = Color(0xFF3A3D3F)

@Composable
internal fun VoteListContent(
    modifier: Modifier = Modifier,
    items: ImmutableList<VoteListItemUi>,
    isLoading: Boolean,
    onItemClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        DddText(
            text = stringResource(R.string.vote_list_title),
            style = Typography.titleLargeB,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                DddText(
                    text = "...",
                    style = Typography.bodyMediumM,
                    color = TextDisabled
                )
            }
        } else if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                DddText(
                    text = stringResource(R.string.vote_list_empty),
                    style = Typography.bodyMediumM,
                    color = TextDisabled
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items, key = { it.voteId }) { item ->
                    VoteListRow(item = item, onClick = { onItemClick(item.voteId) })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        DddLargeSizeButton(
            text = stringResource(R.string.vote_list_back),
            shape = RoundedCornerShape(16.dp),
            height = 58.dp,
            textStyle = Typography.bodyLargeB,
            onClick = onBack
        )
    }
}

@Composable
private fun VoteListRow(
    item: VoteListItemUi,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundSecondaryDark)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            DddText(
                text = item.title.ifBlank { "제목 없음" },
                style = Typography.bodyLargeB,
                color = TextPrimary
            )

            if (item.createdDate.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                DddText(
                    text = item.createdDate.take(10),
                    style = Typography.bodySmallR,
                    color = TextSecondaryDark
                )
            }
        }

        StatusChip(item.statusLabel, item.statusKind)
    }
}

@Composable
private fun StatusChip(label: String, kind: VoteStatusKind) {
    val bg = when (kind) {
        VoteStatusKind.OPEN -> OpenChipBg
        VoteStatusKind.CLOSED -> ClosedChipBg
        VoteStatusKind.DRAFT -> DraftChipBg
        VoteStatusKind.UNKNOWN -> BorderDisabled
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        DddText(
            text = label,
            style = Typography.bodySmallM,
            color = TextPrimary
        )
    }
}
