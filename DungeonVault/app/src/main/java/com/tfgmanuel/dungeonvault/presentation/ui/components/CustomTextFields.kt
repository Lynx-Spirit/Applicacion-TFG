package com.tfgmanuel.dungeonvault.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Campo de texto custom para los formularios.
 *
 * @param modifier Modificador para ajustar el diseño del componente.
 * @param shape Forma del borde del campo de texto (por defecto esquinas redondeadas).
 * @param focusedBorderColor Color del borde cuando el campo está enfocado.
 * @param unfocusedBorderColor Color del borde cuando el campo no está enfocado.
 * @param focusedLabelColor Color de la etiqueta cuando el campo está enfocado.
 * @param unfocusedLabelColor Color de la etiqueta cuando el campo no está enfocado.
 * @param focusedTextColor Color del texto cuando el campo está enfocado.
 * @param unfocusedTextColor Color del texto cuando el campo no está enfocado.
 * @param minLines Número mínimo de líneas visibles.
 * @param value Valor actual del campo de texto.
 * @param textLabel Etiqueta (placeholder persistente) que describe el contenido esperado.
 * @param onValueChange Función que se ejecuta al cambiar el valor del campo.
 * @param isPassword Indica si el campo es para una contraseña (oculta el texto).
 * @param singleLine Indica si el campo debe ocupar una sola línea.
 * @param isError Indica si el campo está en estado de error (cambia estilos visuales).
 * @param textError Texto de error que se muestra debajo del campo si [isError] es true.
 * @param keyboardType Tipo de teclado a mostrar (texto, número, email, etc.).
 */
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    focusedBorderColor: Color = Color(0xFFFFA726),
    unfocusedBorderColor: Color = Color(0xFF1A1A1A),
    focusedLabelColor: Color = Color(0xFF1A1A1A),
    unfocusedLabelColor: Color = Color(0xFF1A1A1A),
    focusedTextColor: Color = Color(0xFFCDCDCD),
    unfocusedTextColor: Color = Color(0xFFCDCDCD),
    minLines: Int = 1,
    value: String,
    textLabel: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    isError: Boolean = false,
    textError: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        modifier = modifier,
        shape = shape,
        value = value,
        label = {
            Text(
                text = textLabel,
                color = Color.White
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            focusedLabelColor = focusedLabelColor,
            unfocusedLabelColor = unfocusedLabelColor,
            focusedTextColor = focusedTextColor,
            unfocusedTextColor = unfocusedTextColor
        ),
        minLines = minLines,
        supportingText = {
            if (textError != null) {
                Text(
                    text = textError,
                    fontSize = 10.sp
                )
            }
        },
        onValueChange = onValueChange,
        singleLine = singleLine,
        isError = isError,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 32.sp
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeHolder,
                    fontSize = 32.sp,
                    color = Color.Gray
                )
            }
            innerTextField()
        }
    )
}