package com.an.core.service;

import com.an.core.exception.ForbiddenException;
import com.an.core.exception.UnauthorizedException;
import com.an.sys.entity.User;
import com.an.utils.Util;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户权限过滤器(用户身份及权限验证)
 *
 * @author Karas
 */
public class SecurityFilter extends HttpServlet implements Filter {
    private static final long serialVersionUID = -4275105240038370264L;
    private static Logger logger = Logger.getLogger(SecurityFilter.class);

    private static final String exclude = "sys/login.html,sec/logout,sec/user,sec/login,sec/passwd";
    public static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public void init(FilterConfig config) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = Util.nvl(request.getRequestURI(), "");
        String method = request.getMethod();
        String ip = request.getRemoteAddr();
        String username = " ";

        String regEx = "(?<=" + request.getContextPath() + "/)[^/]+(/[^/]+|$)";
        String act = "";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(uri.replace("/rest/", "/"));
        if (matcher.find())
            act = matcher.group();

        // 需验证区域
        if (act.isEmpty() || !exclude.contains(act)) {

            try {
                HttpSession session = request.getSession();
                if (session == null)
                    throw new UnauthorizedException("登录超时！");

                User user = (User) session.getAttribute(User.SESSION_KEY);
                if (user == null)
                    throw new UnauthorizedException("登录超时！");


               /* if (!SessionRepository.isAlive(session.getId())) {
                    session.invalidate();
                    throw new UnauthorizedException("你已被强制登出！");
                }*/

                username = user.getUserName();

                // 验证用户所访问资源权限许可
                if (!act.isEmpty() && !user.permit(act, method))
                    throw new ForbiddenException("权限验证失败！");

                threadLocal.set(user);

//                if (!precsrf(request))
//                    throw new BadRequestException("安全系数校验失败!");

                if (user.isFirstTime()) {
                    response.setContentType("text/html;charset=utf-8");
                    RequestDispatcher rd = request
                            .getRequestDispatcher("/sys/passwd.html");
                    rd.forward(servletRequest, servletResponse);
                }


            } catch (UnauthorizedException e) {
                response.setStatus(401);

                if ("XMLHttpRequest".equals(request
                        .getHeader("X-Requested-With"))) {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write(e.getMessage());
                    out.flush();
                    out.close();

                } else {
                    response.setContentType("text/html;charset=utf-8");
                    RequestDispatcher rd = request
                            .getRequestDispatcher("/sys/login.html");
                    rd.forward(servletRequest, servletResponse);
                }
                return;
            } catch (ForbiddenException e) {
                response.setStatus(403);
                if ("XMLHttpRequest".equals(request
                        .getHeader("X-Requested-With"))) {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write(e.getMessage());
                    out.flush();
                    out.close();
                } else {
                    response.setContentType("text/html;charset=utf-8");
                    RequestDispatcher rd = request
                            .getRequestDispatcher("/sys/login.html");
                    rd.forward(servletRequest, servletResponse);
                }
                return;
            } catch (Exception e) {
                response.setStatus(503);
                logger.error(e.getMessage(), e);
                return;
            } finally {
                StringBuffer sb = new StringBuffer("ip:" + ip);
                sb.append(",");
                sb.append(request.getMethod());
                sb.append(",");
                sb.append(response.getStatus());
                sb.append(",u:");
                sb.append(username);
                sb.append(",:");
                sb.append(uri);
                sb.append(",body:");

                if ("application/json".equalsIgnoreCase(request
                        .getContentType())) {
                    PayLoadRequestWrapper wrapper = new PayLoadRequestWrapper(
                            request);
                    sb.append(wrapper.getBody());
                    request = wrapper;
                } else {
                    sb.append(JSONObject.fromObject(request.getParameterMap())
                            .toString());
                }

                logger.info(sb);
            }
        }

        filterChain.doFilter(request, response);
    }

    public void destroy() {
        threadLocal.remove();
    }

    private boolean precsrf(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String csrf = String.valueOf(session.getAttribute("CSRF_SALT_CACHE"));
        if (!request.getMethod().equalsIgnoreCase("GET") && csrf != null) {
            String xsrf = request.getHeader("X-XSRFToken");
            return csrf.equals(xsrf);
        } else {
            return true;
        }
    }

    private void aftercsrf(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (!request.getMethod().equalsIgnoreCase("GET") || session.getAttribute("CSRF_SALT_CACHE") == null) {
            Cookie xsrf = new Cookie("_xsrf", UUID.randomUUID().toString());
            session.setAttribute("CSRF_SALT_CACHE", xsrf.getValue());
            response.addCookie(xsrf);
        }
    }


}