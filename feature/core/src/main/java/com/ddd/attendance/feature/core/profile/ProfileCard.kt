package com.ddd.attendance.feature.core.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ddd.attendance.feature.core.R

data class ProfileData(
    val name: String,
    val position: String,
    val team: String,
    val generation: String,
    val organization: String
)

@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    profileData: ProfileData,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(580.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFF1976D2)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp)
    ) {
        Column {
            // 상단 태그들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileTag(
                    text = stringResource(R.string.member),
                    backgroundColor = Color.Transparent,
                    textColor = Color(0xFF0D82F9),
                    borderColor = Color(0xFF0D82F9),
                )
                
                ProfileTag(
                    text = stringResource(R.string.edit_generation),
                    backgroundColor = Color(0xB20D82F9),
                    textColor = Color.White,
                    iconDrawableRes = R.drawable.ic_edit,
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // 이름
            Text(
                text = profileData.name,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.weight(1F))
            
            // 정보 섹션들
            ProfileInfoSection(
                label = stringResource(R.string.job_role),
                value = profileData.position
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            ProfileInfoSection(
                label = stringResource(R.string.team),
                value = profileData.team
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            ProfileInfoSection(
                label = stringResource(R.string.generation),
                value = profileData.generation
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // 하단 조직명
            Text(
                text = profileData.organization,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF525252),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProfileTag(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color? = null,
    @DrawableRes iconDrawableRes: Int? = null
) {
    Box(
        modifier = Modifier
            .background(
                backgroundColor,
                RoundedCornerShape(16.dp)
            )
            .then(
                if (borderColor != null) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            if (iconDrawableRes != null) {
                Icon(
                    painter = painterResource(id = iconDrawableRes),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = textColor
                )
            }

            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}

@Composable
private fun ProfileInfoSection(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF5E5E5E)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF202325)
        )
    }
}