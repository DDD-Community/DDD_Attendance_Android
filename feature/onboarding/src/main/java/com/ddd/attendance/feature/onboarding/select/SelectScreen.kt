package com.ddd.attendance.feature.onboarding.select

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ddd.attendance.feature.designsystem.component.DddIconButton
import com.ddd.attendance.feature.designsystem.component.DddText
import com.ddd.attendance.feature.designsystem.theme.BackgroundSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.BorderEnabled
import com.ddd.attendance.feature.designsystem.theme.TextPrimary
import com.ddd.attendance.feature.designsystem.theme.TextSecondaryDark
import com.ddd.attendance.feature.designsystem.theme.Transparent
import com.ddd.attendance.feature.designsystem.theme.Typography
import com.ddd.attendance.feature.onboarding.OnBoardingStep
import com.ddd.attendance.feature.onboarding.R
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SelectionScreen(
    step: OnBoardingStep,
    items: ImmutableList<SelectItemUiModel>,
    onClick:(position: Int) -> Unit,
) {
    Content(
        step = step,
        items = items,
        onClick = {
            onClick(it)
        }
    )
}

@Composable
internal fun Content(
    step: OnBoardingStep,
    items: ImmutableList<SelectItemUiModel>,
    onClick:(position: Int) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        DddText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = step.titleRes),
            style = Typography.titleLargeB,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        DddText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = step.subTitleRes),
            style = Typography.bodySmallM,
            color = TextSecondaryDark,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = items,
                key = { _, item -> "${item.text}_${item.id}" }
            ) { index, item ->
                SelectBox(
                    text = item.text,
                    isSelected = item.isSelected,
                    onClick = {
                        onClick(index)
                    }
                )
            }
        }
    }
}

@Composable
internal fun SelectBox(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BackgroundSecondaryDark)
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) BorderEnabled else Transparent
            )
            .clickable { onClick() }
            .fillMaxWidth()
            .height(58.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DddText(
            modifier = Modifier.weight(1f),
            text = text,
            style = Typography.bodyLargeM,
        )

        DddIconButton(
            modifier = Modifier
                .size(24.dp),
            isSelected = isSelected,
            enabledIconRes = R.drawable.radio_button_enable,
            disabledIconRes = R.drawable.radio_button_disable,
        )
    }
}