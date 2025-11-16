package com.example.classscheduler.ui.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.classscheduler.R
import com.example.classscheduler.ui.theme.DarkBlue

@Composable
fun AuthWithGoogleButton(
    @StringRes label: Int,
    onSignInWithGoogleClicked: () -> Unit,
): Unit{
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        onClick = onSignInWithGoogleClicked,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = DarkBlue,
            contentColor = Color.White
        ),
        border = BorderStroke(1.dp, DarkBlue)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(R.drawable.google_logo),
                contentScale = ContentScale.Fit,
                contentDescription = null
            )

            Text(
                text = stringResource(label),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                letterSpacing = 0.1.em
            )
        }
    }
}