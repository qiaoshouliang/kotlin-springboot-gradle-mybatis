package com.casair.cba_work.dto


data class AppLoginDTO(val userName: String, val password: String) {
    constructor() : this("", "")
}