package com.example.app.dto

data class SpringBeanResource(val name: String, val type: String)

fun Any.beanResource(name: String) = SpringBeanResource(name, this.javaClass.name)
