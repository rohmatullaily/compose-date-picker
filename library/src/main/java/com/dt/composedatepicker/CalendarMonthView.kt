package com.dt.composedatepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun CalendarMonthView(
    monthList:List<MonthData>,
    selectedMonth: MonthData,
    setMonth: (MonthData) -> Unit,
    minMonth: Int,
    maxMonth: Int,
    minYear:Int,
    maxYear:Int,
    selectedYear:Int,
    setShowMonths:(Boolean)->Unit,
    setHeight:(Int)->Unit,
    showOnlyMonth:Boolean,
    themeColor:Color,
    monthViewType: MonthViewType?
) {

    val NUMBER_OF_ROW_ITEMS = 3
    var numberOfElement = 0

    LazyColumn(modifier = Modifier
        .padding(horizontal = 20.dp)
        .padding(vertical = 20.dp)
        .onGloballyPositioned { setHeight(it.size.height) }) {
        items(items = monthList.chunked(NUMBER_OF_ROW_ITEMS)) { rowItems ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                for ((index, item) in rowItems.withIndex()) {
                    MonthItem(month = item,
                        index = index,
                        numberOfElement = numberOfElement,
                        rowSize = NUMBER_OF_ROW_ITEMS,
                        selectedMonth = selectedMonth.name,
                        setMonth = setMonth,
                        minMonth = minMonth,
                        maxMonth = maxMonth,
                        minYear = minYear,
                        maxYear = maxYear,
                        selectedYear = selectedYear,
                        setShowMonths = setShowMonths,
                        showOnlyMonth = showOnlyMonth,
                        themeColor = themeColor,
                        monthViewType = monthViewType
                    )
                    numberOfElement += 1
                }
            }
        }
    }
}

@Composable
fun MonthItem(
    month: MonthData,
    selectedMonth: String,
    setMonth: (MonthData) -> Unit,
    index: Int,
    numberOfElement: Int,
    rowSize: Int,
    minMonth: Int,
    maxMonth: Int,
    minYear: Int,
    maxYear: Int,
    selectedYear: Int,
    setShowMonths: (Boolean) -> Unit,
    showOnlyMonth: Boolean,
    themeColor:Color,
    monthViewType: MonthViewType?
) {
    val enabled = checkDate(minYear = minYear,maxYear = maxYear,selectedYear = selectedYear,maxMonth = maxMonth,minMonth = minMonth,numberOfElement = numberOfElement)
    val monthText:String = when(monthViewType){
        MonthViewType.ONLY_MONTH -> month.name.uppercase()
        MonthViewType.ONLY_NUMBER -> numberOfElement.plus(1).toString()
        MonthViewType.BOTH_NUMBER_AND_MONTH -> month.name.uppercase() + " " + "(${numberOfElement.plus(1)})"
        else -> month.name.uppercase()
    }
    Box(modifier = Modifier
        .background(color = if (month.name == selectedMonth) themeColor else Color.Transparent,
            shape = RoundedCornerShape(100))
        .fillMaxWidth(1f / (rowSize - index + 1f))
        .aspectRatio(1f)
        .clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() },
            enabled = enabled) {
            setMonth(month)
            if (!showOnlyMonth) {
                setShowMonths(false)
            }
        },
        contentAlignment = Alignment.Center) {
        Text(text = monthText,
            color = if (enabled && month.name == selectedMonth) Color.White
            else if (enabled) Color.Black
            else Color.Gray)
    }
}
private fun checkDate(minYear: Int,maxYear: Int,selectedYear: Int,minMonth: Int,maxMonth: Int,numberOfElement: Int):Boolean{
//    if(minMonth==0) return true
    if (minYear == maxYear) return numberOfElement in minMonth..maxMonth
    if (selectedYear==minYear){
        return numberOfElement>=minMonth
    }
    else if (selectedYear==maxYear) {
        if (numberOfElement>maxMonth) return false
    }
    return true
}