import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

////////////////////Classe principale modèle////////////////////

/**
 * Le modèle : le coeur de l'application.
 *
 * Le modèle étend la classe [Observable] : il va posséder un certain nombre
 * d'observateurs (ici, un : la partie de la vue responsable de l'affichage)
 * et devra les prévenir avec [notifyObservers] lors des modifications.
 * Voir la méthode [avance()] pour cela.
 */
class CModele extends Observable {
    //On fixe la taille de la grille.
    public static final int HAUTEUR=10, LARGEUR=15;
    //On stocke un tableau de cellules. 
    private Cellule[][] cellules;
    public final int nbJoueurs;
    //Le tableau des joueurs
    private Joueur[] joueurs;
    //Le numéro du joueur dont c'est le tour
    private int tour;
    //Le numéro du joueur qui possède cet artéfact
    private int[] artefacts = new int[4];
    private boolean abandon = false;
    private boolean[] joueursCoinces = new boolean[4];
    private int nbActions;
    private Cartes<int[]> paquetZone = new Cartes(this);
    private Cartes<evenement> paquetEvent = new Cartes(this);

	/** Construction : on initialise un tableau de cellules. **/
    public CModele(int nbJoueurs) {
    	//Au début de la partie, aucun joueur ne possède d'artéfact
    	for (int i = 0; i < 4; i++) {
    		artefacts[i] = -1;
    	}
    	this.nbJoueurs = nbJoueurs;
    	for (int i = 0; i < nbJoueurs; i++) {
    		joueursCoinces[i] = false;
    	}
    	joueurs = new Joueur[nbJoueurs];
    	int x = (int)(Math.random() * LARGEUR + 1);
    	int y = (int)(Math.random() * HAUTEUR + 1);
    	//Tous les joueurs commencent à l'héliport
    	for (int i = 0; i < nbJoueurs; i++) {
    		joueurs[i] = new Joueur(this, x, y);
    	}
	    tour = 0;
		cellules = new Cellule[LARGEUR+2][HAUTEUR+2];
		for(int i=0; i<LARGEUR+2; i++) {
		    for(int j=0; j<HAUTEUR+2; j++) {
			cellules[i][j] = new Cellule(this,i, j);
				if (i == x && j == y) {
					cellules[i][j].presenceJoueur = true;
					cellules[i][j].type = elements.heliport;
				}
		    }
		}
		//On initialise le terrain
		init();
		for (int i = 0; i < LARGEUR; i++) {
			for (int j = 0; j < HAUTEUR; j++) {
				int[] zone = {i, j};
				paquetZone.ajouterPaquet(zone);
			}
		}
		//On initialise les paquets de cartes
		paquetZone.melanger();
		for (int i = 0; i < 5; i++) {
			paquetEvent.ajouterPaquet(evenement.helicoptere);
			paquetEvent.ajouterPaquet(evenement.sacSable);
		}
		for (int i = 0; i < 8; i++) {
			paquetEvent.ajouterPaquet(evenement.cleAir);
			paquetEvent.ajouterPaquet(evenement.cleEau);
			paquetEvent.ajouterPaquet(evenement.cleTerre);
			paquetEvent.ajouterPaquet(evenement.cleFeu);
		}
		for (int i = 0; i < 15; i++) {
			paquetEvent.ajouterPaquet(evenement.monteeEaux);
		}
		for (int i = 0; i < 43; i++) {
			paquetEvent.ajouterPaquet(evenement.rien);
		}
		paquetEvent.melanger();
    }
    
    /**
     * Donne le (n+1)-ième joueur
     * @param : n le numéro du joueur dans le tableau de joueurs
     * @return : Le (n+1)-ième joueur
     */
    public Joueur getJoueurs(int n) { return joueurs[n]; }

    /**
     * Donne le numéro du tour
     * @return : le numéro du tour
     */
	public int getTour() { return tour; }
	
	/**
	 * Provoque l'abandon
	 */
	public void abandonner() { this.abandon = true; }
	
	/**
	 * Donne le numéro du joueur qui possède l'artéfact
	 * @param n : le numéro de l'artéfact
	 * @return : le numéro du joueur possédant l'artéfact n
	 */
	public int getArtefact(int n) {return this.artefacts[n];
	}
	
	/**
	 * Donne le nombre d'actions effectuées ce tour
	 * @return Le nombre d'actions effectuées ce tour
	 */
	public int getNbActions() {return this.nbActions; }
	
