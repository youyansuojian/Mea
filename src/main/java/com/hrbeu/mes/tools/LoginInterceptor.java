package com.hrbeu.mes.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hrbeu.mes.pojo.User;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        // 判断session
		/*HttpSession session = request.getSession();
		// 从session中取出用户份信息
		User user = (User) session.getAttribute(Constants.USER_SESSION);
		if (user == null) {
			// 执行这里表示用户身份需要验证，跳转到登录界面
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			// return false表示拦截，不向下执行
			return false;
		}*/
        // return true表示放行 身份存在，放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }
}
