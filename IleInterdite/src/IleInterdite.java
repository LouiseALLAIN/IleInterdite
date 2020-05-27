import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;


/**
 * Interface des objets observateurs.
 */ 
interface Observer {
    /**
     * Un observateur doit posséder une méthode [update] déclenchant la mise à
     * jour.
     */
    public void update();
    /**
     * La version officielle de Java possède des paramètres précisant le
     * changement qui a eu lieu.
     */
}

/**
 * Classe des objets pouvant être observés.
 */
abstract class Observable {
    /**
     * On a une liste [observers] d'observateurs, initialement vide, à laquelle
     * viennent s'inscrire les observateurs via la méthode [addObserver].
     */
    private ArrayList<Observer> observers;
    public Observable() {
	this.observers = new ArrayList<Observer>();
    }
    public void addObserver(Observer o) {
	observers.add(o);
    }

    /**
     * Lorsque l'état de l'objet observé change, il est convenu d'appeler la
     * méthode [notifyObservers] pour prévenir l'ensemble des observateurs
     * enregistrés.
     * On le fait ici concrètement en appelant la méthode [update] de chaque
     * observateur.
     */
    public void notifyObservers() {
	for(Observer o : observers) {
	    o.update();
	}
    }
}
/** Fin du schéma observateur/observé. */


/**
 * Nous allons commencer à construire notre application, en voici la classe
 * principale.
 */
public class IleInterdite {
    /**
     * L'amorçage est fait en créant le modèle et la vue, par un simple appel
     * à chaque constructeur.
     * Ici, le modèle est créé indépendamment (il s'agit d'une partie autonome
     * de l'application), et la vue prend le modèle comme paramètre (son
     * objectif est de faire le lien entre modèle et utilisateur).
     */
    public static void main(String[] args) {
	/**
	 * Pour les besoins du jour on considère la ligne EvenQueue... comme une
	 * incantation qu'on pourra expliquer plus tard.
	 */
    	EventQueue.invokeLater(() -> {
    		/** Voici le contenu qui nous intéresse. */
    				
    				CVueMenu vueMenu = new CVueMenu();
    				/**while(true) {
    					if(vueMenu.resultat != 0) {
    						CModele modele = new CModele(vueMenu.resultat);
    						CVue vue = new CVue(modele); 
    						break;
    					}
    					
    				}*/
    	    });
    }
}
/** Fin de la classe principale. */


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
    
    public void asseche(int k) {
    	if(k == KeyEvent.VK_RIGHT && cellules[joueurs[tour].x+1][joueurs[tour].y].etat == etat.inondee && joueurs[tour].x+1 <= LARGEUR) {
    		cellules[joueurs[tour].x+1][joueurs[tour].y].etat = etat.normale;
    		joueurs[tour].nbActions+=1;
    	}
		else if(k == KeyEvent.VK_LEFT && cellules[joueurs[tour].x-1][joueurs[tour].y].etat == etat.inondee && joueurs[tour].x-1 > 0) {
			cellules[joueurs[tour].x-1][joueurs[tour].y].etat = etat.normale;
    		joueurs[tour].nbActions+=1;
		}
		else if(k == KeyEvent.VK_UP && cellules[joueurs[tour].x][joueurs[tour].y-1].etat == etat.inondee && joueurs[tour].y-1 > 0) {
			cellules[joueurs[tour].x][joueurs[tour].y-1].etat = etat.normale;
    		joueurs[tour].nbActions+=1;
		}
		else if (k == KeyEvent.VK_DOWN && cellules[joueurs[tour].x][joueurs[tour].y+1].etat == etat.inondee && joueurs[tour].y+1 <= HAUTEUR) {
			cellules[joueurs[tour].x][joueurs[tour].y+1].etat = etat.normale;
    		joueurs[tour].nbActions+=1;
		}
		else if (k == KeyEvent.VK_ENTER && cellules[joueurs[tour].x][joueurs[tour].y].etat == etat.inondee) {
			cellules[joueurs[tour].x][joueurs[tour].y].etat = etat.normale;
    		joueurs[tour].nbActions+=1;
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
	}
}  