    /**
     * Initialisation aléatoire des cellules, exceptées celle des bords qui
     * ont été ajoutés.
     */
    public void init() {
    	int xeau =(int)(Math.random() * LARGEUR + 1);
    	int yeau = (int)(Math.random() * HAUTEUR + 1);	
    	//On vérifie que l'héliport ne se trouve pas déjà à cet endroit
    	while(cellules[xeau][yeau].type != elements.autre) {
			xeau =(int)(Math.random() * LARGEUR + 1);
	    	yeau= (int)(Math.random() * HAUTEUR + 1);
		}
    	cellules[xeau][yeau].type = elements.eau;
    	int xair =(int)(Math.random() * LARGEUR + 1);
    	int yair = (int)(Math.random() * HAUTEUR + 1);
    	//On vérifie que l'héliport ou un autre artéfact ne se trouve pas déjà à cet endroit
		while(cellules[xair][yair].type != elements.autre) {
			xair =(int)(Math.random() * LARGEUR + 1);
	    	yair = (int)(Math.random() * HAUTEUR + 1);
		}
		cellules[xair][yair].type = elements.air;
		int xfeu =(int)(Math.random() * LARGEUR + 1);
    	int yfeu = (int)(Math.random() * HAUTEUR + 1);
    	//On vérifie que l'héliport ou un autre artéfact ne se trouve pas déjà à cet endroit
		while(cellules[xfeu][yfeu].type != elements.autre) {
			xfeu =(int)(Math.random() * LARGEUR + 1);
	    	yfeu = (int)(Math.random() * HAUTEUR + 1);
		}
    	cellules[xfeu][yfeu].type = elements.feu;
		int xterre =(int)(Math.random() * LARGEUR + 1);
    	int yterre = (int)(Math.random() * HAUTEUR + 1);
    	//On vérifie que l'héliport ou un autre artéfact ne se trouve pas déjà à cet endroit
		while(cellules[xterre][yterre].type != elements.autre) {
			xterre =(int)(Math.random() * LARGEUR + 1);
	    	yterre = (int)(Math.random() * HAUTEUR + 1);
		}
		cellules[xterre][yterre].type = elements.terre;
    }

    /**
     * Passage au tour suivant
     */
   /** public void tourSuivant() {
    	// On submerge trois cellules au hasard
		int x0 = (int)(Math.random() * LARGEUR + 1);
		int y0 = (int)(Math.random() * HAUTEUR + 1);
		//On vérifie que les cellules qu'on submerge ne le sont pas déjà
		while(cellules[x0][y0].etat == etat.submergee) {
			x0 = (int)(Math.random() * LARGEUR + 1);
			y0 = (int)(Math.random() * HAUTEUR + 1);
		}
		if(cellules[x0][y0].etat == etat.normale) cellules[x0][y0].etat = etat.inondee;
		else cellules[x0][y0].etat = etat.submergee;
		x0 = (int)(Math.random() * LARGEUR + 1);
		y0 = (int)(Math.random() * HAUTEUR + 1);
		while(cellules[x0][y0].etat == etat.submergee) {
			x0 = (int)(Math.random() * LARGEUR + 1);
			y0 = (int)(Math.random() * HAUTEUR + 1);
		}
		if(cellules[x0][y0].etat == etat.normale) cellules[x0][y0].etat = etat.inondee;
		else cellules[x0][y0].etat = etat.submergee;
		x0 = (int)(Math.random() * LARGEUR + 1);
		y0 = (int)(Math.random() * HAUTEUR + 1);
		while(cellules[x0][y0].etat == etat.submergee) {
			x0 = (int)(Math.random() * LARGEUR + 1);
			y0 = (int)(Math.random() * HAUTEUR + 1);
		}
		if(cellules[x0][y0].etat == etat.normale) cellules[x0][y0].etat = etat.inondee;
		else cellules[x0][y0].etat = etat.submergee;
		//Si un joueur se trouve sur une cellule submergée, on le fait s'échapper
		for (int i = 0; i < nbJoueurs; i++) {
			if (cellules[joueurs[i].x][joueurs[i].y].etat == etat.submergee) {
				ArrayList<int[]> echapatoire = new ArrayList<int[]>();
				int[] pos = new int[2];
				if(cellules[joueurs[i].x-1][joueurs[i].y].etat != etat.submergee) {
					pos[0] = joueurs[i].x-1;
					pos[1] = joueurs[i].y;
					echapatoire.add(pos);}
				if(cellules[joueurs[i].x+1][joueurs[i].y].etat != etat.submergee) {
					pos[0] = joueurs[i].x+1;
					pos[1] = joueurs[i].y;
					echapatoire.add(pos);
				}
				if(cellules[joueurs[i].x][joueurs[i].y-1].etat != etat.submergee) {
					pos[0] = joueurs[i].x;
					pos[1] = joueurs[i].y-1;
					echapatoire.add(pos);
				}
				if(cellules[joueurs[i].x][joueurs[i].y+1].etat != etat.submergee) {
					pos[0] = joueurs[i].x;
					pos[1] = joueurs[i].y+1;
					echapatoire.add(pos);
				}
				if (echapatoire.size() == 0) this.joueursCoinces[i] = true;
				else {
					cellules[joueurs[i].x][joueurs[i].y].presenceJoueur = false;
					int a = (int)(Math.random()*echapatoire.size());
					joueurs[i].x = echapatoire.get(a)[0];
					joueurs[i].y = echapatoire.get(a)[1];
					cellules[joueurs[i].x][joueurs[i].y].presenceJoueur = true;
				}
			}
		}
		//On donne aléatoirement une clé ou un sac de sable ou un hélicoptère au joueur qui finit son tour
		double a = Math.random();
		double b = Math.random();
		if (a < 0.3) {
			if (b < 0.25) joueurs[tour].cleEau +=1;
			else if (b < 0.5) joueurs[tour].cleAir +=1;
			else if (b < 0.75) joueurs[tour].cleFeu +=1;
			else joueurs[tour].cleTerre +=1;
		}
		else if (a < 0.4) {
			if (b < 0.5) joueurs[tour].helicoptere+=1;
			else joueurs[tour].sacSable+=1;
		}
		tour=(tour+1)%nbJoueurs;
	    nbActions = 0;
		/**
		 * Pour finir, le modèle ayant changé, on signale aux observateurs
		 * qu'ils doivent se mettre à jour.
		 */
		/**notifyObservers();
    }**/
    
