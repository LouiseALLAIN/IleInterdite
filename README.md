PROJET ILE INTERDITE
========
##### ALLAIN Louise - ATIA Sakina - CATILLON Paul

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

Dans cette phase du projet, nous avons décidé de créer un menu demandant le nombre de joueur, ce qui a été particulièrement compliqué. Cela passait par la création d'un autre JFrame et d'une vue alternative, le tout créant la JFrame principale avec le modèle correspondant au nombre de joueurs demandé. Nous avons aussi eu des difficultés à implémenter les déplacements grâce aux touches du clavier (nous avions tout d'abord fait des boutons, moins ergonomiques) et à faire en sorte que les informations contenues sur le panneau latéral s'actualisent au fil de la partie. Nous avons aussi eu des difficultés à placer correctements les différentes informations sur ce panneau latéral.

## 3. Sa place est dans un musée

### Fonctionnalités implémentées

Nous avons inclus dans notre modèle les clés pouvant être possédées par un joueur ainsi que le fait qu'en fin de tour, le joueur puisse recevoir une clé aléatoire avec une probabilité de 30%. Nous avons aussi ajouté au contrôleur le fait de récupérer un artéfact avec la touche "Space" du clavier et la partie gagnante. La partie perdante affiche quant-à-elle la raison de la défaite grâce à un switch case selon quatre critères qui sont la submersion de l'héliport ou d'une zone d'artéfact, la noyade d'un joueur ainsi que l'abandon de la partie grâce à un bouton sur le panneau latéral.

## 4. La vraie aventure

### Fonctionnalités implémentées

Nous avons tout d'abord fait en sorte qu'il faille désormais 4 clés pour récupérer un artéfact en changeant la méthode recupere() et avons implémenté l'échange de clé grâce au clavier. 
Nous avons aussi ajouté l'hélicoptère et le sac de sable, qui ont 10% de chance d'être obtenus à la fin de chaque tour. L'hélicoptère et le sac de sable peuvent être utilisés grâce à un clic de la souris sur la zone concernée. Pour réaliser le sac de sable, nous avons dû modifier la méthode asseche(). 
Nous avons créé les paquets de cartes demandés en utilisant une classe paramétrée mais ne sommes pas sûrs d'avoir bien compris les consignes, notamment en ce qu'il s'agit de la montée des eaux.
Enfin, nous avons créé les rôles spéciaux excepté celui du navigateur qui sont distribués aléatoirement aux joueurs, l'utilisation de chaque capacité spéciale varie selon le rôle.

### Problèmes rencontrés

Nous avons rencontré des problèmes pour l'échange de clé puisque nous voulions utiliser une suite de touches (par exemple 1 puis E pour donner une clé d'eau au joueur 1) mais cela a été compliqué. Par ailleurs, sans pavé tactile, nous avons du remplacer 1 par F1 ce qui nous oblige alors à faire Fn+F1 suivi de E. 
En ce qu'il s'agit du sac de sable et de l'hélicoptère, nous avons rencontré quelques difficultés dans l'utilisation des touches de la souris et dû calcul de la case concernée. 
Au niveau des cartes, la principale difficulté se trouve dans la compréhension de la consigne.
En ce qu'il s'agit des rôles, nous avons eu beaucoup de mal en implémenté certains, notamment le pilote et l'explorateur car nous voulions, pour utiliser leurs capacités spéciales, faire une combinaison clavier/souris (shift suivi de la case où ils souhaitaient aller) mais n'avons pas réussi. Il faut donc cliquer sur la roulette de la souris avant de cliquer sur la case où l'on souhaite se déplacer. Nous n'avons aussi pas réussi à permettre aux joueurs de choisir leur rôle dans un menu graphique et avons donc décidé de les attribuer au hasard. 

## Travail graphique
Nous avons représenté l'île par une grille rectangulaire de 15x10 cases, chaque case correspondant à une zone accessible. La figure sur la case est associée à son état : il y a trois images pour une zone sèche, inondée et submergée. Les zones spéciales (héliport, artefacts) sont représentées par des images en forme de cercles inscrits dans la case, afin que l'on puisse détecter l'état des cases sur lesquelles elles se trouvent. Enfin, les joueurs sont des images d'aventuriers de couleurs différentes, pour les différencier plus facilement,avec, en plus, un numéro qui se déplacent sur les cases. \\
Nous avons également ajouté une musique de fond qui se lance dès le démarrage du menu. Nous avons par ailleurs ajouté une fenêtre qui affiche les commandes du jeu.

### Problèmes rencontrés
Ici, le problème majeur était de réussir à importer la première image. Nous avions quelques difficultés quant à l'endroit où importer l'image et dans quelle fonction l'implémenter. Une fois cette première image implémentée avec succès, il suffisait de répéter l'opération pour les 12 autres images. Il a fallu de simples retouches photos (changer la forme rectangulaire pour opter pour un cercle, enlever le fond de l'image, modifier les couleurs des joueurs) pour avoir un jeu qui nous plaise.

(toutes les images sont prises de www.vectorstock.com, la musique provient de www.melodyloops.com)

## Répartition du travail
Il faut savoir que nous avons commencé en deux groupes séparés (Louise travaillait seule au début et Paul et Sakina formaient un binôme). Nous nous sommes rejoints au milieu de la partie 2 et avons mis en commun ce qui avait déjà été fait. 
Par la suite, nous avons avancé ensemble selon nos disponibilités. Entre autres, Paul s'est principalement chargé du menu, des modalités de défaite et des personnages particuliers. Sakina s'est beaucoup investie dans le travail sur la vue ainsi que le respect de l'accessibilité des variables. Louise, quant à elle, s'est occupée du modèle, notamment la première partie, ainsi que du jeu de cartes, l'échange de clés et les actions spéciales (hélicoptère, sac de sable).
Pour résumer, Louise a plutôt travaillé sur le modèle, Sakina sur la vue et Paul sur les contrôles, mais l'idée générale était plutôt d'avancer ensemble en s'aidant les uns les autres.
