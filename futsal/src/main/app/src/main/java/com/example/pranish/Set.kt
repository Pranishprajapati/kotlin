package com.example.pranish

fun set(){
    val numbers = setOf(1, 2, 3, 4)
    for(elements in numbers){
        println(elements)
    }

    val numbersBackwards = setOf(4, 3, 2, 1)
    println("The sets are equal: ${numbers == numbersBackwards}")
}