/**
 * La vue : l'interface avec l'utilisateur.
 *
 * On définit une classe chapeau [CVue] qui crée la fenêtre principale de 
 * l'application et contient les deux parties principales de notre vue :
 *  - Une zone d'affichage où on voit l'ensemble des cellules.
 *  - Une zone de commande avec un bouton pour passer à la génération suivante.
 */
class CVue {
    /**
     * JFrame est une classe fournie pas Swing. Elle représente la fenêtre
     * de l'application graphique.
     */
    private JFrame frame;
    /**
     * VueGrille et VueCommandes sont deux classes définies plus loin, pour
     * nos deux parties de l'interface graphique.
     */
    private VueGrille grille;
    private VueCommandes commandes;
    private VuePlayer player;
    /** Construction d'une vue attachée à un modèle. */
    public CVue(CModele modele) {
	/** Définition de la fenêtre principale. */
	frame = new JFrame();
	frame.setTitle(" L'île interdite ");
	JPanel text = new JPanel();
	text.setLayout(new BoxLayout(text, BoxLayout.LINE_AXIS));
    text.add(new JLabel("Cliquer pour assécher une zone inondée"));
    JPanel bouton = new JPanel();
 
    
    /**
	 * On précise un mode pour disposer les différents éléments à
	 * l'intérieur de la fenêtre. Quelques possibilités sont :
	 *  - BorderLayout (défaut pour la classe JFrame) : chaque élément est
	 *    disposé au centre ou le long d'un bord.
	 *  - FlowLayout (défaut pour un JPanel) : les éléments sont disposés
	 *    l'un à la suite de l'autre, dans l'ordre de leur ajout, les lignes
	 *    se formant de gauche à droite et de haut en bas. Un élément peut
	 *    passer à la ligne lorsque l'on redimensionne la fenêtre.
	 *  - GridLayout : les éléments sont disposés l'un à la suite de
	 *    l'autre sur une grille avec un nombre de lignes et un nombre de
	 *    colonnes définis par le programmeur, dont toutes les cases ont la
	 *    même dimension. Cette dimension est calculée en fonction du
	 *    nombre de cases à placer et de la dimension du contenant.
	 */
	frame.setLayout(new FlowLayout());
	//frame.setLayout(new BorderLayout());


	/** Définition des deux vues et ajout à la fenêtre. */
	grille = new VueGrille(modele);
	frame.add(grille);
	commandes = new VueCommandes(modele);
	bouton.setLayout(new BoxLayout(bouton, BoxLayout.PAGE_AXIS));
	bouton.add(commandes);

    JPanel position = new JPanel();
    position.setLayout(new BoxLayout(position, BoxLayout.PAGE_AXIS));
    position.add(text);
    position.add(bouton);
    player = new VuePlayer(modele);
    position.add(player);
    frame.add(position);
	
	/**
	 * Remarque : on peut passer à la méthode [add] des paramètres
	 * supplémentaires indiquant où placer l'élément. Par exemple, si on
	 * avait conservé la disposition par défaut [BorderLayout], on aurait
	 * pu écrire le code suivant pour placer la grille à gauche et les
	 * commandes à droite.
	 *     frame.add(grille, BorderLayout.WEST);
	 *     frame.add(commandes, BorderLayout.EAST);
	 */

	/**
	 * Fin de la plomberie :
	 *  - Ajustement de la taille de la fenêtre en fonction du contenu.
	 *  - Indiquer qu'on quitte l'application si la fenêtre est fermée.
	 *  - Préciser que la fenêtre doit bien apparaître à l'écran.
	 */
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }
}

class VueGrille extends JPanel implements Observer {
    /** On maintient une référence vers le modèle. */
    private CModele modele;
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 40;
    JLabel fin = new JLabel(" ");
    /** Constructeur. */
    public VueGrille(CModele modele) {
	this.modele = modele;
	/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
	modele.addObserver(this);
	/**
	 * Définition et application d'une taille fixe pour cette zone de
	 * l'interface, calculée en fonction du nombre de cellules et de la
	 * taille d'affichage.
	 */
	Dimension dim = new Dimension(TAILLE*CModele.LARGEUR,
				      TAILLE*CModele.HAUTEUR);
	this.setPreferredSize(dim);
	this.add(fin);
    }

