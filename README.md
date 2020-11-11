# Springboot-jdbc

Projet SpringBoot étudiant l'accès à une base de données MySQL via le module JDBC.  
Le code écrit dans ce projet utilise jdbctemplate.
Cependant, nous détaillons beaucoup le processus d'accès à la base : conection, prepare statement ; ceci dans un but pédagogique.  

* **Côté front** : un client html javascript natif utilisant les bibliothèques jquery & bootstrap.
* **Côté back** : une application Java SpringBoot qui implémente les principes du MVC avec une couche DAO qui communique avec Mysql.

Pour exécuter le projet, vous devez avoir installer MySQL et sa base de demo sakila sur votre machine.  
Renseigner les variables d'environnement MYSQL_DB_USER et MYSQL_DB_PASSWORD pour avoir les droits d'accès à la base.  
Une fois le serveur lancé, allez sur localhost:8080 dans votre navigateur web.
