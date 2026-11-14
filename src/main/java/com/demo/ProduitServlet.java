package com.demo;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Produit;
import model.ProduitDAO;



@WebServlet("/produits")
public class ProduitServlet extends HttpServlet {
    private ProduitDAO dao;

    @Override
    public void init() {
        dao = (ProduitDAO) getServletContext().getAttribute("produitDAO");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("new".equals(action)) {
            req.setAttribute("produit", new Produit());
            afficherFormulaire(req, resp);
        } else if ("edit".equals(action)) {
            Long id = lireId(req.getParameter("id"));
            Produit produit = id == null ? null : dao.findById(id);
            if (produit == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.setAttribute("produit", produit);
            afficherFormulaire(req, resp);
        } else {
            Object flash = req.getSession().getAttribute("flash");
            if (flash != null) {
                req.setAttribute("flash", flash);
                req.getSession().removeAttribute("flash");
            }
            req.setAttribute("produits", dao.findAll());
            req.getRequestDispatcher("/WEB-INF/views/produits/list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            Long id = lireId(req.getParameter("id"));
            if (id != null) {
                dao.delete(id);
                req.getSession().setAttribute("flash", "Produit supprimé avec succès.");
            }
            resp.sendRedirect(req.getContextPath() + "/produits");
            return;
        }

        String idTexte = req.getParameter("id");
        Long id = (idTexte == null || idTexte.isBlank()) ? null : lireId(idTexte);
        String nom = valeur(req.getParameter("nom")).trim();
        String description = valeur(req.getParameter("description")).trim();
        String prixTexte = valeur(req.getParameter("prix")).trim();
        String stockTexte = valeur(req.getParameter("stock")).trim();

        Map<String, String> erreurs = new HashMap<>();
        BigDecimal prix = null;
        int stock = 0;

        if (nom.isEmpty()) {
            erreurs.put("nom", "Le nom est obligatoire.");
        } else if (nom.length() < 2 || nom.length() > 50) {
            erreurs.put("nom", "Le nom doit contenir entre 2 et 50 caractères.");
        }

        if (description.length() > 255) {
            erreurs.put("description", "La description ne doit pas dépasser 255 caractères.");
        }

        try {
            if (prixTexte.isEmpty()) throw new NumberFormatException();
            prix = new BigDecimal(prixTexte);
            if (prix.compareTo(BigDecimal.ZERO) <= 0) {
                erreurs.put("prix", "Le prix doit être strictement positif.");
            }
        } catch (NumberFormatException e) {
            erreurs.put("prix", "Saisissez un prix décimal valide et positif.");
        }

        try {
            if (!stockTexte.isEmpty()) {
                stock = Integer.parseInt(stockTexte);
            }
            if (stock < 0) {
                erreurs.put("stock", "Le stock doit être supérieur ou égal à zéro.");
            }
        } catch (NumberFormatException e) {
            erreurs.put("stock", "Le stock doit être un entier valide.");
        }

        Produit produit = new Produit(id, nom, description, prix, stock);

        if (!erreurs.isEmpty()) {
            req.setAttribute("erreurs", erreurs);
            req.setAttribute("produit", produit);
            afficherFormulaire(req, resp);
            return;
        }

        dao.save(produit);
        req.getSession().setAttribute("flash",
                "Produit « " + nom + " » enregistré avec succès.");
        resp.sendRedirect(req.getContextPath() + "/produits");
    }

    private void afficherFormulaire(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/produits/form.jsp").forward(req, resp);
    }

    private Long lireId(String texte) {
        try {
            return Long.valueOf(texte);
        } catch (Exception e) {
            return null;
        }
    }

    private String valeur(String s) {
        return s == null ? "" : s;
    }
}
