package com.demo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/upload")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 6 * 1024 * 1024)
public class UploadServlet extends HttpServlet {

    private static final List<String> EXTENSIONS_AUTORISEES =
            Arrays.asList("jpg", "jpeg", "png", "pdf");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        req.setCharacterEncoding("UTF-8");

    
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("utilisateur") == null) {
            resp.sendRedirect(req.getContextPath() + "/inscription");
            return;
        }

     
        Part filePart = req.getPart("file");
        if (filePart == null
                || filePart.getSubmittedFileName() == null
                || filePart.getSubmittedFileName().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Aucun fichier sélectionné");
            return;
        }

    
        String fileName = new File(filePart.getSubmittedFileName()).getName();

     
        int point = fileName.lastIndexOf('.');
        String extension = (point < 0) ? "" : fileName.substring(point + 1).toLowerCase();
        if (!EXTENSIONS_AUTORISEES.contains(extension)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Extension non autorisée (jpg, jpeg, png, pdf uniquement)");
            return;
        }

       
        File uploadDir = new File(System.getProperty("user.home"), "uploads");
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Impossible de créer le dossier d'upload");
            return;
        }

      
        String uniqueName = UUID.randomUUID() + "_" + fileName;
        filePart.write(new File(uploadDir, uniqueName).getAbsolutePath());

        session.setAttribute("fichier", fileName);

  
        resp.sendRedirect(req.getContextPath() + "/succes");
    }
}