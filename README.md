# AndroidRecruitmentTestApp
Ce projet est une application Android permettant de consulter une liste d'albums, de voir leurs détails et de gérer des favoris en mode déconnecté.

### Configuration Technique 

* Min SDK : 24
* Target SDK : 36
* Langage : Kotlin

### Architecture & Pattern

Le projet suit les principes de la Clean Architecture avec un découpage multi-modulaire pour garantir la séparation des responsabilités et la testabilité.

#### Module :domain (Pure Kotlin)
C'est le cœur métier. Il ne contient aucune dépendance Android.
- Entities : Modèles de données métier (Album).
- Repositories Interfaces : Définition des contrats de données.
- Use Cases : Logique métier isolée (GetAlbumsUseCase, ToggleFavoriteUseCase) pour tester la logique indépendamment de l'UI ou de la base de données.

#### Module :data 
Responsable de la provenance des données.
- Retrofit : Gestion des appels API.
- Room : Persistance locale pour le mode Offline. c'est la solution officielle Google pour la persistance SQLite avec support natif des Flow
- Mappers : Conversion des DTO (réseau) en Entities (DB) et en modèles Domain.
- Repository Implementation : Logique de synchronisation (Offline-first).

#### Module :app 
Couche de présentation.
- MVVM : Gestion de l'état de l'UI avec StateFlow.
- Hilt (Dagger) : Injection de dépendances pour lier les modules. C'est le standard de l'industrie pour la DI, il simplifie la gestion des scopes et la testabilité.
- Jetpack Compose : UI déclarative, performante et réactive.


### Gestion des Favoris
Une gestion des Favoris est implémentée. L'album mis en favoris n'est pas écrasé lors d'un rafraîchissement de l'API.
Stratégie : Lors du fetch API, nous récupérons d'abord les IDs des favoris existants en base. Lors du mapping des données reçues, nous ré-injectons l'état isFavorite avant l'insertion en base via une stratégie REPLACE.

### Mode Offline-First
L'application affiche immédiatement les données du cache (local en Room). L'appel API met à jour la base de données en arrière-plan, et grâce aux Flows réactifs, l'UI se rafraîchit automatiquement dès que la transaction est terminée.

### UI & Thème
- Utilisation des fichiers Color.kt, Type.kt et AppTheme.kt centralisés.
- Ajout d'un mécanisme PullToRefresh si la liste d'album n'est pas vide

### Tests Unitaires
- Ajout des tests unitaires pour les ViewModels et la Repository