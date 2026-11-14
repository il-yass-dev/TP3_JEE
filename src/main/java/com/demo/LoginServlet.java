
package com.demo;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/views/login.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String login = req.getParameter("login");
        String password = req.getParameter("password");

      

        if ("admin".equals(login) && "password".equals(password)) {

            HttpSession session = req.getSession();
            session.setAttribute("user", login);

            String destination =
                    (String) session.getAttribute("urlDemandee");

            session.removeAttribute("urlDemandee");

            if (destination == null || destination.isBlank()
                    || !destination.startsWith(req.getContextPath() + "/")) {
                destination = req.getContextPath() + "/dashboard";
            }

            resp.sendRedirect(destination);

        } else {

            req.setAttribute("erreur",
                    "Identifiant ou mot de passe incorrect.");

            req.setAttribute("login", login == null ? "" : login);

            req.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(req, resp);
        }
    }
}
