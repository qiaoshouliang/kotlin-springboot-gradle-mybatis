package com.casair.cba_work.repository
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.casair.cba_work.domain.User
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : BaseMapper<User>