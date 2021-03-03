package org.geektimes.projects.user.service;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.UserRepository;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/3.
 */
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * 注册用户
     *
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    @Override
    public boolean register(User user) {
        return false;
    }

    /**
     * 注销用户
     *
     * @param user 用户对象
     * @return 成功返回<code>true</code>
     */
    @Override
    public boolean deregister(User user) {
        repository.save(user);
        return false;
    }

    /**
     * 更新用户信息
     *
     * @param user 用户对象
     * @return
     */
    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User queryUserById(Long id) {
        return null;
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return null;
    }
}
