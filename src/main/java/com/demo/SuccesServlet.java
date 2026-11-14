package com.demo;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/succes")
public class SuccesServlet extends HttpServlet {

  
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

    
        HttpSession session = req.getSession(false);
        String utilisateur = (session == null) ? null : (String) session.getAttribute("utilisateur");

        if (utilisateur == null) {
            resp.sendRedirect(req.getContextPath() + "/inscription");
            return;
        }

        String fichier = (String) session.getAttribute("fichier");

        resp.setContentType("text/html;charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Succès</title></head><body>");
        sb.append("<h1>Bienvenue ").append(esc(utilisateur)).append(" !</h1>");
        sb.append("<p>Inscription réussie.</p>");
        if (fichier != null) {
            sb.append("<p>Fichier envoyé : ").append(esc(fichier)).append("</p>");
        }
        sb.append("</body></html>");

        resp.getWriter().println(sb);
    }
}