    public void tourSuivant() {
    	evenement FinTour = paquetEvent.tirer();
    	switch (FinTour) {
    	case rien :
    		break;
    	case cleEau :
    		joueurs[tour].cleEau++;
    		break;
    	case cleAir :
    		joueurs[tour].cleAir++;
    		break;
    	case cleFeu :
    		joueurs[tour].cleFeu++;
    		break;
    	case cleTerre :
    		joueurs[tour].cleTerre++;
    		break;
    	case monteeEaux :
    		if (cellules[joueurs[tour].x][joueurs[tour].y].etat == etat.inondee) 
    			cellules[joueurs[tour].x][joueurs[tour].y].etat = etat.submergee;
    		else cellules[joueurs[tour].x][joueurs[tour].y].etat = etat.inondee;
    		paquetZone.viderDefausse();
    		break;
    	case helicoptere :
    		joueurs[tour].helicoptere++;
    		break;
    	case sacSable :
    		joueurs[tour].sacSable++;
    		break;
    	}
    	paquetEvent.ajouterDefausse(FinTour);
    	if(paquetEvent.estVide()) paquetEvent.viderDefausse();
    	for (int i = 0; i < 3; i++) {
    		int[] zone = paquetZone.tirer();
    		if (cellules[zone[0]][zone[1]].etat == etat.inondee) {
    			cellules[zone[0]][zone[1]].etat = etat.submergee;
    		}
    		else {
    			cellules[zone[0]][zone[1]].etat = etat.inondee;
    			paquetZone.ajouterDefausse(zone);
    		}
    	}
    	if(paquetZone.estVide()) paquetZone.viderDefausse();
		//Si un joueur se trouve sur une cellule submergée, on le fait s'échapper
		for (int i = 0; i < nbJoueurs; i++) {
			if (cellules[joueurs[i].x][joueurs[i].y].etat == etat.submergee) {
				ArrayList<int[]> echapatoire = new ArrayList<int[]>();
				int[] pos = new int[2];
				if(cellules[joueurs[i].x-1][joueurs[i].y].etat != etat.submergee) {
					pos[0] = joueurs[i].x-1;
					pos[1] = joueurs[i].y;
					echapatoire.add(pos);}
				if(cellules[joueurs[i].x+1][joueurs[i].y].etat != etat.submergee) {
					pos[0] = joueurs[i].x+1;
					pos[1] = joueurs[i].y;
					echapatoire.add(pos);
				}
				if(cellules[joueurs[i].x][joueurs[i].y-1].etat != etat.submergee) {
					pos[0] = joueurs[i].x;
					pos[1] = joueurs[i].y-1;
					echapatoire.add(pos);
				}
				if(cellules[joueurs[i].x][joueurs[i].y+1].etat != etat.submergee) {
					pos[0] = joueurs[i].x;
					pos[1] = joueurs[i].y+1;
					echapatoire.add(pos);
				}
				if (echapatoire.size() == 0) this.joueursCoinces[i] = true;
				else {
					cellules[joueurs[i].x][joueurs[i].y].presenceJoueur = false;
					int a = (int)(Math.random()*echapatoire.size());
					joueurs[i].x = echapatoire.get(a)[0];
					joueurs[i].y = echapatoire.get(a)[1];
					cellules[joueurs[i].x][joueurs[i].y].presenceJoueur = true;
				}
			}
		}
		tour=(tour+1)%nbJoueurs;
	    nbActions = 0;
		/**
		 * Pour finir, le modèle ayant changé, on signale aux observateurs
		 * qu'ils doivent se mettre à jour.
		 */
		notifyObservers();
    }
    
