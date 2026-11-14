package com.demo;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cookies")
public class CookieServlet extends HttpServlet {

 
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String action = req.getParameter("action");

        if ("set".equals(action)) {
            Cookie cookie = new Cookie("theme", "dark");
            cookie.setMaxAge(30 * 24 * 60 * 60);  
            cookie.setPath("/");
            cookie.setHttpOnly(true);           
            resp.addCookie(cookie);
        } else if ("delete".equals(action)) {
            Cookie cookie = new Cookie("theme", "");
            cookie.setMaxAge(0);                 
            cookie.setPath("/");
            resp.addCookie(cookie);
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Cookies</title></head><body>");
        out.println("<h1>Cookies reçus</h1>");

    
        Cookie[] cookies = req.getCookies();
        if (cookies == null || cookies.length == 0) {
            out.println("<p>Aucun cookie reçu.</p>");
        } else {
            out.println("<ul>");
            for (Cookie c : cookies) {
                out.println("<li>" + esc(c.getName()) + " : " + esc(c.getValue()) + "</li>");
            }
            out.println("</ul>");
        }

        String base = req.getContextPath() + "/cookies";
        out.println("<p><a href='" + base + "?action=set'>Créer le cookie theme</a> | "
                + "<a href='" + base + "?action=delete'>Supprimer le cookie theme</a> | "
                + "<a href='" + base + "'>Actualiser</a></p>");
        out.println("</body></html>");
    }
}