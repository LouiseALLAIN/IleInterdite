import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Le modèle : le coeur de l'application.
 *
 * Le modèle étend la classe [Observable] : il va posséder un certain nombre
 * d'observateurs (ici, un : la partie de la vue responsable de l'affichage)
 * et devra les prévenir avec [notifyObservers] lors des modifications.
 * Voir la méthode [avance()] pour cela.
 */
class CModele extends Observable {
    /** On fixe la taille de la grille. */
    public static final int HAUTEUR=10, LARGEUR=15;
    /** On stocke un tableau de cellules. */
    private Cellule[][] cellules;
    public int nbJoueurs;
    private Joueur[] joueurs;
    private int tour;
    int[] artefacts = new int[4];
    //boolean defaite = false;
    boolean abandon = false;
    /*boolean heliportRecouvert = false;
    boolean manaAirRecouvert = false;
    boolean manaTerreRecouvert = false;
    boolean manaFeuRecouvert = false;
    boolean manaEauRecouvert = false;*/
    //boolean joueurCoince = false;
    boolean[] joueursCoinces = new boolean[4];
    
    public Joueur[] getJoueurs() {
		return joueurs;
	}

	public int getTour() {
		return tour;
	}

	/** Construction : on initialise un tableau de cellules. **/
    public CModele(int nbJoueurs) {
	/**
	 * Pour éviter les problèmes aux bords, on ajoute une ligne et une
	 * colonne de chaque côté, dont les cellules n'évolueront pas.
	 */ 
    	/*for (int i = 0; i < nbJoueurs; i++) {
    		joueursCoinces[i] = false;
    	}*/
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
    	for (int i = 0; i < nbJoueurs; i++) {
    		joueurs[i] = new Joueur(x, y);
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
		init();
    }

    /**
     * Initialisation aléatoire des cellules, exceptées celle des bords qui
     * ont été ajoutés.
     */
    public void init() {
    	int xeau =(int)(Math.random() * LARGEUR + 1);
    	int yeau = (int)(Math.random() * HAUTEUR + 1);	
    	while(cellules[xeau][yeau].type != elements.autre) {
			xeau =(int)(Math.random() * LARGEUR + 1);
	    	yeau= (int)(Math.random() * HAUTEUR + 1);
		}
    	cellules[xeau][yeau].type = elements.eau;
    	int xair =(int)(Math.random() * LARGEUR + 1);
    	int yair = (int)(Math.random() * HAUTEUR + 1);
		while(cellules[xair][yair].type != elements.autre) {
			xair =(int)(Math.random() * LARGEUR + 1);
	    	yair = (int)(Math.random() * HAUTEUR + 1);
		}
		cellules[xair][yair].type = elements.air;
		int xfeu =(int)(Math.random() * LARGEUR + 1);
    	int yfeu = (int)(Math.random() * HAUTEUR + 1);
		while(cellules[xfeu][yfeu].type != elements.autre) {
			xfeu =(int)(Math.random() * LARGEUR + 1);
	    	yfeu = (int)(Math.random() * HAUTEUR + 1);
		}
    	cellules[xfeu][yfeu].type = elements.feu;
		int xterre =(int)(Math.random() * LARGEUR + 1);
    	int yterre = (int)(Math.random() * HAUTEUR + 1);
		while(cellules[xterre][yterre].type != elements.autre) {
			xterre =(int)(Math.random() * LARGEUR + 1);
	    	yterre = (int)(Math.random() * HAUTEUR + 1);
		}
		cellules[xterre][yterre].type = elements.terre;
    }

    /**
     * Calcul de la génération suivante.
     */
    public void avance() {
		int x0 = (int)(Math.random() * LARGEUR + 1);
		int y0 = (int)(Math.random() * HAUTEUR + 1);
		int x1 = (int)(Math.random() * LARGEUR + 1);
		int y1 = (int)(Math.random() * HAUTEUR + 1);
		int x2 = (int)(Math.random() * LARGEUR + 1);
		int y2 = (int)(Math.random() * HAUTEUR + 1);
		while(cellules[x0][y0].etat == etat.submergee) {
			x0 = (int)(Math.random() * LARGEUR + 1);
			y0 = (int)(Math.random() * HAUTEUR + 1);
		}
		while(cellules[x1][y1].etat == etat.submergee) {
			x1 = (int)(Math.random() * LARGEUR + 1);
			y1 = (int)(Math.random() * HAUTEUR + 1);
		}
		while(cellules[x2][y2].etat == etat.submergee) {
			x2 = (int)(Math.random() * LARGEUR + 1);
			y2 = (int)(Math.random() * HAUTEUR + 1);
		}
		if(cellules[x0][y0].etat == etat.normale) cellules[x0][y0].etat = etat.inondee;
		else cellules[x0][y0].etat = etat.submergee;
		if(cellules[x1][y1].etat == etat.normale) cellules[x1][y1].etat = etat.inondee;
		else cellules[x1][y1].etat = etat.submergee;
		if(cellules[x2][y2].etat == etat.normale) cellules[x2][y2].etat = etat.inondee;
		else cellules[x2][y2].etat = etat.submergee;
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
		joueurs[tour].nbActions = 0;
		/**
		 * Pour finir, le modèle ayant changé, on signale aux observateurs
		 * qu'ils doivent se mettre à jour.
		 */
		notifyObservers();
    }
    
    public void deplace(int k) {
		cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
		if(k == KeyEvent.VK_RIGHT && cellules[joueurs[tour].x+1][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x+1 <= LARGEUR) {
			joueurs[tour].x+=1;
			joueurs[tour].nbActions++;
		}
		else if(k == KeyEvent.VK_LEFT && cellules[joueurs[tour].x-1][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x-1 > 0) {
			joueurs[tour].x-=1;
			joueurs[tour].nbActions++;
		}
		else if(k == KeyEvent.VK_UP && cellules[joueurs[tour].x][joueurs[tour].y-1].etat != etat.submergee && joueurs[tour].y-1 > 0) {
			joueurs[tour].y-=1;
			joueurs[tour].nbActions++;
		}
		else if (k == KeyEvent.VK_DOWN && cellules[joueurs[tour].x][joueurs[tour].y+1].etat != etat.submergee && joueurs[tour].y+1 <= HAUTEUR) {
			joueurs[tour].y+=1;
			joueurs[tour].nbActions++;
		}
	    for (int i = 0; i < nbJoueurs; i++) {
	    	cellules[joueurs[i].x][joueurs[i].y].presenceJoueur = true;
	    }
    	if (joueurs[tour].nbActions == 3) {
    		avance();
    	}
	    	notifyObservers();
    }
    
    public void asseche(boolean sac, int x, int y) {
    	if (!sac) {
	    	if(cellules[x][y].etat == etat.inondee && x <= LARGEUR && x > 0 && y <= HAUTEUR && y > 0) {
	    		cellules[x][y].etat = etat.normale;
	    		joueurs[tour].nbActions+=1;
	    	}
    	}
    	else {
    		if (joueurs[tour].sacSable > 0 && cellules[x][y].etat == etat.inondee) {
    			cellules[x][y].etat = etat.normale;
    			joueurs[tour].sacSable-=1;
    		}
    	}
    	if (joueurs[tour].nbActions == 3) {
    		avance();
    	}
	    	notifyObservers();
	   
    }
    
    public void recupere() {
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.eau && joueurs[tour].cleEau >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleEau = 0;
    		joueurs[tour].nbActions++;
    		artefacts[0] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.air && joueurs[tour].cleAir >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleAir = 0;
    		joueurs[tour].nbActions++;
    		artefacts[1] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.feu && joueurs[tour].cleFeu >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleFeu = 0;
    		joueurs[tour].nbActions++;
    		artefacts[2] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.terre && joueurs[tour].cleTerre >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleTerre = 0;
    		joueurs[tour].nbActions++;
    		artefacts[3] = tour+1;
    	}
    	if (joueurs[tour].nbActions == 3) {
    		avance();
    	}
    	notifyObservers();
    }
    
    public boolean victoire() {
    	for(int i = 0; i < 4; i++) {
    		if(artefacts[i] == -1) return false;
    	}
    	for (int i = 0; i < nbJoueurs; i++) {
    		if(cellules[joueurs[i].x][joueurs[i].y].type != elements.heliport) return false;
    	}
    	return true;
    }
    
    public int defaite() {
    	/*if(this.manaAirRecouvert) return 1; 
    	if(this.manaTerreRecouvert) return 2; 
    	if(this.manaFeuRecouvert) return 3; 
    	if(this.manaEauRecouvert) return 4; 
    	if(this.abandon) return 5; 
    	if(this.heliportRecouvert) return 6; 
    	if(this.joueurCoince) return 7; 
    	for (int i = 0; i < LARGEUR+1; i++) {
    		for (int j = 0; j < HAUTEUR+1; j++) {
    			if (cellules[i][j].type != elements.autre && cellules[i][j].etat == etat.submergee) return 8;
    		}
    	}*/
    	
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
    
    public void echange(int j, elements e) {
    	if (j != tour && joueurs[j].x == joueurs[tour].x && joueurs[j].y == joueurs[tour].y) {
    		if (e == elements.eau && joueurs[tour].cleEau > 0) {
    			joueurs[tour].cleEau--;
    			joueurs[j].cleEau++;
    			joueurs[tour].nbActions++;
    		}
    		else if (e == elements.air && joueurs[tour].cleAir > 0) {
    			joueurs[tour].cleAir--;
    			joueurs[j].cleAir++;
    			joueurs[tour].nbActions++;
    		}
    		else if (e == elements.feu && joueurs[tour].cleFeu > 0) {
    			joueurs[tour].cleFeu--;
    			joueurs[j].cleFeu++;
    			joueurs[tour].nbActions++;
    		}
    		else if (e == elements.terre && joueurs[tour].cleTerre > 0) {
    			joueurs[tour].cleTerre--;
    			joueurs[j].cleTerre++;
    			joueurs[tour].nbActions++;
    		}
    	}
    	if (joueurs[tour].nbActions == 3) {
    		avance();
    	}
    	notifyObservers();
    	
    }
    
    public void prendreHelicoptere(int xDestination, int yDestination) {
    	cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
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
    	cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = true;
    	notifyObservers();
    }
    


    /**
     * Une méthode pour renvoyer la cellule aux coordonnées choisies (sera
     * utilisée par la vue).
     */
    public Cellule getCellule(int x, int y) {
	return cellules[x][y];
    }
}

/** Fin de la classe CModele. */

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
/** Fin de la classe Cellule, et du modèle en général. */

class Joueur{
	public int cleEau;
	public int cleFeu;
	public int cleAir;
	public int cleTerre;
	public int helicoptere;
	public int sacSable;
	public int x, y;
	public int nbActions;
	
	public int getNbActions() {
		return nbActions;
	}

	private CModele modele;
	
	public Joueur(int x, int y) {
		this.modele = modele;
		this.x = x;
		this.y = y;
		this.nbActions = 0;
		cleEau = 0;
		cleFeu = 0;
		cleTerre = 0;
		cleAir = 0;
		helicoptere = 0;
		sacSable = 0;
	}
}  
