##### Tableau 1 : opérations sur les ressources de type "Authentification"
| URI | Méthode HTTP | Auths? | Opération |
|---|---|---|---|
| **auths/login** | POST | Non | Vérifier les « credentials » d’un User et renvoyer le User et un token JWT s’ils sont OK |
| **auths/register** | POST | Non | Créer une ressource User et un token JWT et les renvoyer |

<br/>

##### Tableau 1 : opérations sur les ressources de type "Pages"
| URI | Méthode HTTP | Auths? | Opération |
|---|---|---|---|
| **pages** | GET | JWT | READ ALL : Lire toutes les ressources de la collection qui sont publiées ou dont l'utilisateur authentifié en est l'auteur |
| **pages** | GET | Non | READ ALL : Lire toutes les ressources de la collection qui sont publiées |
| **pages/{id}** | GET | JWT | READ ONE : Lire la ressource identifiée si elle est publiée ou si l'utilisateur authentifié en est l'auteur |
| **pages/{id}** | GET | Non | READ ONE : Lire la ressource identifiée si elle est publiée |
| **pages** | POST | JWT | CREATE ONE : Créer une ressource basée sur les données de la requête |
| **pages/{id}** | DELETE | JWT | DELETE ONE : Effacer la ressource identifiée seulement si l'utilisateur authentifié en est l'auteur |
| **pages/{id}** | PUT | JWT | UPDATE ONE : Replacer l'entièreté de la ressource par les données de la requête seulement si l'utilisateur authentifié en est l'auteur|
