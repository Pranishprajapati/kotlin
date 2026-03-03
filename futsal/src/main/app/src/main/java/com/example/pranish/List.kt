package com.example.pranish

fun displayList(){
    val numbers : List<String> = listOf("one", "two", "three", "four")

    println("Number of elements: ${numbers.size}")
    println("Third element: ${numbers.get(2)}")
    println("Fourth element: ${numbers[3]}")
    println("Index of element \"two\" ${numbers.indexOf("two")}")
}