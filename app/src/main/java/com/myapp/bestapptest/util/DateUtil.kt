package com.myapp.bestapptest.util

fun String.toDate(): String {

    return this.replace('-', '/')
        .replace('T', ' ')
        .substringBeforeLast(':')

}