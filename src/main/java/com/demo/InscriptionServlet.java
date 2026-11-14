package com.demo;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/inscription")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class InscriptionServlet extends HttpServlet {

 
    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        afficherFormulaire(req, resp, null, "", "", "");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

     
        req.setCharacterEncoding("UTF-8");

        String nom = req.getParameter("nom");
        String email = req.getParameter("email");
        String ageStr = req.getParameter("age");

 
        if (nom == null || nom.trim().isEmpty()) {
            afficherFormulaire(req, resp, "Le nom est obligatoire.", nom, email, ageStr);
            return;
        }
        nom = nom.trim();

      
        if (email == null || !email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            afficherFormulaire(req, resp, "Email invalide.", nom, email, ageStr);
            return;
        }
        email = email.trim();


        int age;
        try {
            age = Integer.parseInt(ageStr == null ? "" : ageStr.trim());
        } catch (NumberFormatException e) {
            afficherFormulaire(req, resp, "L'âge doit être un nombre.", nom, email, ageStr);
            return;
        }
        if (age < 18 || age > 120) {
            afficherFormulaire(req, resp, "L'âge doit être compris entre 18 et 120.", nom, email, ageStr);
            return;
        }

    
        HttpSession session = req.getSession(true);
        session.setAttribute("utilisateur", nom);

      
        req.getRequestDispatcher("/upload").forward(req, resp);
    }

    private void afficherFormulaire(HttpServletRequest req, HttpServletResponse resp,
                                    String erreur, String nom, String email, String age)
            throws IOException {

        resp.setContentType("text/html;charset=UTF-8");

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Inscription</title></head><body>");
        sb.append("<h1>Inscription</h1>");

        if (erreur != null) {
            sb.append("<p style='color:red'>").append(esc(erreur)).append("</p>");
        }

        sb.append("<form method='post' action='").append(req.getContextPath())
          .append("/inscription' enctype='multipart/form-data'>");
        sb.append("<p><input type='text' name='nom' placeholder='Nom' required value='")
          .append(esc(nom)).append("'></p>");
        sb.append("<p><input type='email' name='email' placeholder='Email' required value='")
          .append(esc(email)).append("'></p>");
        sb.append("<p><input type='number' name='age' placeholder='Age' min='18' max='120' required value='")
          .append(esc(age)).append("'></p>");
        sb.append("<p><input type='file' name='file' required></p>");
        sb.append("<button type='submit'>S'inscrire</button>");
        sb.append("</form></body></html>");

        resp.getWriter().println(sb);
    }
}