    /**
     * L'interface [Observer] demande de fournir une méthode [update], qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie [repaint].
     */
    public void update() { repaint(); }

    public void paintComponent(Graphics g) {
	super.repaint();
	/** Pour chaque cellule... */
	if(!modele.victoire() && modele.defaite() == 0) {
		for(int i=1; i<=CModele.LARGEUR; i++) {
		    for(int j=1; j<=CModele.HAUTEUR; j++) {
			/**
			 * ... Appeler une fonction d'affichage auxiliaire.
			 * On lui fournit les informations de dessin [g] et les
			 * coordonnées du coin en haut à gauche.
			 */
			paint(g, modele.getCellule(i, j), (i-1)*TAILLE, (j-1)*TAILLE);
		    }
		}
		g.setColor(Color.WHITE);
		for (int i = 0; i < modele.nbJoueurs; i++) {
			g.drawString(Integer.toString(i+1), modele.getJoueurs()[i].x*TAILLE-23, modele.getJoueurs()[i].y*TAILLE-15);
		}
	}else if (modele.victoire()){
		this.removeAll();
		g.setColor(Color.WHITE);
		g.fillRect(0,  0,  TAILLE*CModele.LARGEUR,
				      TAILLE*CModele.HAUTEUR);
		fin.setLayout(new BoxLayout(fin, BoxLayout.X_AXIS));
		fin = new JLabel("VICTOIRE");
		Font font = new Font("Arial", Font.BOLD, 96);
		fin.setFont(font);
		this.add(fin);
		this.validate();
	}
	else {
		this.removeAll();
		g.setColor(Color.WHITE);
		g.fillRect(0,  0,  TAILLE*CModele.LARGEUR,
				      TAILLE*CModele.HAUTEUR);
		fin.setLayout(new BoxLayout(fin, BoxLayout.X_AXIS));
		switch(modele.defaite()) {
		case 1:
			fin = new JLabel("Défaite : le mana d'air a été submergé !");
			break;
		case 2:
			fin = new JLabel("Défaite : le mana de terre a été submergé !");
			break;
		case 3:
			fin = new JLabel("Défaite : le mana de feu a été submergé !");
			break;
		case 4:
			fin = new JLabel("Défaite : le mana d'eau a été submergé !");
			break;
		case 5:
			fin = new JLabel("Abandon...");
			break;
		case 6:
			fin = new JLabel("Défaite : L'héliport a été submergé !");
			break;
		case 7:
			fin = new JLabel("Défaite : Joueur 1 est submergé et coincé !");
			break;
		case 8:
			fin = new JLabel("Défaite : Joueur 2 est submergé et coincé !");
			break;
		case 9:
			fin = new JLabel("Défaite : Joueur 3 est submergé et coincé !");
			break;
		case 10:
			fin = new JLabel("Défaite : Joueur 4 est submergé et coincé !");
			break;
		}
		
		Font font = new Font("Arial", Font.BOLD, 25);
		fin.setFont(font);
		this.add(fin);
		/*int a = modele.defaite();
		this.add(new JLabel(String.valueOf(a)));*/
		this.validate();
	}
    }
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut être désignée que par l'intermédiaire
     * de la classe [CModele] à laquelle elle est interne, d'où type
     * [CModele.Cellule].
     * Ceci serait impossible si [Cellule] était déclarée privée dans [CModele].
     */
    private void paint(Graphics g, Cellule c, int x, int y) {
        /** Sélection d'une couleur. */
	if (c.etat == etat.normale) g.setColor(new Color(186, 74, 0));
	else if (c.etat == etat.inondee) g.setColor(new Color(93, 173, 226));
	else g.setColor(new Color(27, 79, 114));
	g.fillRect(x, y, TAILLE, TAILLE);
	if(c.type == elements.eau) g.setColor(Color.BLUE);
	if(c.type == elements.air) g.setColor(Color.LIGHT_GRAY);
	if(c.type == elements.feu) g.setColor(Color.RED);
	if(c.type == elements.terre) g.setColor(Color.GREEN);
	if(c.type == elements.heliport) g.setColor(Color.DARK_GRAY);
	if(c.type != elements.autre) g.fillOval(x, y, TAILLE, TAILLE);
	if(c.presenceJoueur) {
		g.setColor(Color.BLACK);
		g.fillOval(x+5,  y+5,  TAILLE-10, TAILLE-10);
	}
        /** Coloration d'un rectangle. */
    }
}

