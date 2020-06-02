import java.awt.EventQueue;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

////////////////////Schéma observateur/observé////////////////////

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

////////////////////Fin schéma observateur/observé////////////////////

////////////////////Classe principale////////////////////

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
    		new CVueMenu();
    	});
    }
}
////////////////////Fin classe principale////////////////////

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
    private float nbActions;
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
    	List<roles> rolesPossibles = new ArrayList<>(); 
        rolesPossibles.add(roles.ingenieur); 
        rolesPossibles.add(roles.messager);
        rolesPossibles.add(roles.plongeur);
        rolesPossibles.add(roles.explorateur);
        rolesPossibles.add(roles.pilote);
        
    	for(int i = 0; i < nbJoueurs; i++) {
    		joueurs[i] = new Joueur(this, x, y, roles.aucun);
    		int a = ThreadLocalRandom.current().nextInt(0, rolesPossibles.size());
    		//Décommenter la ligne suivante pour activer les rôles
    		joueurs[i] = new Joueur(this, x, y, rolesPossibles.get(a));
    		rolesPossibles.remove(a);
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
	public int getArtefact(int n) {return this.artefacts[n];}
	
	/**
	 * Donne le nombre d'actions effectuées ce tour
	 * @return Le nombre d'actions effectuées ce tour
	 */
	public float getNbActions() {return this.nbActions; }
	
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
    	if(nbActions + 1 > 3) {
    		tourSuivant();
    		return;
    	}
    	//On indique que le joueur se déplace
		cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
		//On vérifie que la case ou il souhaite aller n'est pas submergée ou hors du terrain
		if(k == KeyEvent.VK_RIGHT && cellules[joueurs[tour].x+1][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x+1 <= LARGEUR) {
			joueurs[tour].x+=1;
			nbActions = (int)(nbActions + 1.5);
		}
		else if(k == KeyEvent.VK_LEFT && cellules[joueurs[tour].x-1][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x-1 > 0) {
			joueurs[tour].x-=1;
			nbActions = (int)(nbActions + 1.5);
		}
		else if(k == KeyEvent.VK_UP && cellules[joueurs[tour].x][joueurs[tour].y-1].etat != etat.submergee && joueurs[tour].y-1 > 0) {
			joueurs[tour].y-=1;
			nbActions = (int)(nbActions + 1.5);
		}
		else if (k == KeyEvent.VK_DOWN && cellules[joueurs[tour].x][joueurs[tour].y+1].etat != etat.submergee && joueurs[tour].y+1 <= HAUTEUR) {
			joueurs[tour].y+=1;
			nbActions = (int)(nbActions + 1.5);
		}
		else if(joueurs[tour].role == roles.plongeur) {
	    	if(k == KeyEvent.VK_RIGHT && cellules[joueurs[tour].x+1][joueurs[tour].y].etat == etat.submergee && cellules[joueurs[tour].x+2][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x+2 <= LARGEUR) {
	    		cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
	    		joueurs[tour].x+=2;
	    		nbActions = (int)(nbActions + 1.5);
			}
			else if(k == KeyEvent.VK_LEFT && cellules[joueurs[tour].x-1][joueurs[tour].y].etat == etat.submergee && cellules[joueurs[tour].x-2][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x-2 > 0) {
				cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
				joueurs[tour].x-=2;
				nbActions = (int)(nbActions + 1.5);
			}
			else if(k == KeyEvent.VK_UP && cellules[joueurs[tour].x][joueurs[tour].y-1].etat == etat.submergee && cellules[joueurs[tour].x][joueurs[tour].y-2].etat != etat.submergee && joueurs[tour].y-2 > 0) {
				cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
				joueurs[tour].y-=2;
				nbActions = (int)(nbActions + 1.5);
			}
			else if (k == KeyEvent.VK_DOWN && cellules[joueurs[tour].x][joueurs[tour].y+1].etat == etat.submergee && cellules[joueurs[tour].x][joueurs[tour].y+2].etat != etat.submergee && joueurs[tour].y+2 <= HAUTEUR) {
				cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
				joueurs[tour].y+=2;
				nbActions = (int)(nbActions + 1.5);
			}
	    }
	    for (int i = 0; i < nbJoueurs; i++) {
	    	cellules[joueurs[i].x][joueurs[i].y].presenceJoueur = true;
	    }
	    //Si le joueur a épuisé toutes ses actions, on passe au tour suivant
    	if (nbActions >= 3) tourSuivant();
	    notifyObservers();
    }
    
    /**
     * Active le pouvoir du joueur
     * @param x : l'abscisse de la case visée
     * @param y : l'ordonnée de la case visée
     */
    public void utilisePouvoir(boolean gauche, int x, int y) {
    	if (joueurs[tour].role == roles.pilote) pilote(x, y);
    	if (joueurs[tour].role == roles.explorateur) explore(gauche, x, y);
    }
    
    /**
     * Utilise le pouvoir du pilote
     * @param xx : l'abscisse de la zone ou le joueur va se rendre
     * @param yy : l'ordonnée de la zone où le joueur va le rendre
     */
    public void pilote(int xx, int yy) {
    	//On vérifie que la zone n'est pas submergée
    	if(cellules[xx][yy].etat != etat.submergee) {
	    	cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
	    	joueurs[tour].x=xx;
	    	joueurs[tour].y=yy;
	    	//Comme le pouvoir du pilote est fort, nous avons décidé qu'il couterait deux actions
	    	nbActions+=2;
	    	cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = true;
	    	if (nbActions >= 3) tourSuivant();
    	}
    	notifyObservers();
    }
    
    /**
     * Utilise le pouvoir de l'explorateur
     * @param xx : l'abscisse de la zone où le joueur va se rendre
     * @param yy : l'ordonnée de la zone où le joueur va se rendre
     */
    public void explore(boolean gauche, int xx, int yy) {
    	//On vérifie que la zone est accessible à l'explorateur
    	if(gauche) {
	    	if(xx <= joueurs[tour].x+1 && xx >= joueurs[tour].x - 1 && yy <= joueurs[tour].y+1 && yy >= joueurs[tour].y-1
	    			&& (joueurs[tour].x != xx || joueurs[tour].y != yy)
	    			&& cellules[xx][yy].etat != etat.submergee) {
	    		    cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
		    		joueurs[tour].x=xx;
		    		joueurs[tour].y=yy;
		    		nbActions++;
		    		cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = true;
		    		if (nbActions >= 3) tourSuivant();
	    	notifyObservers();
	    	}
    	}
    	else {
    		if(xx <= joueurs[tour].x+1 && xx >= joueurs[tour].x - 1 && yy <= joueurs[tour].y+1 && yy >= joueurs[tour].y-1
	    			&& cellules[xx][yy].etat == etat.inondee) {
    			asseche(false, xx, yy);    			
    		}
    	}
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
	    		if(joueurs[tour].role == roles.ingenieur) nbActions -= 0.5;
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
    	if (nbActions >= 3) tourSuivant();
	    	notifyObservers();
	   
    }
    
    /**
     * Récupère un artéfact
     */
    public void recupere() {
    	if(nbActions + 1 > 3) {
    		tourSuivant();
    		return;
    	}
    	//On vérifie que le joueur se situe bien sur la zone de l'artéfact et qu'il possède assez de clés pour le 
    	//récupérer et si c'est le cas, on retire l'artéfact du terrain
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.eau && joueurs[tour].cleEau >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleEau = 0;
    		nbActions = (int)(nbActions + 1.5);
    		artefacts[0] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.air && joueurs[tour].cleAir >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleAir = 0;
    		nbActions = (int)(nbActions + 1.5);
    		artefacts[1] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.feu && joueurs[tour].cleFeu >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleFeu = 0;
    		nbActions = (int)(nbActions + 1.5);
    		artefacts[2] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.terre && joueurs[tour].cleTerre >= 4) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleTerre = 0;
    		nbActions = (int)(nbActions + 1.5);
    		artefacts[3] = tour+1;
    	}
    	//Si le joueur a épuisé toutes ses actions, on passe au tour suivant
    	if (nbActions >= 3) tourSuivant();
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
    	if(nbActions + 1 > 3) {
    		tourSuivant();
    		return;
    	}
    	//On vérifie que les deux joueurs impliqués dans l'échange se situent sur la même case
    	if (j != tour && ((joueurs[j].x == joueurs[tour].x && joueurs[j].y == joueurs[tour].y) || joueurs[tour].role == roles.messager)) {
    		//On vérifie que le joueur qui veut donner une clé la possède bien
    		if (e == elements.eau && joueurs[tour].cleEau > 0) {
    			joueurs[tour].cleEau--;
    			joueurs[j].cleEau++;
    			nbActions = (int)(nbActions + 1.5);
    		}
    		else if (e == elements.air && joueurs[tour].cleAir > 0) {
    			joueurs[tour].cleAir--;
    			joueurs[j].cleAir++;
    			nbActions = (int)(nbActions + 1.5);
    		}
    		else if (e == elements.feu && joueurs[tour].cleFeu > 0) {
    			joueurs[tour].cleFeu--;
    			joueurs[j].cleFeu++;
    			nbActions = (int)(nbActions + 1.5);
    		}
    		else if (e == elements.terre && joueurs[tour].cleTerre > 0) {
    			joueurs[tour].cleTerre--;
    			joueurs[j].cleTerre++;
    			nbActions = (int)(nbActions + 1.5);
    		}
    	}
    	//Si le joueur a épuisé toutes ses actions, on passe au tour suivant
    	if (nbActions >= 3) tourSuivant();
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
	protected roles role;
	//On conserve un pointeur cers la classe principale du modèle
	private CModele modele;
	
	public Joueur(CModele modele, int x, int y, roles r) {
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
		this.role = r;
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

/**
 * La vue : l'interface avec l'utilisateur.
 *
 * On définit une classe chapeau [CVue] qui crée la fenêtre principale de 
 * l'application et contient les deux parties principales de notre vue :
 *  - Une zone d'affichage où on voit l'ensemble des cellules.
 *  - Une zone de commande avec des boutons et des informations
 */
class CVue {
    /**
     * JFrame est une classe fournie pas Swing. Elle représente la fenêtre
     * de l'application graphique.
     */
    private JFrame frame;
    private VueGrille grille;
    private VueCommandes commandes;
    private VuePlayer player;
    /** Construction d'une vue attachée à un modèle. */
    public CVue(CModele modele) {
	/** Définition de la fenêtre principale. */
	frame = new JFrame();
	frame.setTitle(" L'île interdite ");
	JPanel text = new JPanel();
    text.add(new JLabel("Cliquer pour assécher une zone inondée"));
    JPanel bouton = new JPanel();
	frame.setLayout(new BorderLayout());
	frame.setResizable(false);
	grille = new VueGrille(modele);
	commandes = new VueCommandes(modele);
	bouton.add(commandes);
    JPanel position = new JPanel();
    position.add(text);
    position.add(bouton);
    player = new VuePlayer(modele);
    frame.add(grille, BorderLayout.CENTER);
    frame.add(position, BorderLayout.PAGE_END);
    frame.add(player, BorderLayout.EAST);
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }
}

/**
 * Une classe pour représenter la zone contenant le bouton et les informations
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
		AssecheGauche.setLocation(Frame.WIDTH/2, -Frame.HEIGHT/2);
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
	Controleur ctrl = new Controleur(modele);
	this.addMouseListener(ctrl);
	this.addKeyListener(ctrl);
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
	ImageIcon j1 = new ImageIcon("J1.PNG");
    Image J1 = j1.getImage();
    
    ImageIcon j2  = new ImageIcon("J2.PNG");
    Image J2 = j2.getImage();
    
    ImageIcon j3 = new ImageIcon("J3.PNG");
    Image J3 = j3.getImage();
    
    ImageIcon j4 = new ImageIcon("J4.PNG");
    Image J4 = j4.getImage();
    
	/** Pour chaque cellule... */
	if(!modele.victoire() && modele.defaite() == 0) {
		for(int i=1; i<=CModele.LARGEUR; i++) {
		    for(int j=1; j<=CModele.HAUTEUR; j++) {
			paint(g, modele.getCellule(i, j), (i-1)*TAILLE, (j-1)*TAILLE);
		    }
		}
		g.setColor(Color.WHITE);
		for (int i = 0; i < modele.nbJoueurs; i++) {
			if(i == 0)
			g.drawImage(J1, modele.getJoueurs(i).x*TAILLE-40, modele.getJoueurs(i).y*TAILLE-40, TAILLE, TAILLE, null);
			if(i == 1)
			g.drawImage(J2, modele.getJoueurs(i).x*TAILLE-40, modele.getJoueurs(i).y*TAILLE-40, TAILLE, TAILLE, null);
			if(i == 2)
				g.drawImage(J3, modele.getJoueurs(i).x*TAILLE-40, modele.getJoueurs(i).y*TAILLE-40, TAILLE, TAILLE, null);
			if(i == 3)
				g.drawImage(J4, modele.getJoueurs(i).x*TAILLE-40, modele.getJoueurs(i).y*TAILLE-40, TAILLE, TAILLE, null);
			g.drawString(Integer.toString(i+1), modele.getJoueurs(i).x*TAILLE-23, modele.getJoueurs(i).y*TAILLE);

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
    	
      	 //importation des images
           ImageIcon i = new ImageIcon("ground.png");
           Image terre = i.getImage();
           
           ImageIcon i1 = new ImageIcon("airf.png");
           Image air = i1.getImage();
           
           ImageIcon i2 = new ImageIcon("waterf.png");
           Image eau = i2.getImage();
           
           ImageIcon i3 = new ImageIcon("feuf.png");
           Image feu = i3.getImage();
           
           
           ImageIcon i4 = new ImageIcon("heliportf.png");
           Image heliport = i4.getImage();
           
           ImageIcon i5 = new ImageIcon("herbef.png");
           Image herbe = i5.getImage();
           
           ImageIcon i6 = new ImageIcon("water.png");
           Image inondé = i6.getImage();
           
           ImageIcon i7 = new ImageIcon("eau.png");
           Image submergé = i7.getImage();
           
           ImageIcon i8 = new ImageIcon("joueur.png");
           Image player = i8.getImage();
       	if (c.etat == etat.normale) g.drawImage(terre, x, y, TAILLE, TAILLE, null); 
       	else if (c.etat == etat.inondee) g.drawImage(inondé, x, y, TAILLE, TAILLE, null);
       	else g.drawImage(submergé, x, y, TAILLE, TAILLE, null);
       	if(c.type == elements.eau) g.drawImage(eau, x, y, TAILLE, TAILLE, null);
       	if(c.type == elements.air) g.drawImage(air, x, y, TAILLE, TAILLE, null);
       	if(c.type == elements.feu) g.drawImage(feu, x, y, TAILLE, TAILLE, null);
       	if(c.type == elements.terre) g.drawImage(herbe, x, y, TAILLE, TAILLE, null);
       	if(c.type == elements.heliport) g.drawImage(heliport, x, y, TAILLE, TAILLE, null);
       	if(c.presenceJoueur) {
       	}
    }
}

/**
 * Une classe montrant les informations utiles aux joueurs
 */
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
  	float actions = modele.getNbActions();  //actions
  	this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
  	if(modele.getJoueurs(modele.getTour()).role != roles.aucun) this.add(new JLabel("Au tour du joueur " + (modele.getTour()+1) +
  			" (" + modele.getJoueurs(modele.getTour()).role + ")"));
  	else this.add(new JLabel("Au tour du joueur " + (modele.getTour()+1)));
  	this.add(new JLabel("Nombre d'actions restantes : " + (int)(3-actions)));
  	for (int i = 0; i < modele.nbJoueurs; i++) {
  		if(modele.getJoueurs(modele.getTour()).role != roles.aucun) this.add(new JLabel("Joueur " + (i+1) + 
  				" (" + modele.getJoueurs(i).role +") : " +
  				modele.getJoueurs(i).cleEau + " Clés eau, " + 
  				modele.getJoueurs(i).cleAir + " Clés air, " + 
  				modele.getJoueurs(i).cleFeu + " Clés feu, " + 
  				modele.getJoueurs(i).cleTerre + " Clés terre, " + 
  				modele.getJoueurs(i).helicoptere + " Hélicoptères, " +
  				modele.getJoueurs(i).sacSable + " Sacs de sable "));
  		else this.add(new JLabel("Joueur " + (i+1) + " : " +
  				modele.getJoueurs(i).cleEau + " Clés eau, " + 
  				modele.getJoueurs(i).cleAir + " Clés air, " + 
  				modele.getJoueurs(i).cleFeu + " Clés feu, " + 
  				modele.getJoueurs(i).cleTerre + " Clés terre, " + 
  				modele.getJoueurs(i).helicoptere + " Hélicoptères, " +
  				modele.getJoueurs(i).sacSable + " Sacs de sable "));
  	}
  	this.add(value0);
  	this.add(value1);
  	this.add(value2);
  	this.add(value3);
    }

    public void update() { 
    	this.removeAll();
    	float actions = modele.getNbActions();  //actions
    	if(modele.getJoueurs(modele.getTour()).role != roles.aucun) this.add(new JLabel("Au tour du joueur " + (modele.getTour()+1) +
      			" (" + modele.getJoueurs(modele.getTour()).role + ")"));
      	else this.add(new JLabel("Au tour du joueur " + (modele.getTour()+1)));
      	this.add(new JLabel("Nombre d'actions restantes : " + (int)(3-actions+0.5)));
      	for (int i = 0; i < modele.nbJoueurs; i++) {
      		if(modele.getJoueurs(modele.getTour()).role != roles.aucun) this.add(new JLabel("Joueur " + (i+1) + 
      				" (" + modele.getJoueurs(i).role +") : " +
      				modele.getJoueurs(i).cleEau + " Clés eau, " + 
      				modele.getJoueurs(i).cleAir + " Clés air, " + 
      				modele.getJoueurs(i).cleFeu + " Clés feu, " + 
      				modele.getJoueurs(i).cleTerre + " Clés terre, " + 
      				modele.getJoueurs(i).helicoptere + " Hélicoptères, " +
      				modele.getJoueurs(i).sacSable + " Sacs de sable "));
      		else this.add(new JLabel("Joueur " + (i+1) + " : " +
      				modele.getJoueurs(i).cleEau + " Clés eau, " + 
      				modele.getJoueurs(i).cleAir + " Clés air, " + 
      				modele.getJoueurs(i).cleFeu + " Clés feu, " + 
      				modele.getJoueurs(i).cleTerre + " Clés terre, " + 
      				modele.getJoueurs(i).helicoptere + " Hélicoptères, " +
      				modele.getJoueurs(i).sacSable + " Sacs de sable "));
      	}
      	if(modele.getArtefact(0) != -1) value0 = new JLabel("Artéfact d'eau possédé par Joueur " + modele.getArtefact(0));
      	if(modele.getArtefact(1) != -1) value1 = new JLabel("Artéfact d'air possédé par Joueur " + modele.getArtefact(1));
      	if(modele.getArtefact(2) != -1) value2 = new JLabel("Artéfact de feu possédé par Joueur " + modele.getArtefact(2));
      	if(modele.getArtefact(3) != -1) value3 = new JLabel("Artéfact de terre possédé par Joueur " + modele.getArtefact(3));
      	this.add(value0);
      	this.add(value1);
      	this.add(value2);
      	this.add(value3);
        this.validate();
    	this.repaint(); }
}

class VueCommandesMenu extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * référence au modèle.
     */
    /** Constructeur. */
    public VueCommandesMenu() {
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

class CVueMenu {
    public static int resultat;
    private JFrame frame;
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

	/** Définition des deux vues et ajout à la fenêtre. */
	VueCommandesMenu commandes = new VueCommandesMenu();
	bouton.setLocation(frame.getHeight()/2, frame.getWidth()/2);
	bouton.add(commandes);
    JPanel position = new JPanel();
    position.add(text, BorderLayout.CENTER);
    position.add(bouton);
    frame.add(position, BorderLayout.CENTER);
	music("music.wav");
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	if(resultat != 0) {
		frame.dispose();
	}
    }
    
    private void music(String filepath) {			
		try {
			File musicPath = new File(filepath);
			if(musicPath.exists()) {
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip(); 
				clip.open(audioIn); 
				clip.start(); 
				clip.loop(clip.LOOP_CONTINUOUSLY);
			}else {
				System.out.println("error");
			}
		}
		catch(Exception error) {
			error.printStackTrace();
		}
	}
}

/**
* Classe pour une fenêtre à part contenant les controles du jeu
**/
class CVueControles {
    public static int resultat;
	JFrame frame;
    public CVueControles() {
	/** Définition de la fenêtre principale. */
	frame = new JFrame();
	frame.setTitle("Controles");
	JPanel text = new JPanel();   
	text.setLayout(new BoxLayout(text, BoxLayout.PAGE_AXIS));
    text.add(new JLabel("Déplacement d'un joueur : flèches du clavier"));
    text.add(new JLabel("Assèchement d'une zone : boutons"));
    text.add(new JLabel("Fin de tour : touche entrée"));
    text.add(new JLabel("Récupération d'un artéfact : touche espace"));
    text.add(new JLabel("Echange de clé : Fx suivi de y où :"));
    text.add(new JLabel("     x corresponds au numéro du joueur auquel on souhaite donner une clé"));
    text.add(new JLabel("     y corresponds à l'initiale de la ressource que l'on souhaite donner :"));
    text.add(new JLabel("          E = Eau"));
    text.add(new JLabel("          A = Air"));
    text.add(new JLabel("          F = Feu"));
    text.add(new JLabel("          T = Terre"));
    text.add(new JLabel("Exemple : F1 suivi de E permet de donner une clé d'eau au joueur 1"));
    text.add(new JLabel("Utilisation hélicoptère : Clique gauche sur la zone d'arrivée"));
    text.add(new JLabel("Utilisation sac de sable : Clique gauche sur la zone à assécher"));
    text.add(new JLabel("Actions spéciales :"));
    text.add(new JLabel("     Pilote : Clique sur la roulette de la souris puis clique gauche sur la zone d'arrivée"));
    text.add(new JLabel("     Explorateur : Clique sur la roulette puis clique gauche sur la zone ciblée pour le déplacement ou clique droit pour assécher"));
    JPanel position = new JPanel();
    position.add(text);
    frame.add(position);
    frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }
}

/**
 * Classe pour notre contrôleur
 *
 * Le contrôleur implémente les interfaces ActionListener, LeyListener et MouseListener
 */
class Controleur implements ActionListener, KeyListener, MouseListener {
    /**
     * On garde un pointeur vers le modèle, car le contrôleur doit
     * provoquer un appel de méthode du modèle.
     */
    private CModele modele;
    private boolean[] j;
    private boolean pouvoir = false;
    public Controleur(CModele modele) { 
    	this.modele = modele; 
    	j = new boolean[modele.nbJoueurs];
    	for (int i = 0; i < modele.nbJoueurs; i++) {
    		j[i] = false;
    	}
    }

    /**
    * Gère les événements du clavier
    **/
    public void keyPressed(KeyEvent e) {
    	if (modele.victoire() || modele.defaite() != 0) return;
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		//Les 4 cas suivants correspondent aux flèches du clavier pour le déplacement
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
		//ENTER fait le passage au tour suivant
		case KeyEvent.VK_ENTER:
			modele.tourSuivant();
			break;
		//SPACE permet de récupérer un artéfact
		case KeyEvent.VK_SPACE:
			modele.recupere();
			break;
		//Choix de l'artéfact à échanger
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
	    	//On annule l'action d'échanger un artéfact
		for (int i = 0; i < modele.nbJoueurs; i++) {
			j[i] = false;
		}
	    	//Choix du joueur pour l'échange d'un artéfact
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
	/**
	*Réagit si on appuie sur un bouton
	**/
	public void actionPerformed(ActionEvent e) {
		if (modele.victoire() || modele.defaite() != 0) return;
		String actionCode = e.getActionCommand();
		switch (actionCode) {
		case "h":
			modele.asseche(false, modele.getJoueurs(modele.getTour()).x, modele.getJoueurs(modele.getTour()).y-1);
			break;
		case "b":
			modele.asseche(false, modele.getJoueurs(modele.getTour()).x, modele.getJoueurs(modele.getTour()).y+1);
			break;
		case "d":
			modele.asseche(false, modele.getJoueurs(modele.getTour()).x+1, modele.getJoueurs(modele.getTour()).y);
			break;
		case "g":
			modele.asseche(false, modele.getJoueurs(modele.getTour()).x-1, modele.getJoueurs(modele.getTour()).y);;
			break;
		case "o":
			modele.asseche(false, modele.getJoueurs(modele.getTour()).x, modele.getJoueurs(modele.getTour()).y);
			break;
		case "Abandonner":
			modele.abandonner();
			break;
		}
		
	}

	@Override
	/**
	* Réagit au clic de souris
	**/
	public void mouseClicked(MouseEvent m) {
		int mouseCode = m.getButton();
		//Si on ne souhaite pas utiliser la capacité spéciale du personnage
		if(!pouvoir) {
			switch (mouseCode) {
			//Clique gauche = hélicoptère
			case MouseEvent.BUTTON1:
				modele.prendreHelicoptere(m.getX()/40+1, m.getY()/40+1);
				break;
			//Clique sur la roulette pour activer un pouvoir
			case MouseEvent.BUTTON2:
				pouvoir = true;
				break;
			//Clique droit = sac sable
			case MouseEvent.BUTTON3:
				modele.asseche(true, m.getX()/40+1, m.getY()/40+1);
				break;
			}	
		}
		else {
			pouvoir = false;
			switch (mouseCode) {
			case MouseEvent.BUTTON1:
				modele.utilisePouvoir(true, m.getX()/40+1, m.getY()/40+1);
				break;
			case MouseEvent.BUTTON3:
				modele.utilisePouvoir(false, m.getX()/40+1, m.getY()/40+1);
				break;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}

class ControleurMenu implements ActionListener{
    public ControleurMenu() { }
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCode = e.getActionCommand();
		switch (actionCode) {
		case "2":
			CVueMenu.resultat = 2;
			CModele modele2 = new CModele(2);
			new CVue(modele2); 
			break;
		case "3":
			CVueMenu.resultat = 3;
			CModele modele3 = new CModele(3);
			new CVue(modele3);
			break;
		case "4":
			CVueMenu.resultat = 4;
			CModele modele4 = new CModele(4);
			new CVue(modele4);
			break;
		}
		new CVueControles();
		
	}
}