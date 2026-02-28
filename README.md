# Feedback GraphQL API

API de gestion de feedback sécurisée, développée avec **Spring Boot 3**, **GraphQL**, **Spring Security (JWT)** et **Spring Data JPA**.

## Objectifs

- **Collecter** des retours utilisateurs (bugs, idées, UX, performance, etc.).
- **Gérer** le cycle de vie des feedbacks (OPEN, IN_PROGRESS, RESOLVED, CLOSED).
- **Répondre** aux feedbacks via des commentaires.
- **Sécuriser** l’accès avec JWT, rôles `ROLE_USER` / `ROLE_ADMIN`.

## Stack technique

- Java 17  
- Spring Boot 3  
- Spring GraphQL  
- Spring Security + JWT  
- Spring Data JPA  
- H2 (base en mémoire `feedback`)

## Structure du projet

- `com.example.feedback.domain` : entités JPA (`User`, `Feedback`, `FeedbackResponse`, enums).  
- `com.example.feedback.repository` : interfaces Spring Data JPA.  
- `com.example.feedback.dto` : DTOs pour Auth, User, Feedback, pagination.  
- `com.example.feedback.service` : logique métier (auth, utilisateurs, feedbacks).  
- `com.example.feedback.security` : configuration Spring Security, JWT, filtres.  
- `com.example.feedback.graphql` : contrôleurs GraphQL (queries/mutations).  
- `com.example.feedback.exception` : exceptions métier + handler GraphQL.  
- `src/main/resources/graphql/schema.graphqls` : schéma GraphQL.  
- `src/main/resources/application.yml` : configuration (DB, GraphQL, JWT, logging).

## Configuration JWT

Dans `application.yml` :

```yaml
app:
  security:
    jwt:
      secret: "change-this-secret-in-prod-very-long-and-random"
      expiration-ms: 86400000
```

- **En production**, remplace la valeur de `secret` par une chaîne longue et aléatoire (au moins 32 caractères).  
- Tu peux aussi la surcharger via une variable d’environnement ou une propriété JVM (`-Dapp.security.jwt.secret=...`).

## Lancer l'application

```bash
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`.  
L'endpoint GraphQL est disponible sur `http://localhost:8080/graphql` et l'interface GraphiQL sur `http://localhost:8080/graphiql`.

## Exemples de requêtes GraphQL

### Authentification

```graphql
mutation Register {
  register(input: {username: "john", email: "john@example.com", password: "secret123"}) {
    token
    user {
      id
      username
      email
      roles
    }
  }
}

mutation Login {
  login(input: {username: "john", password: "secret123"}) {
    token
    user {
      id
      username
    }
  }
}
```

Utiliser ensuite le token retourné dans le header HTTP :

```text
Authorization: Bearer <token>
```

### Création d'un feedback

```graphql
mutation CreateFeedback {
  createFeedback(
    input: {
      title: "Bug sur la page profil"
      message: "Impossible de sauvegarder les modifications."
      category: BUG
      rating: 3
    }
  ) {
    id
    title
    status
    category
    createdAt
  }
}
```

### Récupération paginée et filtrée

```graphql
query ListFeedbacks {
  feedbacks(page: 0, size: 10, filter: {status: OPEN, searchText: "bug"}) {
    items {
      id
      title
      status
      author {
        username
      }
    }
    pageInfo {
      page
      size
      totalElements
      totalPages
    }
  }
}
```

## Sécurité et rôles

- `register` et `login` : accessibles sans authentification.  
- `createFeedback`, `myFeedbacks`, `me`, `addFeedbackResponse` : nécessitent un JWT valide.  
- `updateFeedbackStatus` : réservé au rôle `ROLE_ADMIN`.  

Les artefacts de build (`target/`, IDE, etc.) sont exclus du dépôt via `.gitignore` pour garder le repository propre.

