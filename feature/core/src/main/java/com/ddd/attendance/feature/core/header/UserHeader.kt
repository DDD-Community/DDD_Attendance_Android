package com.ddd.attendance.feature.core.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.core.R
import com.ddd.attendance.feature.core.model.UserType
import com.ddd.attendance.feature.designsystem.component.DddIconButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.Typography

@Composable
fun UserHeader(
    modifier: Modifier = Modifier,
    type: UserType,
    text: String = stringResource(R.string.attendance),
    onClick:() -> Unit = {}
) {
    val isAdmin = when(type) {
        UserType.Admin -> true
        UserType.Member -> false
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(start = if (isAdmin) 24.dp else 16.dp, end = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isAdmin) {
            Row(
                modifier = modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClick()
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                DddText(
                    text = text,
                    style = Typography.titleMediumB
                )

                Spacer(modifier = Modifier.width(2.dp))

                Image(
                    modifier = modifier,
                    painter = painterResource(id = R.drawable.ic_bottom_arrow_white),
                    contentDescription = "UI 선택"
                )
            }

        } else {
            Image(
                modifier = Modifier.size(44.dp),
                painter = painterResource(id = R.drawable.ic_logo_ddd),
                contentDescription = "앱 로고",
            )
        }

        Spacer(modifier = Modifier.weight(1F))

        DddIconButton(
            modifier = Modifier
                .size(36.dp),
            enabledIconRes = R.drawable.ic_qr,
            disabledIconRes = R.drawable.ic_qr
        )

        Spacer(modifier = Modifier.width(12.dp))

        DddIconButton(
            modifier = Modifier
                .size(36.dp),
            enabledIconRes = R.drawable.ic_profile,
            disabledIconRes = R.drawable.ic_profile,
        )
    }
}