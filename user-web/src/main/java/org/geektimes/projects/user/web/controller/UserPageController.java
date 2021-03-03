package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.DatabaseUserRepository;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.UserServiceImpl;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * TODO
 *
 * @author C.HD
 * @since 2021/3/2.
 */
@Path("/user")
public class UserPageController implements PageController {

    private final UserService userService = new UserServiceImpl(new DatabaseUserRepository());

    /**
     * 用户注册
     *
     * @param request  HTTP 请求
     * @param response HTTP 相应
     * @return 视图地址路径
     * @throws Throwable 异常发生时
     */
    @Override
    @GET
    @Path("/register")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return "register.jsp";
    }

    @POST
    @Path("/save")
    public String save(HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        userService.deregister(user);
        return "registerSuccess.jsp";
    }
}