    /**
     * Déplace le joueur dans la direction qu'il désire
     * @param k : le code entier de la direction désirée
     */
    public void deplace(int k) {
    	//On indique que le joueur se déplace
		cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
		//On vérifie que la case ou il souhaite aller n'est pas submergée ou hors du terrain
		if(k == KeyEvent.VK_RIGHT && cellules[joueurs[tour].x+1][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x+1 <= LARGEUR) {
			joueurs[tour].x+=1;
			nbActions++;
		}
		else if(k == KeyEvent.VK_LEFT && cellules[joueurs[tour].x-1][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x-1 > 0) {
			joueurs[tour].x-=1;
			nbActions++;
		}
		else if(k == KeyEvent.VK_UP && cellules[joueurs[tour].x][joueurs[tour].y-1].etat != etat.submergee && joueurs[tour].y-1 > 0) {
			joueurs[tour].y-=1;
			nbActions++;
		}
		else if (k == KeyEvent.VK_DOWN && cellules[joueurs[tour].x][joueurs[tour].y+1].etat != etat.submergee && joueurs[tour].y+1 <= HAUTEUR) {
			joueurs[tour].y+=1;
			nbActions++;
		}
	    for (int i = 0; i < nbJoueurs; i++) {
	    	cellules[joueurs[i].x][joueurs[i].y].presenceJoueur = true;
	    }
	    //Si le joueur a épuisé toutes ses actions, on passe au tour suivant
    	if (nbActions == 3) {
    		tourSuivant();
    	}
	    	notifyObservers();
    }
    
    /**
     * Asseche la zone demandée par le joueur
     * @param sac : indique si le joueur utilise ou non un sac de sable
     * @param x : l'abscisse de la case à assécher
     * @param y : l'ordonnée de la case à assécher
     */
    public void asseche(boolean sac, int x, int y) {
    	//Si le joueur n'utilise pas un sac de sable, on vérifie que la zone qu'il souhaite assécher est bien
    	//inondée et dans le terrain
    	if (!sac) {
	    	if(cellules[x][y].etat == etat.inondee && x <= LARGEUR && x > 0 && y <= HAUTEUR && y > 0) {
	    		cellules[x][y].etat = etat.normale;
	    		nbActions+=1;
	    	}
    	}
    	//Sinon, on vérifie que le joueur possède bien un sac de sable et que la zone qu'il souhaite assécher est bien
    	//inondée, si c'est le cas, on lui retire un sac de sable
    	else {
    		if (joueurs[tour].sacSable > 0 && cellules[x][y].etat == etat.inondee) {
    			cellules[x][y].etat = etat.normale;
    			joueurs[tour].sacSable-=1;
    		}
    	}
    	//Si le joueur a épuisé toutes ses actions, on passe au tour suivant
    	if (nbActions == 3) {
    		tourSuivant();
    	}
	    	notifyObservers();
	   
    }
    
    /**
     * Récupère un artéfact
     */
    public void recupere() {
    	//On vérifie que le joueur se situe bien sur la zone de l'artéfact et qu'il possède assez de clés pour le 
    	//récupérer et si c'est le cas, on retire l'artéfact du terrain
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.eau && joueurs[tour].cleEau >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleEau = 0;
    		nbActions++;
    		artefacts[0] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.air && joueurs[tour].cleAir >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleAir = 0;
    		nbActions++;
    		artefacts[1] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.feu && joueurs[tour].cleFeu >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleFeu = 0;
    		nbActions++;
    		artefacts[2] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.terre && joueurs[tour].cleTerre >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleTerre = 0;
    		nbActions++;
    		artefacts[3] = tour+1;
    	}
    	//Si le joueur a épuisé toutes ses actions, on passe au tour suivant
    	if (nbActions == 3) {
    		tourSuivant();
    	}
    	notifyObservers();
    }
    
    /**
     * Vérifie si les joueurs ont gagné
     * @return true si les joueurs ont gagné, false sinon
     */
    public boolean victoire() {
    	//On vérifie que tous les artéfacts ont été récupérés
    	for(int i = 0; i < 4; i++) {
    		if(artefacts[i] == -1) return false;
    	}
    	//On vérifie que tous les joueurs sont bien à l'héliport
    	for (int i = 0; i < nbJoueurs; i++) {
    		if(cellules[joueurs[i].x][joueurs[i].y].type != elements.heliport) return false;
    	}
    	return true;
    }
    
    /**
     * Vérifie si les joueurs ont perdu
     * @return le numéro de la cause de la défaite et 0 si il n'y a pas défaite
     */
    public int defaite() {    	
    	if(this.abandon) return 5;
    	for(int i = 0; i < nbJoueurs; i++) {
    		if(this.joueursCoinces[i] == true) return 7+i; 
    	}
    	for (int i = 0; i < LARGEUR+1; i++) {
    		for (int j = 0; j < HAUTEUR+1; j++) {
    			if (cellules[i][j].type == elements.air && cellules[i][j].etat == etat.submergee) return 1;
    		}
    	}
    	for (int i = 0; i < LARGEUR+1; i++) {
    		for (int j = 0; j < HAUTEUR+1; j++) {
    			if (cellules[i][j].type == elements.terre && cellules[i][j].etat == etat.submergee) return 2;
    		}
    	}
    	for (int i = 0; i < LARGEUR+1; i++) {
    		for (int j = 0; j < HAUTEUR+1; j++) {
    			if (cellules[i][j].type == elements.feu && cellules[i][j].etat == etat.submergee) return 3;
    		}
    	}
    	for (int i = 0; i < LARGEUR+1; i++) {
    		for (int j = 0; j < HAUTEUR+1; j++) {
    			if (cellules[i][j].type == elements.eau && cellules[i][j].etat == etat.submergee) return 4;
    		}
    	}
    	for (int i = 0; i < LARGEUR+1; i++) {
    		for (int j = 0; j < HAUTEUR+1; j++) {
    			if (cellules[i][j].type == elements.heliport && cellules[i][j].etat == etat.submergee) return 6;
    		}
    	}
    	return 0;
    }
    
    /** 
     * Procède à un échange de clés
     * @param j : le numéro du joueur avec le souhaite on souhaite échanger une clé
     * @param e : l'élément de la clé qu'on souhaite échanger
     */
    public void echange(int j, elements e) {
    	//On vérifie que les deux joueurs impliqués dans l'échange se situent sur la même case
    	if (j != tour && joueurs[j].x == joueurs[tour].x && joueurs[j].y == joueurs[tour].y) {
    		//On vérifie que le joueur qui veut donner une clé la possède bien
    		if (e == elements.eau && joueurs[tour].cleEau > 0) {
    			joueurs[tour].cleEau--;
    			joueurs[j].cleEau++;
    			nbActions++;
    		}
    		else if (e == elements.air && joueurs[tour].cleAir > 0) {
    			joueurs[tour].cleAir--;
    			joueurs[j].cleAir++;
    			nbActions++;
    		}
    		else if (e == elements.feu && joueurs[tour].cleFeu > 0) {
    			joueurs[tour].cleFeu--;
    			joueurs[j].cleFeu++;
    			nbActions++;
    		}
    		else if (e == elements.terre && joueurs[tour].cleTerre > 0) {
    			joueurs[tour].cleTerre--;
    			joueurs[j].cleTerre++;
    			nbActions++;
    		}
    	}
    	//Si le joueur a épuisé toutes ses actions, on passe au tour suivant
    	if (nbActions == 3) {
    		tourSuivant();
    	}
    	notifyObservers();
    	
    }
    
    /**
     * Fais prendre l'hélicoptère à un joueur
     * @param xDestination : l'abscisse de la destination
     * @param yDestination : l'ordonnée de la destination
     */
    public void prendreHelicoptere(int xDestination, int yDestination) {
    	//On indique que le joueur se déplace
    	cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
    	//On vérifie que le joueur possède bien un hélicoptère et que la zone ou il veut se rendre n'est pas submergée
    	if(joueurs[tour].helicoptere > 0 && cellules[xDestination][yDestination].etat != etat.submergee) {
    		for (int i = 0; i < nbJoueurs; i++) {
    			if (i != tour && joueurs[i].x == joueurs[tour].x && joueurs[i].y == joueurs[tour].y) {
    				joueurs[i].x = xDestination;
    	    		joueurs[i].y = yDestination;
    			}
    		}
    		joueurs[tour].x = xDestination;
    		joueurs[tour].y = yDestination;
    		joueurs[tour].helicoptere-=1;
    	}
    	//On indique le nouvel emplacement du joueur
    	cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = true;
    	notifyObservers();
    }
    


    /**
     * Une méthode pour renvoyer la cellule aux coordonnées choisies (sera
     * utilisée par la vue).
     */
    public Cellule getCellule(int x, int y) { return cellules[x][y]; }
}

////////////////////Fin classe principale modèle////////////////////

////////////////////Classes auxiliaires modèle////////////////////


/**
 * Définition d'une classe pour les cellules.
 * Cette classe fait encore partie du modèle.
 */
class Cellule {
    /** On conserve un pointeur vers la classe principale du modèle. */
    private CModele modele;

