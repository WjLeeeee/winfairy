package com.woojin.winfairy.feature.record

data class VariableInput(
    val category: String,
    val value: String = "",                 //좌석, 유니폼 (단일)
    val values: List<String> = emptyList(), //동반자, 음식 (복수 입력)
    val isMultiple: Boolean = false,        //복수 입력 여부
)
