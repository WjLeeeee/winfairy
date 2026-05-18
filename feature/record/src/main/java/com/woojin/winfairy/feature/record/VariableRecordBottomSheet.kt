package com.woojin.winfairy.feature.record

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VariableRecordBottomSheet(
    variables: List<VariableInput>,
    suggestions: Map<String, List<String>>,
    onVariableChange: (Int, String) -> Unit,
    onVariableAdd: (Int, String) -> Unit,
    onVariableRemove: (Int, String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 40.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    stringResource(R.string.record_variables),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(stringResource(R.string.options), fontSize = 11.sp, color = Color(0xFFAAAAAA))
            }

            variables.forEachIndexed { index, variable ->
                if (variable.isMultiple) {
                    MultipleVariableSection(
                        category = variable.category,
                        values = variable.values,
                        suggestions = suggestions[variable.category] ?: emptyList(),
                        onAdd = { onVariableAdd(index, it) },
                        onRemove = { onVariableRemove(index, it) },
                    )
                } else {
                    SingleVariableSection(
                        category = variable.category,
                        value = variable.value,
                        suggestions = suggestions[variable.category] ?: emptyList(),
                        onValueChange = { onVariableChange(index, it) },
                    )
                }
            }

            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.save_record), fontSize = 14.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun MultipleVariableSection(
    category: String,
    values: List<String>,
    suggestions: List<String>,
    onAdd: (String) -> Unit,
    onRemove: (String) -> Unit,
) {
    var showInput by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val filteredSuggestions = suggestions
        .filter { it !in values }
        .filter { inputText.isNotEmpty() && it.contains(inputText, ignoreCase = true) }
        .take(3)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "$category  ", fontSize = 11.sp, color = Color(0xFF888888))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            values.forEach { value ->
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = value, fontSize = 12.sp, color = Color.White)
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(14.dp)
                            .clickable { onRemove(value) }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.5.dp, Color(0xFFDDDDDD), RoundedCornerShape(16.dp))
                    .clickable { showInput = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(stringResource(R.string.add_more), fontSize = 12.sp, color = Color(0xFFBBBBBB))
            }
        }
        if (showInput) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                1.5.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(8.dp)
                            )
                            .background(Color.White)
                            .onFocusChanged { isFocused = it.isFocused }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (inputText.isNotBlank()) {
                                    onAdd(inputText.trim())
                                    inputText = ""
                                    showInput = false
                                }
                            }
                        ),
                        decorationBox = { inner ->
                            if (inputText.isEmpty()) {
                                Text(
                                    stringResource(R.string.base_text_field),
                                    fontSize = 12.sp,
                                    color = Color(0xFFCCCCCC)
                                )
                            }
                            inner()
                        }
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                if (inputText.isNotBlank()) {
                                    onAdd(inputText.trim())
                                    inputText = ""
                                    showInput = false
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(stringResource(R.string.add), fontSize = 12.sp, color = Color.White)
                    }
                }
                if (isFocused && filteredSuggestions.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
                            .background(Color.White)
                            .border(
                                0.5.dp,
                                Color(0xFFEEEEEE),
                                RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)
                            )
                    ) {
                        filteredSuggestions.forEach { suggestion ->
                            Text(
                                text = suggestion,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onAdd(suggestion)
                                        inputText = ""
                                        showInput = false
                                    }
                                    .padding(12.dp, 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SingleVariableSection(
    category: String,
    value: String,
    suggestions: List<String>,
    onValueChange: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val filteredSuggestions = suggestions
        .filter { value.isNotEmpty() && it.contains(value, ignoreCase = true) }
        .filter { it != value }
        .take(3)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = category, fontSize = 11.sp, color = Color(0xFF888888))
        Column {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5))
                    .onFocusChanged { isFocused = it.isFocused }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                singleLine = true,
                decorationBox = { inner ->
                    if (value.isEmpty()) {
                        Text(
                            stringResource(R.string.base_text_field),
                            fontSize = 12.sp,
                            color = Color(0xFFCCCCCC)
                        )
                    }
                    inner()
                }
            )
            if (isFocused && filteredSuggestions.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
                        .background(Color.White)
                        .border(
                            0.5.dp,
                            Color(0xFFEEEEEE),
                            RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)
                        )
                ) {
                    filteredSuggestions.forEach { suggestion ->
                        Text(
                            text = suggestion,
                            fontSize = 12.sp,
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
}