    /** L'état d'une cellule est donné par un booléen. */
    protected etat etat;
    protected elements type;
    protected boolean presenceJoueur;
    /**
     * On stocke les coordonnées pour pouvoir les passer au modèle lors
     * de l'appel à [compteVoisines].
     */
    private final int x, y;
    public Cellule(CModele modele, int x, int y) {
        this.modele = modele;
        this.etat = etat.normale;
        this.x = x; this.y = y;
        this.type = elements.autre;
        this.presenceJoueur = false;
    }
}    

/**
 * Définition d'une classe pour les joueurs
 *
 */
class Joueur{
	//Inventaire du joueur
	protected int cleEau;
	protected int cleFeu;
	protected int cleAir;
	protected int cleTerre;
	protected int helicoptere;
	protected int sacSable;
	//Position du joueur
	protected int x, y;
	//On conserve un pointeur cers la classe principale du modèle
	private CModele modele;
	
	public Joueur(CModele modele, int x, int y) {
		this.modele = modele;
		this.x = x;
		this.y = y;
		//Au début de la partie, le joueur ne possède rien
		cleEau = 0;
		cleFeu = 0;
		cleTerre = 0;
		cleAir = 0;
		helicoptere = 0;
		sacSable = 0;
	}
}  

/** 
 * Définition d'une classe paramétrée pour les paquets de carte
 */
class Cartes<T>{
	//On conserve un pointeur cers la classe principale du modèle
	CModele modele;
	//On définit le paquet initiale et sas défausse
	ArrayList<T> paquet = new ArrayList<T>();
	ArrayList<T> defausse = new ArrayList<T>();
	
	public Cartes(CModele modele) {
		this.modele = modele;
	}
	
	/**
	 * Mélange le paquet
	 */
	public void melanger() {
		Collections.shuffle(paquet);
	}
	
	/**
	 * Tire la première carte du paquet
	 * @return la première carte du paquet
	 */
	public T tirer() {
		T premiereCarte = paquet.get(0);
		paquet.remove(0);
		return premiereCarte;
	}

	/**
	 * Mélange la défausse et la place au dessus du paquet
	 */
	public void viderDefausse() {
		Collections.shuffle(defausse);
		for (int i = 0; i < defausse.size(); i++) {
			paquet.add(i, defausse.get(i));
		}
		defausse.clear();
	}
	
	/**
	 * Ajoute une carte au paquet
	 * @param o : la carte à ajouter
	 */
	public void ajouterPaquet(T o) {
		paquet.add(o);
	}
	
	/**
	 * Ajoute une carte dans la défausse
	 * @param o : La carte à déposer
	 */
	public void ajouterDefausse(T o) {
		defausse.add(o);
	}
	
	/**
	 * Vérifie si le paquet est vide
	 * @return : true si le paquet est vide, false sinon
	 */
	public boolean estVide() {
		return paquet.size() == 0;
	}
}
