# 1000 Bornes

## Jeu du _1000 Bornes_ par Edmond Dujardin

Codé par Clément GUIBOUT & Esteban MORISSE dans le cadre du DUT Informatique de Ifs.<br>
A partir des [resources](res/) et de la [documentation Java](https://myimah.github.io/mille-bornes-javadoc/) 
données par les professeurs, nous devions réaliser le jeu des 1000 Bornes.

### Libertées prises par rapport au ressources
* ajout de la methode `TasDeCartes#contientBornes()` qui permet de voir si le tas de cartes contient des bornes. Utile pour vérifier dans une autre classe si le jeu contient encore des cartes bornes, sans quoi la partie est finie (il n'est pas possible de consulter toutes les cartes depuis une autre classe que celle-ci)
* La methode `Botte#appliqueEffet(Jeu, EtatJoueur)` n'est pas en abstract car elle est la même dans toutes les bottes sauf une.

### Extensions ajoutées
* 2 difficultées de bot :
  * Aléatoire
  * Un peu plus intelligent.
* L'implémentation d'une sauvegarde automatique après chaque tour.
