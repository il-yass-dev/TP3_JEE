# exe1 — Application web Java EE (Jakarta) : Servlets, JSP, Sessions, Cookies, Upload

Projet pédagogique d'application web dynamique (Eclipse WTP) illustrant les notions de base de **Jakarta Servlet 6 / JSP / JSTL** : authentification par session, filtre de sécurité, CRUD de produits, gestion des cookies, formulaire d'inscription avec envoi de fichier.

## Technologies

| Élément | Version / détail |
|---|---|
| Java | 25 (configuré dans Eclipse ; Java 17+ suffit en pratique) |
| Serveur | Apache Tomcat **10.1** (Jakarta EE 10, Servlet 6.0) |
| Vues | JSP + JSTL (`jakarta.servlet.jsp.jstl-3.0.1`, fournis dans `WEB-INF/lib`) |
| IDE | Eclipse IDE for Enterprise Java (projet *Dynamic Web Project*, facet `jst.web` 6.0) |
| Stockage | En mémoire (`ConcurrentHashMap`), aucune base de données |

> Les imports utilisent le namespace `jakarta.*` : le projet **ne fonctionne pas** sur Tomcat 9 ou inférieur (`javax.*`).

## Structure du projet

```
exe1/
├── src/main/java/
│   ├── com/demo/           # Servlets
│   │   ├── LoginServlet        /login
│   │   ├── LogoutServlet       /logout
│   │   ├── DashboardServlet    /dashboard
│   │   ├── ProduitServlet      /produits
│   │   ├── CookieServlet       /cookies
│   │   ├── InscriptionServlet  /inscription
│   │   ├── UploadServlet       /upload
│   │   └── SuccesServlet       /succes
│   ├── filter/AuthFilter.java      # Protège /dashboard et /produits/*
│   ├── listener/AppInitListener.java  # Crée le ProduitDAO au démarrage
│   └── model/
│       ├── Produit.java            # Entité (id, nom, description, prix, stock)
│       └── ProduitDAO.java         # DAO en mémoire
└── src/main/webapp/
    ├── formulaire.html             # Ancien formulaire d'inscription statique
    └── WEB-INF/
        ├── lib/                    # JARs JSTL
        └── views/                  # JSP (non accessibles directement)
            ├── login.jsp
            ├── dashboard.jsp
            └── produits/{list,form}.jsp
```

## Fonctionnalités

### 1. Authentification (`/login`, `/logout`, `AuthFilter`)
- Compte de test : **`admin` / `password`** (en dur dans `LoginServlet`).
- `AuthFilter` intercepte `/dashboard`, `/produits` et `/produits/*`. Si l'utilisateur n'est pas connecté, l'URL demandée est mémorisée en session (`urlDemandee`) puis il est redirigé vers `/login`.
- Après connexion, redirection vers l'URL initialement demandée (uniquement si elle appartient au contexte de l'application), sinon vers `/dashboard`.
- `/logout` invalide la session.

### 2. Gestion des produits (`/produits`) — CRUD
- **Liste** (`GET /produits`) avec message *flash* après chaque opération.
- **Ajout** (`?action=new`) et **modification** (`?action=edit&id=…`).
- **Suppression** (`POST action=delete`) avec confirmation JavaScript.
- Validation côté serveur : nom (2–50 caractères), description (≤ 255), prix décimal strictement positif, stock entier ≥ 0. Les erreurs sont réaffichées dans le formulaire.
- Pattern **Post/Redirect/Get** après chaque écriture.
- Deux produits sont créés au démarrage (Laptop, Souris).
- Le `ProduitDAO` est partagé via le `ServletContext` (attribut `produitDAO`).

### 3. Cookies (`/cookies`)
- `?action=set` crée le cookie `theme=dark` (30 jours, `HttpOnly`).
- `?action=delete` le supprime.
- La page liste tous les cookies reçus (valeurs échappées contre le XSS).

### 4. Inscription + upload (`/inscription` → `/upload` → `/succes`)
- Formulaire `multipart/form-data` : nom, email, âge (18–120) et fichier.
- Validation côté serveur, puis *forward* vers `/upload`.
- `UploadServlet` : taille max 5 Mo, extensions autorisées `jpg`, `jpeg`, `png`, `pdf`, nom nettoyé (`new File(...).getName()`) et préfixé d'un UUID.
- Les fichiers sont enregistrés dans **`<dossier utilisateur>/uploads`** (`System.getProperty("user.home")`).
- `/succes` affiche un message de bienvenue et le nom du fichier envoyé.

## Installation et exécution (Eclipse)

1. **Prérequis** : un JDK récent et Apache Tomcat 10.1 installés.
2. `File > Import > Existing Projects into Workspace` et sélectionner le dossier `exe1`.
3. Vérifier le runtime : *Project > Properties > Targeted Runtimes* → **Apache Tomcat v10.1**. Adapter le JRE si Java 25 n'est pas installé (*Java Build Path > Libraries*).
4. Clic droit sur le projet → `Run As > Run on Server`.
5. Ouvrir <http://localhost:8080/exe1/>.

## URLs principales

| URL | Description | Protégée |
|---|---|:-:|
| `/exe1/login` | Connexion | non |
| `/exe1/dashboard` | Tableau de bord | oui |
| `/exe1/produits` | Catalogue de produits (CRUD) | oui |
| `/exe1/cookies` | Démo cookies | non |
| `/exe1/inscription` | Inscription avec upload | non |
| `/exe1/succes` | Confirmation d'inscription | session |

## Points d'attention / améliorations possibles

- **Fichier CSS manquant** : les JSP référencent `/css/style.css`, absent de l'archive. Les pages s'affichent mais sans mise en forme. Ajouter `src/main/webapp/css/style.css` (classes utilisées : `container`, `small`, `wide`, `error`, `success`, `hint`, `button-link`, `cancel`, `danger`, `out-stock`, `actions`).
- **`formulaire.html` obsolète** : il n'envoie ni fichier ni `multipart/form-data` ; envoyé tel quel vers `/inscription`, il échouera à l'étape d'upload. Utiliser `/inscription` directement.
- **Sécurité** : identifiants en dur, mot de passe en clair, pas de protection CSRF, pas de régénération de l'ID de session à la connexion (risque de fixation de session). À remplacer par une vraie base d'utilisateurs avec mots de passe hachés.
- **Persistance** : les produits disparaissent à chaque redémarrage ; remplacer le DAO par JDBC/JPA pour une vraie base de données.
- **Uploads** : le dossier `~/uploads` est hors de l'application ; ne pas se fier uniquement à l'extension (vérifier aussi le type MIME) pour un usage en production.
- **Dossier `build/`** : contient des classes compilées par Eclipse ; à exclure du dépôt (`.gitignore`), de même que `.settings/`.
