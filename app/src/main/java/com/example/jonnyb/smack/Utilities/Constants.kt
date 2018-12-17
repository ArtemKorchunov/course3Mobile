package com.example.jonnyb.smack.Utilities

/**
 * Created by jonnyb on 9/1/17.
 */

const val BASE_URL = "http://192.168.0.108:4000/"
//const val BASE_URL = "http://10.0.2.2:3005/v1/"
const val SOCKET_URL = "https://devslopes-chattin.herokuapp.com/"

const val URL_REGISTER = "${BASE_URL}oauth/token"
const val URL_LOGIN = "${BASE_URL}oauth/login"
const val URL_GET_DEVICE = "${BASE_URL}device"

const val URL_CREATE_USER = "${BASE_URL}user/add"
const val URL_GET_USER = "${BASE_URL}user/byEmail/"
const val URL_GET_CHANNELS = "${BASE_URL}channel/"
const val URL_GET_MESSAGES = "${BASE_URL}message/byChannel/"

// Broadcast Constants
const val BROADCAST_USER_DATA_CHANGE = "BROADCAST_USER_DATA_CHANGE"