PROJET ILE INTERDITE
========
## Sommaire
* [1. On va se la couler douce](#1.-On-va-se-la-couler-douce)
* [2. Noooon, pas la trempette !](#2.-Noooon,-pas-la-trempette-!)
* [3. Sa place est dans un musée](#3.-Sa-place-est-dans-un-musée)
* [4. La vraie aventure](#4.-La-vraie-aventure)
* [Travail graphique](#Travail-graphique)
* [Répartition du travail](#Répartition-du-travail)

## 1. On va se la couler douce

### Fonctionnalités implémentées

Danse cette partie, nous avons créé le modèle réduit de l'île avec ses zones spéciales ainsi que la vue affichant ces zones et l'île. Nous avons aussi créé le bouton "fin de tour". Pour cela, nous nous sommes inspirés du fichier Conway.java vu en cours. Afin de représenter les zones spéciales et l'état des différentes zones du plateau, nous avons créé des enum. Enfin, nous avons fait en sorte que les zones inondées à la fin de chaque tour soient des zones qui n'étaient pas déjà submergées.

## 2. Noooon, pas la trempette !

### Fonctionnalités implémentées

Nous avons créé la classe Joueur qui implémente chaque joueur et les avons placé dans un tableau de la taille du nombre de joueurs (entre 2 et 4). Nous avons adapté la vue pour que les joueurs soient visibles et leur avons permis de se déplacer et de récupérer des artéfact grâce au clavier, ainsi que d'assécher une zone grâce à des boutons. Nous avons aussi supprimé le bouton "fin de tour" que nous avons remplacé par la touche "Enter", de plus, lorsqu'un joueur a effectué ses 3 actions, les actions de fin de tour se produisent automatiquement. Nous avons enfin modifié la vue pour qu'elle indique le joueur dont c'est le tour, le nombre de joueurs ainsi que le nombre d'actions restantes.

### Problèmes rencontrés

Dans cette phase du projet, nous avons décidé de créer un menu demandant le nombre de joueur, ce qui a été particulièrement compliqué. Cela passait par la création d'un autre JFrame et d'une vue alternative, le tout créant la JFrame principale avec le modèle correspondant au nombre de joueurs demandé. Nous avons aussi eu des difficultés à implémenter les déplacements grâce aux touches du clavier (nous avions tout d'abord fait des boutons, moins ergonomiques) et à faire en sorte que les informations contenues sur le panneau latéral s'actualisent au fil de la partie.

## 3. Sa place est dans un musée

### Fonctionnalités implémentées

Nous avons inclus dans notre modèle les clés pouvant être possédées par un joueur ainsi que le fait qu'en fin de tour, le joueur puisse recevoir une clé aléatoire avec une probabilité de 30%. Nous avons aussi ajouté au contrôleur le fait de récupérer un artéfact avec la touche "Space" du clavier et la partie gagnante. La partie perdante affiche quant-à-elle la raison de la défaite grâce à un switch case selon quatre critères qui sont la submersion de l'héliport ou d'une zone d'artéfact, la noyade d'un joueur ainsi que l'abandon de la partie grâce à un bouton sur le panneau latéral.

## 4. La vraie aventure

### Fonctionnalités implémentées

Nous avons réalisé le premier et le deuxième point de cette partie. Nous avons tout d'abord fait en sorte qu'il faille désormais 4 clés pour récupérer un artéfact en changeant la méthode recupere() et avons implémenté l'échange de clé grâce au clavier. Nous avons aussi ajouté l'hélicoptère et le sac de sable, qui ont 10% de chance d'être obtenus à la fin de chaque tour. L'hélicoptère et le sac de sable peuvent être utilisés grâce à un clic de la souris sur la zone concernée. Pour réaliser le sac de sable, nous avons dû modifier la méthode asseche(). 

### Problèmes rencontrés

Nous avons rencontré des problèmes pour l'échange de clé puisque nous voulions utiliser une suite de touches (par exemple 1 puis E pour donner une clé d'eau au joueur 1) mais cela a été compliqué. Par ailleurs, sans pavé tactile, nous avons du remplacer 1 par F1 ce qui nous oblige alors à faire Fn+F1 suivi de E. En ce qu'il s'agit du sac de sable et de l'hélicoptère, nous avons rencontré quelques difficultés dans l'utilisation des touches de la souris et dû calcul de la case concernée. 

## Travail graphique
Nous avons représenté l'île par une grille rectangulaire de 15x10 cases, chaque case correspondant à une zone accessible. La figure sur la case est associée à son état : il y a trois images pour une zone sèche, inondée et submergée. Les zones spéciales (héliport, artefacts) sont représentées par des images en forme de cercles inscrits dans la case, afin que l'on puisse détecter l'état des cases sur lesquelles elles se trouvent. Enfin, les joueurs sont des images d'aventuriers de couleurs différentes, pour les différencier plus facilement,avec, en plus, un numéro qui se déplacent sur les cases. 
Nous avons également ajouté une musique de fond qui se lance dès le démarrage du menu. 

### Problèmes rencontrés
Ici, le problème majeur était de réussir à importer la première image. Nous avions quelques difficultés quant à l'endroit où importer l'image et dans quelle fonction l'implémenter. Une fois cette première image implémentée avec succès, il suffisait de répéter l'opération pour les 12 autres images. Il a fallu de simples retouches photos (changer la forme rectangulaire pour opter pour un cercle, enlever le fond de l'image, modifier les couleurs des joueurs) pour avoir un jeu qui nous plaise.

(toutes les images sont prises de www.vectorstock.com, la musique provient de www.melodyloops.com)

## Répartition du travail
(euh Louise ALED, tu as fait beaucoup trop de choses (voir tout) comparé à nous ^^' ) 
