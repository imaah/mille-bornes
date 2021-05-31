# 1000 Bornes

## Jeu du _1000 Bornes_ par Edmond Dujardin

Codé par Clément GUIBOUT & Esteban MORISSE dans le cadre du DUT Informatique de Ifs.<br>

### Libertées prises par rapport au ressources

* ajout de la methode `TasDeCartes#contientBornes()` qui permet de voir si le tas de cartes contient des bornes. Utile
  pour vérifier dans une autre classe si le jeu contient encore des cartes bornes, sans quoi la partie est finie (il
  n'est pas possible de consulter toutes les cartes depuis une autre classe que celle-ci)
* La methode `Botte#appliqueEffet(Jeu, EtatJoueur)` n'est pas en abstract car elle est la même dans toutes les bottes
  sauf une.

### Extensions ajoutées

* 2 difficultées de bot :
    * Aléatoire: Joue essaye de jouer n'importe quelle carte, défausse une carte au hasard s'il ne peut en jouer de sa
      main
    * Methode naive: Joue les mouvements les plus évidents que n'importe qui pourrait faire
* L'implémentation d'une sauvegarde automatique après chaque tour.

### Arguments

Il est possible de modifier quelques "règles" du jeu en ajoutant des arguments à la commande de lancement :

* `--max-joueurs=x` : permet de définir le nombre maximum de joueurs (par défaut `4`)
* `--max-limite=x` : permet de définir le maximum sous la limite de vitesse (par défaut `50`)
* `--multiple-200=true/false` : permet de dire si on utilise la règle disant qu'on peut jouer seulement une carte 200km
  par joueur (par défaut `true`)
* `--debug-bot=true/false` : permet d'afficher ou non les logs de debug pour les bots, c-à-d afficher ce que le bot
  aléatoire essaye de faire avant de parvenir à jouer un coup légal (par défaut `false`)

***DISCLAIMER : Il n'y a pas de vérification de type pour ces arguments.***

## Interface graphique

## Dépendances :

* Java 11.0.10
* JavaFX 11.0.2 :
    * [Windows](https://gluonhq.com/download/javafx-11-0-2-sdk-windows/)
    * [Linux](https://gluonhq.com/download/javafx-11-0-2-sdk-linux/)
    * [MacOS](https://gluonhq.com/download/javafx-11-0-2-sdk-mac)
* Google GSON 2.8.6 : [téléchargement](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar)

## IDE

Nous avons décidé d'utiliser l'IDE IntelliJ pour des raisons de préférence.

**ATTENTION :** Le dossier [ressources](/ressources) est un dossier de sources (sur eclipse) ou de ressources (sur
intelliJ), donc s'il n'est pas annoté comme ça le programme ne pourra pas se lancer