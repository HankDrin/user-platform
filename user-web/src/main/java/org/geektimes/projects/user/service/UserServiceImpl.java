package org.geektimes.projects.user.service;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.UserRepository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/3.
 */
public class UserServiceImpl implements UserService {

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

    @Resource(name = "bean/Validator")
    private Validator validator;

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
        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        if (!violationSet.isEmpty()) {
            throw new RuntimeException(violationSet.stream().map(ConstraintViolation::getMessage).collect(
                    Collectors.joining(" | ")));
        }
        entityManager.persist(user);
        return true;
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