class VuePlayer extends JPanel implements Observer {

    private CModele modele;
    private JLabel value;
    JLabel value0 = new JLabel(" ");
  	JLabel value1 = new JLabel(" ");
  	JLabel value2 = new JLabel(" ");
  	JLabel value3 = new JLabel(" ");
    public VuePlayer(CModele modele) {
	this.modele = modele;
	modele.addObserver(this);
  	int actions = modele.getJoueurs()[modele.getTour()].nbActions;  //actions
  	this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
  	this.add(new JLabel("Nombre de joueurs: " + modele.getJoueurs().length));
  	this.add(new JLabel("Au tour du joueur: " + (modele.getTour()+1)));
  	this.add(new JLabel("Nombre d'actions restantes: " + (3-actions)));
  	for (int i = 0; i < modele.nbJoueurs; i++) {
  		this.add(new JLabel("Joueur " + (i+1) + " : " + 
  				modele.getJoueurs()[i].cleEau + " Clés eau, " + 
  				modele.getJoueurs()[i].cleAir + " Clés air, " + 
  				modele.getJoueurs()[i].cleFeu + " Clés feu, " + 
  				modele.getJoueurs()[i].cleTerre + " Clés terre" ));
  	}
  	this.add(value0);
  	this.add(value1);
  	this.add(value2);
  	this.add(value3);
    }

    public void update() { 
    	this.removeAll();
    	int actions = modele.getJoueurs()[modele.getTour()].nbActions;  //actions
    	this.add(new JLabel("Nombre de joueurs: " + modele.getJoueurs().length));
      	this.add(new JLabel("Au tour du joueur: " + (modele.getTour()+1)));
      	this.add(new JLabel("Nombre d'actions restantes: " + (3-actions)));
      	for (int i = 0; i < modele.nbJoueurs; i++) {
      		this.add(new JLabel("Joueur " + (i+1) + " : " + 
      				modele.getJoueurs()[i].cleEau + " Clés eau, " + 
      				modele.getJoueurs()[i].cleAir + " Clés air, " + 
      				modele.getJoueurs()[i].cleFeu + " Clés feu, " + 
      				modele.getJoueurs()[i].cleTerre + " Clés terre" ));
      	}
      	if(modele.artefacts[0] != -1) value0 = new JLabel("Artéfact d'eau possédé par Joueur " + modele.artefacts[0]);
      	if(modele.artefacts[1] != -1) value1 = new JLabel("Artéfact d'air possédé par Joueur " + modele.artefacts[1]);
      	if(modele.artefacts[2] != -1) value2 = new JLabel("Artéfact de feu possédé par Joueur " + modele.artefacts[2]);
      	if(modele.artefacts[3] != -1) value3 = new JLabel("Artéfact de terre possédé par Joueur " + modele.artefacts[3]);
      	this.add(value0);
      	this.add(value1);
      	this.add(value2);
      	this.add(value3);
        this.validate();
    	this.repaint(); }
}

/**
 * Une classe pour représenter la zone contenant le bouton.
 *
 * Cette zone n'aura pas à être mise à jour et ne sera donc pas un observateur.
 * En revanche, comme la zone précédente, celle-ci est un panneau [JPanel].
 */
