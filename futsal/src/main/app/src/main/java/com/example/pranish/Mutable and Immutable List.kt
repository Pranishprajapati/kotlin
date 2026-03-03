package com.example.pranish

fun main(){
    // Immutable List
    val lst = listOf("one", "two", "three")
    println("Mutable list")
    for(i in lst.indices){
        println(lst[i])
    }

    println()

    // Mutable List
    val mutableLst = mutableListOf("one", "two", "three")
    mutableLst.add("Four")
    println("Immutable list")
    for(i in mutableLst.indices){
        println(mutableLst[i])
    }
}