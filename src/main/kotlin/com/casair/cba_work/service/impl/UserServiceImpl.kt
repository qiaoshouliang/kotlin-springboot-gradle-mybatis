package com.casair.cba_work.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.casair.cba_work.domain.User
import com.casair.cba_work.repository.UserRepository
import com.casair.cba_work.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(val userRepository: UserRepository): ServiceImpl<UserRepository, User>(), UserService {

    override fun getUser(id: String):User {
       return userRepository.selectById(id)
    }
}