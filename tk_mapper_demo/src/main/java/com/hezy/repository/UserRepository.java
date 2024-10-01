package com.hezy.repository;

import com.hezy.pojo.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserRepository extends Mapper<User> {
}
