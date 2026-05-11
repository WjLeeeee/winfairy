package com.woojin.winfairy.feature.record

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SingleVariableInput(
    category: String,
    value: String,
    suggestions: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val filteredSuggestions = suggestions
        .filter { value.isNotEmpty() && it.contains(value, ignoreCase = true) }
        .filter { it != value }
        .take(3)

    Column(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .onFocusChanged { isFocused = it.isFocused }
                .padding(12.dp, 8.dp),
            textStyle = TextStyle(
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            text = "$category 입력",
                            fontSize = 13.sp,
                            color = Color(0xFFCCCCCC)
                        )
                    }
                    innerTextField()
                }
            }
        )
        if (isFocused && filteredSuggestions.isNotEmpty() && value.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
                    .background(Color.White)
                    .border(0.5.dp, Color(0xFFEEEEEE), RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))
            ) {
                filteredSuggestions.forEach { suggestion ->
                    Text(
                        text = suggestion,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onValueChange(suggestion) }
                            .padding(12.dp, 10.dp)
                    )
                }
            }
        }
    }
}