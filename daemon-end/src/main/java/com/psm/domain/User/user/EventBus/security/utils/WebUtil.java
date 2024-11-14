package com.psm.domain.User.user.EventBus.security.utils;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtil {
    /**
     * 将字符串渲染到客户端
     *
     * @param response
     * @param string
     * @return
     * @throws IOException
     */
    public static String renderString(HttpServletResponse response, String string) throws IOException {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
