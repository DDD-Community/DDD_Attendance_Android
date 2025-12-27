package com.ddd.attendance.feature.core.board

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.core.R
import com.ddd.attendance.feature.core.model.AttendanceUiModel
import com.ddd.attendance.feature.core.model.AttendanceType
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondary
import com.ddd.attendance.feature.designsystem.theme.BorderDisabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.Typography
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AttendanceStatusBoard(
    modifier: Modifier = Modifier,
    items: ImmutableList<AttendanceUiModel>,
    onInfoClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(112.dp)
            .background(
                color = BackgroundSecondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->

                val textColor =
                    if (item.count > 0) item.type.activeColor else TextPrimary

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DddText(
                        text = item.count.toString(),
                        style = Typography.headlineSmallB,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DddText(
                            text = stringResource(item.type.labelRes),
                            style = Typography.bodyMediumM
                        )

                        if (item.type == AttendanceType.ABSENT && item.count > 0) {
                            Spacer(Modifier.width(4.dp))
                            Image(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) { onInfoClick() },
                                painter = painterResource(R.drawable.info),
                                contentDescription = null
                            )
                        }
                    }
                }

                if (index < items.lastIndex) {
                    Spacer(
                        modifier = Modifier
                            .height(48.dp)
                            .width(1.dp)
                            .background(BorderDisabled)
                    )
                }
            }
        }
    }
}