class VueCommandes extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * référence au modèle.
     */
    private CModele modele;
    
    /** Constructeur. */
    public VueCommandes(CModele modele) {
		this.modele = modele;
		/**
		 * On crée un nouveau bouton, de classe [JButton], en précisant le
		 * texte qui doit l'étiqueter.
		 * Puis on ajoute ce bouton au panneau [this].
		 */
		JButton AssecheHaut = new JButton("h");
		JButton AssecheBas = new JButton("b");
		JButton Asseche = new JButton("o");
		JButton AssecheGauche = new JButton("g");
		JButton AssecheDroite = new JButton("d"); 
		this.add(AssecheHaut);
		this.add(AssecheBas);
		this.add(Asseche);
		this.add(AssecheGauche);
		this.add(AssecheDroite);
		JButton abandon = new JButton("Abandonner");
		this.add(abandon);
		Controleur ctrl = new Controleur(modele);
		/** Enregistrement du contrôleur comme auditeur du bouton. */
		AssecheHaut.addActionListener(ctrl);
		AssecheBas.addActionListener(ctrl);
		Asseche.addActionListener(ctrl);
		AssecheGauche.addActionListener(ctrl);
		AssecheDroite.addActionListener(ctrl);
		AssecheHaut.addKeyListener(ctrl);
		AssecheDroite.addKeyListener(ctrl);
		AssecheBas.addKeyListener(ctrl);
		Asseche.addKeyListener(ctrl);
		AssecheGauche.addKeyListener(ctrl);
		abandon.addActionListener(ctrl);
		
		/**
		 * Variante : une lambda-expression qui évite de créer une classe
	         * spécifique pour un contrôleur simplissime.
	         *
	         JButton boutonAvance = new JButton(">");
	         this.add(boutonAvance);
	         boutonAvance.addActionListener(e -> { modele.avance(); });
	         *
	         */
	
    }
}
/** Fin de la vue. */

/**
 * Classe pour notre contrôleur rudimentaire.
 *
 * Le contrôleur implémente l'interface [ActionListener] qui demande
 * uniquement de fournir une méthode [actionPerformed] indiquant la
 * réponse du contrôleur à la réception d'un événement.
 */
class Controleur implements ActionListener, KeyListener {
    /**
     * On garde un pointeur vers le modèle, car le contrôleur doit
     * provoquer un appel de méthode du modèle.
     * Remarque : comme cette classe est interne, cette inscription
     * explicite du modèle est inutile. On pourrait se contenter de
     * faire directement référence au modèle enregistré pour la classe
     * englobante [VueCommandes].
     */
    CModele modele;
    boolean[] j;
    public Controleur(CModele modele) { 
    	this.modele = modele; 
    	j = new boolean[modele.nbJoueurs];
    	for (int i = 0; i < modele.nbJoueurs; i++) {
    		j[i] = false;
    	}
    }

    public void keyPressed(KeyEvent e) {
    	if (modele.victoire() || modele.defaite() != 0) return;
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			modele.deplace(KeyEvent.VK_UP);
			break;
		case KeyEvent.VK_DOWN:
			modele.deplace(KeyEvent.VK_DOWN);
			break;
		case KeyEvent.VK_RIGHT:
			modele.deplace(KeyEvent.VK_RIGHT);
			break;
		case KeyEvent.VK_LEFT:
			modele.deplace(KeyEvent.VK_LEFT);
			break;
		case KeyEvent.VK_ENTER:
			modele.avance();
			break;
		case KeyEvent.VK_SPACE:
			modele.recupere();
			break;
		case KeyEvent.VK_E:
			for (int i = 0; i < modele.nbJoueurs; i++) {
				if (j[i]) {
					modele.echange(i, elements.eau);
					break;
				}
			}
			break;
		case KeyEvent.VK_A:
			for (int i = 0; i < modele.nbJoueurs; i++) {
				if (j[i]) {
					modele.echange(i, elements.air);
					break;
				}
			}
			break;
		case KeyEvent.VK_F:
			for (int i = 0; i < modele.nbJoueurs; i++) {
				if (j[i]) {
					modele.echange(i, elements.feu);
					break;
				}
			}
			break;
		case KeyEvent.VK_T:
			for (int i = 0; i < modele.nbJoueurs; i++) {
				if (j[i]) {
					modele.echange(i, elements.terre);
					break;
				}
			}
			break;
		}
		for (int i = 0; i < modele.nbJoueurs; i++) {
			j[i] = false;
		}
		switch(keyCode) {
		case KeyEvent.VK_F1:
			j[0] = true;
			break;
		case KeyEvent.VK_F2:
			j[1] = true;
			break;
		case KeyEvent.VK_F3:
			if (modele.nbJoueurs > 2) j[2] = true;
			break;
		case KeyEvent.VK_F4:
			if(modele.nbJoueurs == 4) j[3] = true;
			break;
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (modele.victoire() || modele.defaite() != 0) return;
		String actionCode = e.getActionCommand();
		switch (actionCode) {
		case "h":
			modele.asseche(KeyEvent.VK_UP);
			break;
		case "b":
			modele.asseche(KeyEvent.VK_DOWN);
			break;
		case "d":
			modele.asseche(KeyEvent.VK_RIGHT);
			break;
		case "g":
			modele.asseche(KeyEvent.VK_LEFT);
			break;
		case "o":
			modele.asseche(KeyEvent.VK_ENTER);
			break;
		case "Abandonner":
			modele.abandon = true;
			break;
		}
		
	}
}

