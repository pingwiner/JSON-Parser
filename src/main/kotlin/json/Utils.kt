package org.example.json

import kotlin.text.toInt

fun longToInt(long: Long): Int? = if (long >= Int.MIN_VALUE && long <= Int.MAX_VALUE)
    long.toInt()
else
    null

fun doubleToFloat(double: Double) = if (double >= Float.MIN_VALUE && double <= Float.MAX_VALUE)
    double.toFloat()
else
    null


