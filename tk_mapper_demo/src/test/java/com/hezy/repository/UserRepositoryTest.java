package com.hezy.repository;

import com.hezy.pojo.User;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

/**
 * @author 10765
 * @create 2024/10/1 14:56
 */
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void insertUserTest() {
        // 1.全字段插入
        User user1 = new User();
        user1.setUsername("wangwu");
        user1.setPassword("123465");
        userRepository.insert(user1);

        // 2.部分字段插入
        User user2 = new User();
        user2.setUsername("zhaoliu");
        userRepository.insertSelective(user2);
    }

    @Test
    public void deleteUserTest() {
        // 1.根据对象删除，用这种方式最好给对象set一个唯一约束的字段，不然会删掉所有password=123456的记录
        User user1 = new User();
        user1.setPassword("123456");
        userRepository.delete(user1);

        // 2.根据主键删除
        userRepository.deleteByPrimaryKey(5);

        // 3.构建条件删除，username等于zhaoliu的记录
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", "zhaoliu");
        userRepository.deleteByExample(example);
    }

    @Test
    public void updateUserTest() {
        // 1.根据主键全字段更新，未设置的字段为null或者用数据库默认值
        User user1 = new User();
        user1.setId(4);
        user1.setUsername("zhangsan");
        user1.setPassword("123456");
        userRepository.updateByPrimaryKey(user1);

        // 2.根据主键部分字段更新，未设置的字段保持原样
        User user2 = new User();
        user2.setId(4);
        user2.setUsername("lisi");
        userRepository.updateByPrimaryKeySelective(user2);

        // 3.构建条件全字段更新，更新username=lisi的记录，password字段为123456，username字段未设置，更新后，username=null
        User user3 = new User();
        user3.setPassword("123456");
        Example example1 = new Example(User.class);
        example1.createCriteria().andEqualTo("username", "lisi");
        userRepository.updateByExample(user3, example1);

        // 4.构建条件部分字段更新，更新username=lisi的记录，password字段为123456，username字段未设置，更新后，username保持原样
        User user4 = new User();
        user4.setPassword("123456");
        Example example2 = new Example(User.class);
        example2.createCriteria().andEqualTo("username", "lisi");
        userRepository.updateByExampleSelective(user4, example2);
    }

    @Test
    public void selectUserTest() {
        // 1.全查
        List<User> users = userRepository.selectAll();
        System.out.println(users);

        // 2.根据主键查
        User user = userRepository.selectByPrimaryKey(4);
        System.out.println(user);

        // 3.查询password=123456的一条记录，如果有多个会报TooManyResultsException
        User user2 = new User();
        user2.setPassword("123456");
        User user3 = userRepository.selectOne(user2);
        System.out.println(user3);

        // 4.统计password=123456的记录数
        User user4 = new User();
        user4.setPassword("123456");
        int count1 = userRepository.selectCount(user4);
        System.out.println(count1);

        // 5.构建条件查询，username字段为null的记录
        Example example1 = new Example(User.class);
        example1.createCriteria().andIsNull("username");
        List<User> userList = userRepository.selectByExample(example1);
        System.out.println(userList);

        // 6.构建条件统计，统计记录以li开头的记录，注意这里andLike的value里面写的是like后面的完整部分，要带上通配符
        Example example2 = new Example(User.class);
        example2.createCriteria().andLike("username", "li%");
        int count2 = userRepository.selectCountByExample(example2);
        System.out.println(count2);

        // 7.分页查询，假设每页显示2条，查询第2页的内容
        int pageIndex = 2;
        int pageSize = 2;
        int offset = (pageIndex - 1) * pageSize;
        RowBounds rowBounds = new RowBounds(offset, pageSize);
        List<User> bounds = userRepository.selectByRowBounds(new User(), rowBounds);
        System.out.println(bounds);

        // 8.根据条件分页查询，查询password=123456的记录，并且按照每页显示2条，查询第2页的内容
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("password", "123456");
        List<User> andRowBounds = userRepository.selectByExampleAndRowBounds(example, rowBounds);
        System.out.println(andRowBounds);
    }
}
