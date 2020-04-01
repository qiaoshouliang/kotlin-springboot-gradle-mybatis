package com.casair.cba_work.service

import com.baomidou.mybatisplus.extension.service.IService
import com.casair.cba_work.domain.User



interface UserService:IService<User> {

    fun getUser(id: String):User
}