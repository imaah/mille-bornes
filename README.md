# 1000 Bornes

## Jeu du _1000 Bornes_ par Edmond Dujardin

Développé par Clément GUIBOUT & Esteban MORISSE dans le cadre du DUT Informatique de Ifs.

## Interface graphique

### Comment jouer ?

Une fois une partie créer, c'est toujours au tour du joueur du bas de jouer. Pour jouer une carte, rien de plus simple:
le clique droit défausse la carte et l'envoi directement dans le tas prévu à cet effet, tandis que le clique gauche joue
la carte classiquement. Si la carte que vous souhaitez jouer nécessite des actions supplémentaires, comme
choisir la cible dans le cas d'une carte d'attaque, une fenêtre de choix vous sera proposée.

**Les différents types de bots:**
- **Le bot aléatoire**: joue toujours au hasard. Peut autant défausser des cartes au mauvais moment que faire les meilleurs 
coups possibles
- **Le bot naïf**: joue la meilleure carte en apparence. Exemples: mettre un feu vert au début, jouer les bornes les plus 
élevées...

## Dépendances :

* Java 11.0.10
* JavaFX 11.0.2 :
    * [Windows](https://gluonhq.com/download/javafx-11-0-2-sdk-windows/)
    * [Linux](https://gluonhq.com/download/javafx-11-0-2-sdk-linux/)
    * [MacOS](https://gluonhq.com/download/javafx-11-0-2-sdk-mac)
* Google GSON 2.8.6 : [téléchargement](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar)

## Libertées prises

- Nous avons décidé de ne pas commenter la base de code existante. Cette base de code nous a été fournie, et nous n'avons 
donc pas jugé nécessaire de le faire.
- Également, nous avons décidé de laisser certaines méthodes qui ne sont pas utilisées ou qui ne le sont plus. Pourquoi ? 
Car ces méthodes pourraient être utilisées si on envisage des changements sur l'application. De plus, elles témoignent
  pour la plupart de notre refléxion tout au long du projet.


## Ressources
**ATTENTION :** Le dossier [ressources](/ressources) est un dossier de sources (sur eclipse) ou de ressources (sur
intelliJ), donc s'il n'est pas annoté comme ça le programme ne pourra pas se lancer