/** Fin du contrôleur. */

class CVueMenu {
    public static int resultat;
	/**
     * JFrame est une classe fournie pas Swing. Elle représente la fenêtre
     * de l'application graphique.
     */
    private JFrame frame;
    /**
     * VueGrille et VueCommandes sont deux classes définies plus loin, pour
     * nos deux parties de l'interface graphique.
     */
    private VueMenu grille;
    private VueCommandesMenu commandes;
    /** Construction d'une vue attachée à un modèle. */
    public CVueMenu() {
	/** Définition de la fenêtre principale. */
    resultat = 0;
	frame = new JFrame();
	frame.setLayout(new BorderLayout());
	frame.setTitle("Menu");
	JPanel text = new JPanel();
    
    JPanel bouton = new JPanel();
  
    ImageIcon icone = new ImageIcon("619.png");;
    
    Image image = icone.getImage();
    Image newimg = image.getScaledInstance(600, 350,  java.awt.Image.SCALE_SMOOTH);
    icone = new ImageIcon(newimg);
    JLabel img = new JLabel(icone, JLabel.CENTER);
    
    frame.add(img, BorderLayout.PAGE_START);
    
    /**
	 * On précise un mode pour disposer les différents éléments à
	 * l'intérieur de la fenêtre. Quelques possibilités sont :
	 *  - BorderLayout (défaut pour la classe JFrame) : chaque élément est
	 *    disposé au centre ou le long d'un bord.
	 *  - FlowLayout (défaut pour un JPanel) : les éléments sont disposés
	 *    l'un à la suite de l'autre, dans l'ordre de leur ajout, les lignes
	 *    se formant de gauche à droite et de haut en bas. Un élément peut
	 *    passer à la ligne lorsque l'on redimensionne la fenêtre.
	 *  - GridLayout : les éléments sont disposés l'un à la suite de
	 *    l'autre sur une grille avec un nombre de lignes et un nombre de
	 *    colonnes définis par le programmeur, dont toutes les cases ont la
	 *    même dimension. Cette dimension est calculée en fonction du
	 *    nombre de cases à placer et de la dimension du contenant.
	 */
	
	//frame.setLayout(new BorderLayout());


	/** Définition des deux vues et ajout à la fenêtre. */
	VueMenu menu = new VueMenu();
	frame.add(menu);
	VueCommandesMenu commandes = new VueCommandesMenu();
	bouton.setLocation(frame.getHeight()/2, frame.getWidth()/2);
	bouton.add(commandes);

    JPanel position = new JPanel();
    position.add(text, BorderLayout.CENTER);
    position.add(bouton);
    frame.add(position, BorderLayout.CENTER);
	
	/**
	 * Remarque : on peut passer à la méthode [add] des paramètres
	 * supplémentaires indiquant où placer l'élément. Par exemple, si on
	 * avait conservé la disposition par défaut [BorderLayout], on aurait
	 * pu écrire le code suivant pour placer la grille à gauche et les
	 * commandes à droite.
	 *     frame.add(grille, BorderLayout.WEST);
	 *     frame.add(commandes, BorderLayout.EAST);
	 */

	/**
	 * Fin de la plomberie :
	 *  - Ajustement de la taille de la fenêtre en fonction du contenu.
	 *  - Indiquer qu'on quitte l'application si la fenêtre est fermée.
	 *  - Préciser que la fenêtre doit bien apparaître à l'écran.
	 */
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	if(resultat != 0) {
		frame.dispose();
	}
    }
}






