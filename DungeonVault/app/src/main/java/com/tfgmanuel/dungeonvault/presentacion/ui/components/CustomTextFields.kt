package com.tfgmanuel.dungeonvault.presentacion.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CustomTextField (
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    focusedBorderColor: Color = Color(0xFFFFA726),
    unfocusedBorderColor : Color = Color(0xFF1A1A1A),
    focusedLabelColor: Color = Color(0xFF1A1A1A),
    unfocusedLabelColor: Color = Color(0xFF1A1A1A),
    focusedTextColor: Color = Color(0xFFCDCDCD),
    unfocusedTextColor: Color = Color(0xFFCDCDCD),
    value: String,
    textLabel: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    isError: Boolean = false,
    textError: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField (
        modifier = modifier,
        shape = shape,
        value = value,
        label = {Text(text = textLabel, color = Color.White)},
        colors = OutlinedTextFieldDefaults.colors (
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            focusedLabelColor = focusedLabelColor,
            unfocusedLabelColor = unfocusedLabelColor,
            focusedTextColor = focusedTextColor,
            unfocusedTextColor = unfocusedTextColor
        ),
        supportingText = {
            if (textError != null) {
                Text(text = textError, fontSize = 10.sp)
            }
        },
        onValueChange = onValueChange,
        singleLine = singleLine,
        isError = isError,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}