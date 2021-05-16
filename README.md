# 1000 Bornes

## Jeu du _1000 Bornes_ par Edmond Dujardin

Codé par Clément GUIBOUT & Esteban MORISSE dans le cadre du DUT Informatique de Ifs.<br>
A partir des [resources](res/) et de la [documentation Java](https://myimah.github.io/mille-bornes-javadoc/) 
donnés par les professeurs, nous devions réaliser le jeu des 1000 Bornes.

### Libertées prises par rapport au ressources
* ajout de la methode `TasDeCartes#contientBornes()` qui permet de voir si le tas de cartes contient des bornes.
* La methode `Botte#appliqueEffet(Jeu, EtatJoueur)` n'est pas en abstract car elle est la même dans toutes les bottes sauf une.

### Extensions ajoutées
* 2 difficultées de bot :
  * Random
  * Methode naive
* L'implémentation d'une sauvegarde automatique après chaque tour.