class VueMenu extends JPanel implements Observer {
    /** On maintient une référence vers le modèle. */
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 20;

    /** Constructeur. */
    public VueMenu() {
	/**
	 * Définition et application d'une taille fixe pour cette zone de
	 * l'interface, calculée en fonction du nombre de cellules et de la
	 * taille d'affichage.
	 */
	Dimension dim = new Dimension(TAILLE*CModele.LARGEUR,
				      TAILLE*CModele.HAUTEUR);
	this.setPreferredSize(dim);
    }

    /**
     * L'interface [Observer] demande de fournir une méthode [update], qui
     * sera appelée lorsque la vue sera notifiée d'un changement dans le
     * modèle. Ici on se content de réafficher toute la grille avec la méthode
     * prédéfinie [repaint].
     */
    public void update() { repaint(); }

    /**
     * Les éléments graphiques comme [JPanel] possèdent une méthode
     * [paintComponent] qui définit l'action à accomplir pour afficher cet
     * élément. On la redéfinit ici pour lui confier l'affichage des cellules.
     *
     * La classe [Graphics] regroupe les éléments de style sur le dessin,
     * comme la couleur actuelle.
     */
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut être désignée que par l'intermédiaire
     * de la classe [CModele] à laquelle elle est interne, d'où le type
     * [CModele.Cellule].
     * Ceci serait impossible si [Cellule] était déclarée privée dans [CModele].
     */
        /** Coloration d'un rectangle. */
    
}






class VueCommandesMenu extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * référence au modèle.
     */
    /** Constructeur. */
    public VueCommandesMenu() {
		/**
		 * On crée un nouveau bouton, de classe [JButton], en précisant le
		 * texte qui doit l'étiqueter.
		 * Puis on ajoute ce bouton au panneau [this].
		 */
        JPanel boutons = new JPanel();
    	boutons.setLayout(new BoxLayout(boutons, BoxLayout.LINE_AXIS));
        boutons.add(new JLabel("Combien d'aventuriers partiront en expédition? "));
        JPanel bouton = new JPanel();
    	
		JButton deuxJoueurs = new JButton("2");
		deuxJoueurs.setLocation(this.getHeight()/2, this.getWidth()/2);
		boutons.add(deuxJoueurs);
		JButton troisJoueurs = new JButton("3");
		troisJoueurs.setLocation(this.getHeight()/2, this.getWidth()/2);
		boutons.add(troisJoueurs);
		JButton quatreJoueurs = new JButton("4");
		quatreJoueurs.setLocation(this.getHeight()/2, this.getWidth()/2);
		boutons.add(quatreJoueurs);
		
		this.add(boutons, BorderLayout.NORTH);
		ControleurMenu ctrlM = new ControleurMenu();
		/** Enregistrement du contrôleur comme auditeur du bouton. */
		deuxJoueurs.addActionListener(ctrlM);
		troisJoueurs.addActionListener(ctrlM);
		quatreJoueurs.addActionListener(ctrlM);
		
	
    }
}






class ControleurMenu implements ActionListener{
    /**
     * On garde un pointeur vers le modèle, car le contrôleur doit
     * provoquer un appel de méthode du modèle.
     * Remarque : comme cette classe est interne, cette inscription
     * explicite du modèle est inutile. On pourrait se contenter de
     * faire directement référence au modèle enregistré pour la classe
     * englobante [VueCommandes].
     */
    public ControleurMenu() { }
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCode = e.getActionCommand();
		switch (actionCode) {
		case "2":
			CVueMenu.resultat = 2;
			CModele modele2 = new CModele(2);
			CVue vue2 = new CVue(modele2); 
			break;
		case "3":
			CVueMenu.resultat = 3;
			CModele modele3 = new CModele(3);
			CVue vue3 = new CVue(modele3);
			break;
		case "4":
			CVueMenu.resultat = 4;
			CModele modele4 = new CModele(4);
			CVue vue4 = new CVue(modele4);
			break;
		}
		
	}
}

/** Fin du contrôleur. */