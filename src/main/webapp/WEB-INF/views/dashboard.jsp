<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Tableau de bord</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<main class="container">
    <h1>Bienvenue <c:out value="${user}"/> !</h1>
    <p>Vous êtes connecté à votre espace de gestion.</p>
    <p><a class="button-link" href="${pageContext.request.contextPath}/produits">Gérer les produits</a></p>
    <p><a href="${pageContext.request.contextPath}/logout">Se déconnecter</a></p>
</main>
</body>
</html>
