<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Catalogue de produits</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main class="container wide">
    <h1>Catalogue de produits</h1>

    <c:if test="${not empty flash}">
        <p class="success"><c:out value="${flash}"/></p>
    </c:if>

    <p><a class="button-link" href="${pageContext.request.contextPath}/produits?action=new">+ Ajouter un produit</a></p>

    <c:choose>
        <c:when test="${empty produits}">
            <p>Aucun produit.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr><th>Nom</th><th>Description</th><th>Prix</th><th>Stock</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${produits}">
                    <tr>
                        <td><c:out value="${p.nom}"/></td>
                        <td><c:out value="${p.description}"/></td>
                        <td><fmt:formatNumber value="${p.prix}" minFractionDigits="2" maxFractionDigits="2"/> DH</td>
                        <td>
                            <c:choose>
                                <c:when test="${p.stock == 0}"><span class="out-stock">0</span></c:when>
                                <c:otherwise><c:out value="${p.stock}"/></c:otherwise>
                            </c:choose>
                        </td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/produits?action=edit&id=${p.id}">Éditer</a>
                            <form method="post" action="${pageContext.request.contextPath}/produits"
                                  onsubmit="return confirm('Voulez-vous vraiment supprimer ce produit ?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${p.id}">
                                <button class="danger" type="submit">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <p><a href="${pageContext.request.contextPath}/dashboard">Retour au tableau de bord</a></p>
</main>
</body>
</html>
