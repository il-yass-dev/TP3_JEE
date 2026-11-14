<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Formulaire produit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main class="container">
    <h1>
        <c:choose>
            <c:when test="${empty produit.id}">Nouveau produit</c:when>
            <c:otherwise>Modifier le produit</c:otherwise>
        </c:choose>
    </h1>

    <form method="post" action="${pageContext.request.contextPath}/produits">
        <c:if test="${not empty produit.id}">
            <input type="hidden" name="id" value="${produit.id}">
        </c:if>

        <label>Nom</label>
        <input type="text" name="nom" maxlength="50" required value="<c:out value='${produit.nom}'/>">
        <c:if test="${not empty erreurs.nom}"><small class="error"><c:out value="${erreurs.nom}"/></small></c:if>

        <label>Description</label>
        <textarea name="description" maxlength="255"><c:out value="${produit.description}"/></textarea>
        <c:if test="${not empty erreurs.description}"><small class="error"><c:out value="${erreurs.description}"/></small></c:if>

        <label>Prix</label>
        <input type="number" name="prix" step="0.01" min="0.01" required value="<c:out value='${produit.prix}'/>">
        <c:if test="${not empty erreurs.prix}"><small class="error"><c:out value="${erreurs.prix}"/></small></c:if>

        <label>Stock</label>
        <input type="number" name="stock" min="0" step="1" value="${produit.stock}">
        <c:if test="${not empty erreurs.stock}"><small class="error"><c:out value="${erreurs.stock}"/></small></c:if>

        <button type="submit">Enregistrer</button>
        <a class="cancel" href="${pageContext.request.contextPath}/produits">Annuler</a>
    </form>
</main>
</body>
</html>
