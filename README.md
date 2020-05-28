PROJET ILE INTERDITE
========
## Sommaire
* [1. On va se la couler douce](#1.-On-va-se-la-couler-douce)
* [2. Noooon, pas la trempette !](#2.-Noooon,-pas-la-trempette-!)

## 1. On va se la couler douce

### Fonctionnalités implémentées

Danse cette partie, nous avons créé le modèle réduit de l'île avec ses zones spéciales ainsi que la vue affichant ces zones et l'île. Nous avons aussi créé le bouton "fin de tour". Pour cela, nous nous sommes inspirés du fichier Conway.java vu en cours. Afin de représenter les zones spéciales et l'état des différentes zones du plateau, nous avons créé des enum. Enfin, nous avons fait en sorte que les zones inondées à la fin de chaque tour soient des zones qui n'étaient pas déjà submergées.

## 2. Noooon, pas la trempette !

### Fonctionnalités implémentées

Nous avons créé la classe Joueur qui implémente chaque joueur et les avons placé dans un tableau de la taille du nombre de joueurs (entre 2 et 4). Nous avons adapté la vue pour que les joueurs soient visibles et leur avons permis de se déplacer et de récupérer des artéfact grâce au clavier, ainsi que d'assécher une zone grâce à des boutons. Nous avons aussi supprimé le bouton "fin de tour" que nous avons remplacé par la touche "Enter", de plus, lorsqu'un joueur a effectué ses 3 actions, les actions de fin de tour se produisent automatiquement. Nous avons enfin modifié la vue pour qu'elle indique le joueur dont c'est le tour, le nombre de joueurs ainsi que le nombre d'actions restantes.

### Problèmes rencontrés
