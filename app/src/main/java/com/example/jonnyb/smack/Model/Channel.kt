package com.example.jonnyb.smack.Model

/**
 * Created by jonnyb on 9/7/17.
 */
class Channel(val name: String, val description: String, val id: String) {
    override fun toString(): String {
        return "#$name"
    }
}