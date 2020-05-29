PROJET ILE INTERDITE
========
## Sommaire
* [1. On va se la couler douce](#1.-On-va-se-la-couler-douce)
* [2. Noooon, pas la trempette !](#2.-Noooon,-pas-la-trempette-!)
* [3. Sa place est dans un musée](#3.-Sa-place-est-dans-un-musée)
* [4. La vraie aventure](#4.-La-vraie-aventure)
* [Répartition du travail](#Répartition-du-travail)

## 1. On va se la couler douce

### Fonctionnalités implémentées

Danse cette partie, nous avons créé le modèle réduit de l'île avec ses zones spéciales ainsi que la vue affichant ces zones et l'île. Nous avons aussi créé le bouton "fin de tour". Pour cela, nous nous sommes inspirés du fichier Conway.java vu en cours. Afin de représenter les zones spéciales et l'état des différentes zones du plateau, nous avons créé des enum. Enfin, nous avons fait en sorte que les zones inondées à la fin de chaque tour soient des zones qui n'étaient pas déjà submergées.

## 2. Noooon, pas la trempette !

### Fonctionnalités implémentées

Nous avons créé la classe Joueur qui implémente chaque joueur et les avons placé dans un tableau de la taille du nombre de joueurs (entre 2 et 4). Nous avons adapté la vue pour que les joueurs soient visibles et leur avons permis de se déplacer et de récupérer des artéfact grâce au clavier, ainsi que d'assécher une zone grâce à des boutons. Nous avons aussi supprimé le bouton "fin de tour" que nous avons remplacé par la touche "Enter", de plus, lorsqu'un joueur a effectué ses 3 actions, les actions de fin de tour se produisent automatiquement. Nous avons enfin modifié la vue pour qu'elle indique le joueur dont c'est le tour, le nombre de joueurs ainsi que le nombre d'actions restantes.

### Problèmes rencontrés

Dans cette phase du projet, nous avons décidé de créer un menu demandant le nombre de joueur, ce qui a été particulièrement compliqué (PAUL C'EST TON MOMENT). Nous avons aussi eu des difficultés à implémenter les déplacements grâce aux touches du clavier (nous avions tout d'abord fait des boutons) et à faire en sorte que les informations contenues sur le panneau latéral s'actualisent au fil de la partie.

## 3. Sa place est dans un musée

### Fonctionnalités implémentées

Nous avons inclus dans notre modèle les clés pouvant être possédées par un joueur ainsi que le fait qu'en fin de tour, le joueur puisse recevoir une clé aléatoire avec une probabilité de 30%. Nous avons aussi ajouté au contrôleur le fait de récupérer un artéfact avec la touche "Space" du clavier et la partie gagnante. La partie perdante affiche quant-à-elle la raison de la défaite grâce à un switch case. Afin de décider d'une défaite, nous avons donné 4 critères qui sont la submersion de l'héliport ou d'une zone d'artéfact, la noyade d'un joueur ainsi que l'abandon de la partie grâce à un bouton sur le panneau latéral.

## 4. La vraie aventure

### Fonctionnalités implémentées

### Problèmes rencontrés

## Répartition du travail
