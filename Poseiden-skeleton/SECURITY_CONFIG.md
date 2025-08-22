# Configuration Sécurisée - Guide d'utilisation

## Variables d'environnement

Ce projet utilise des variables d'environnement pour sécuriser les données sensibles comme les mots de passe et les configurations de base de données.

### Configuration initiale

1. **Copiez le fichier d'exemple :**
   ```bash
   cp .env.example .env
   ```

2. **Modifiez le fichier `.env` avec vos vraies valeurs :**
   ```bash
   # N'ajoutez JAMAIS le fichier .env au contrôle de version !
   nano .env  # ou votre éditeur préféré
   ```

### Variables disponibles

| Variable | Description | Valeur par défaut |
|----------|-------------|-------------------|
| `DB_HOST` | Hôte de la base de données | localhost |
| `DB_PORT` | Port de la base de données | 5432 |
| `DB_NAME` | Nom de la base de données | poseidon |
| `DB_USERNAME` | Nom d'utilisateur BDD | postgres |
| `DB_PASSWORD` | Mot de passe BDD | password |
| `TEST_DB_*` | Variables pour la BDD de test | (similaires) |
| `APP_ENV` | Environnement d'application | dev |
| `SERVER_PORT` | Port du serveur | 8080 |

### Utilisation avec IDE

**IntelliJ IDEA :**
- Run Configuration → Environment Variables → Ajouter les variables

**Eclipse :**
- Run Configuration → Environment → New → Ajouter les variables

**VS Code :**
- Utilisez l'extension "Spring Boot Extension Pack"

### Utilisation en ligne de commande

```bash
# Exporter les variables (Linux/Mac)
export DB_PASSWORD="mon_mot_de_passe_secret"
export DB_USERNAME="mon_utilisateur"

# Puis lancer l'application
mvn spring-boot:run
```

```cmd
# Windows CMD
set DB_PASSWORD=mon_mot_de_passe_secret
set DB_USERNAME=mon_utilisateur
mvn spring-boot:run
```

```powershell
# Windows PowerShell
$env:DB_PASSWORD="mon_mot_de_passe_secret"
$env:DB_USERNAME="mon_utilisateur"
mvn spring-boot:run
```

## Profils d'environnement

- **dev** : Développement local
- **test** : Exécution des tests
- **production** : Déploiement en production

Changez de profil avec : `APP_ENV=production`

## Sécurité

⚠️ **IMPORTANT** :
- Ne commitez JAMAIS les fichiers `.env` ou `application-local.properties`
- Utilisez des mots de passe forts en production
- Changez les mots de passe par défaut
- Limitez les accès réseau à la base de données
