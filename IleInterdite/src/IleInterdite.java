import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;


/**
 * Interface des objets observateurs.
 */ 
interface Observer {
    /**
     * Un observateur doit poss�der une m�thode [update] d�clenchant la mise �
     * jour.
     */
    public void update();
    /**
     * La version officielle de Java poss�de des param�tres pr�cisant le
     * changement qui a eu lieu.
     */
}

/**
 * Classe des objets pouvant �tre observ�s.
 */
abstract class Observable {
    /**
     * On a une liste [observers] d'observateurs, initialement vide, � laquelle
     * viennent s'inscrire les observateurs via la m�thode [addObserver].
     */
    private ArrayList<Observer> observers;
    public Observable() {
	this.observers = new ArrayList<Observer>();
    }
    public void addObserver(Observer o) {
	observers.add(o);
    }

    /**
     * Lorsque l'�tat de l'objet observ� change, il est convenu d'appeler la
     * m�thode [notifyObservers] pour pr�venir l'ensemble des observateurs
     * enregistr�s.
     * On le fait ici concr�tement en appelant la m�thode [update] de chaque
     * observateur.
     */
    public void notifyObservers() {
	for(Observer o : observers) {
	    o.update();
	}
    }
}
/** Fin du sch�ma observateur/observ�. */


/**
 * Nous allons commencer � construire notre application, en voici la classe
 * principale.
 */
public class IleInterdite {
    /**
     * L'amor�age est fait en cr�ant le mod�le et la vue, par un simple appel
     * � chaque constructeur.
     * Ici, le mod�le est cr�� ind�pendamment (il s'agit d'une partie autonome
     * de l'application), et la vue prend le mod�le comme param�tre (son
     * objectif est de faire le lien entre mod�le et utilisateur).
     */
    public static void main(String[] args) {
	/**
	 * Pour les besoins du jour on consid�re la ligne EvenQueue... comme une
	 * incantation qu'on pourra expliquer plus tard.
	 */
    	EventQueue.invokeLater(() -> {
    		/** Voici le contenu qui nous int�resse. */
    				
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
 * Le mod�le : le coeur de l'application.
 *
 * Le mod�le �tend la classe [Observable] : il va poss�der un certain nombre
 * d'observateurs (ici, un : la partie de la vue responsable de l'affichage)
 * et devra les pr�venir avec [notifyObservers] lors des modifications.
 * Voir la m�thode [avance()] pour cela.
 */
class CModele extends Observable {
    /** On fixe la taille de la grille. */
    public static final int HAUTEUR=10, LARGEUR=15;
	boolean abandon = false;
    /** On stocke un tableau de cellules. */
    private Cellule[][] cellules;
    public int nbJoueurs;
    private Joueur[] joueurs;
    private int tour;
    int[] artefacts = new int[4];

    public Joueur[] getJoueurs() {
		return joueurs;
	}

	public int getTour() {
		return tour;
	}

	/** Construction : on initialise un tableau de cellules. **/
    public CModele(int nbJoueurs) {
	/**
	 * Pour �viter les probl�mes aux bords, on ajoute une ligne et une
	 * colonne de chaque c�t�, dont les cellules n'�volueront pas.
	 */ 
    	for (int i = 0; i < 4; i++) {
    		artefacts[i] = -1;
    	}
    	this.nbJoueurs = nbJoueurs;
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
     * Initialisation al�atoire des cellules, except�es celle des bords qui
     * ont �t� ajout�s.
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
     * Calcul de la g�n�ration suivante.
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
		 * Pour finir, le mod�le ayant chang�, on signale aux observateurs
		 * qu'ils doivent se mettre � jour.
		 */
		notifyObservers();
    }
    
    public void deplace(int k) {
		cellules[joueurs[tour].x][joueurs[tour].y].presenceJoueur = false;
		if(k == KeyEvent.VK_RIGHT && cellules[joueurs[tour].x+1][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x+1 <= LARGEUR) joueurs[tour].x+=1;
		else if(k == KeyEvent.VK_LEFT && cellules[joueurs[tour].x-1][joueurs[tour].y].etat != etat.submergee && joueurs[tour].x-1 > 0) joueurs[tour].x-=1;
		else if(k == KeyEvent.VK_UP && cellules[joueurs[tour].x][joueurs[tour].y-1].etat != etat.submergee && joueurs[tour].y-1 > 0) joueurs[tour].y-=1;
		else if (k == KeyEvent.VK_DOWN && cellules[joueurs[tour].x][joueurs[tour].y+1].etat != etat.submergee && joueurs[tour].y+1 <= HAUTEUR) joueurs[tour].y+=1;
	    joueurs[tour].nbActions++;
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
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.eau && joueurs[tour].cleEau > 0) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleEau = 0;
    		joueurs[tour].nbActions++;
    		artefacts[0] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.air && joueurs[tour].cleAir > 0) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleAir = 0;
    		joueurs[tour].nbActions++;
    		artefacts[1] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.feu && joueurs[tour].cleFeu > 0) {
    		cellules[joueurs[tour].x][joueurs[tour].y].type = elements.autre;
    		joueurs[tour].cleFeu = 0;
    		joueurs[tour].nbActions++;
    		artefacts[2] = tour+1;
    	}
    	if (cellules[joueurs[tour].x][joueurs[tour].y].type == elements.terre && joueurs[tour].cleTerre > 0) {
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
    
    


    /**
     * Une m�thode pour renvoyer la cellule aux coordonn�es choisies (sera
     * utilis�e par la vue).
     */
    public Cellule getCellule(int x, int y) {
	return cellules[x][y];
    }

	public boolean abandon() {
		return this.abandon;
	}
}

/** Fin de la classe CModele. */

/**
 * D�finition d'une classe pour les cellules.
 * Cette classe fait encore partie du mod�le.
 */
class Cellule {
    /** On conserve un pointeur vers la classe principale du mod�le. */
    private CModele modele;

    /** L'�tat d'une cellule est donn� par un bool�en. */
    protected etat etat;
    protected elements type;
    protected boolean presenceJoueur;
    /**
     * On stocke les coordonn�es pour pouvoir les passer au mod�le lors
     * de l'appel � [compteVoisines].
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
/** Fin de la classe Cellule, et du mod�le en g�n�ral. */

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
 * On d�finit une classe chapeau [CVue] qui cr�e la fen�tre principale de 
 * l'application et contient les deux parties principales de notre vue :
 *  - Une zone d'affichage o� on voit l'ensemble des cellules.
 *  - Une zone de commande avec un bouton pour passer � la g�n�ration suivante.
 */
class CVue {
    /**
     * JFrame est une classe fournie pas Swing. Elle repr�sente la fen�tre
     * de l'application graphique.
     */
    private JFrame frame;
    /**
     * VueGrille et VueCommandes sont deux classes d�finies plus loin, pour
     * nos deux parties de l'interface graphique.
     */
    private VueGrille grille;
    private VueCommandes commandes;
    private VuePlayer player;
    /** Construction d'une vue attach�e � un mod�le. */
    public CVue(CModele modele) {
	/** D�finition de la fen�tre principale. */
	frame = new JFrame();
	frame.setTitle("L'�le interdite");
	JPanel text = new JPanel();
	text.setLayout(new BoxLayout(text, BoxLayout.LINE_AXIS));
    text.add(new JLabel("Cliquer pour ass�cher une zone inond�e"));
    JPanel bouton = new JPanel();
 
    
    /**
	 * On pr�cise un mode pour disposer les diff�rents �l�ments �
	 * l'int�rieur de la fen�tre. Quelques possibilit�s sont :
	 *  - BorderLayout (d�faut pour la classe JFrame) : chaque �l�ment est
	 *    dispos� au centre ou le long d'un bord.
	 *  - FlowLayout (d�faut pour un JPanel) : les �l�ments sont dispos�s
	 *    l'un � la suite de l'autre, dans l'ordre de leur ajout, les lignes
	 *    se formant de gauche � droite et de haut en bas. Un �l�ment peut
	 *    passer � la ligne lorsque l'on redimensionne la fen�tre.
	 *  - GridLayout : les �l�ments sont dispos�s l'un � la suite de
	 *    l'autre sur une grille avec un nombre de lignes et un nombre de
	 *    colonnes d�finis par le programmeur, dont toutes les cases ont la
	 *    m�me dimension. Cette dimension est calcul�e en fonction du
	 *    nombre de cases � placer et de la dimension du contenant.
	 */
	frame.setLayout(new FlowLayout());
	//frame.setLayout(new BorderLayout());


	/** D�finition des deux vues et ajout � la fen�tre. */
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
	 * Remarque : on peut passer � la m�thode [add] des param�tres
	 * suppl�mentaires indiquant o� placer l'�l�ment. Par exemple, si on
	 * avait conserv� la disposition par d�faut [BorderLayout], on aurait
	 * pu �crire le code suivant pour placer la grille � gauche et les
	 * commandes � droite.
	 *     frame.add(grille, BorderLayout.WEST);
	 *     frame.add(commandes, BorderLayout.EAST);
	 */

	/**
	 * Fin de la plomberie :
	 *  - Ajustement de la taille de la fen�tre en fonction du contenu.
	 *  - Indiquer qu'on quitte l'application si la fen�tre est ferm�e.
	 *  - Pr�ciser que la fen�tre doit bien appara�tre � l'�cran.
	 */
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }
}

class VueGrille extends JPanel implements Observer {
    /** On maintient une r�f�rence vers le mod�le. */
    private CModele modele;
    /** D�finition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 40;
    JLabel fin = new JLabel(" ");
    /** Constructeur. */
    public VueGrille(CModele modele) {
	this.modele = modele;
	/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
	modele.addObserver(this);
	/**
	 * D�finition et application d'une taille fixe pour cette zone de
	 * l'interface, calcul�e en fonction du nombre de cellules et de la
	 * taille d'affichage.
	 */
	Dimension dim = new Dimension(TAILLE*CModele.LARGEUR,
				      TAILLE*CModele.HAUTEUR);
	this.setPreferredSize(dim);
	this.add(fin);
    }

    /**
     * L'interface [Observer] demande de fournir une m�thode [update], qui
     * sera appel�e lorsque la vue sera notifi�e d'un changement dans le
     * mod�le. Ici on se content de r�afficher toute la grille avec la m�thode
     * pr�d�finie [repaint].
     */
    public void update() { repaint(); }

    public void paintComponent(Graphics g) {
	super.repaint();
	/** Pour chaque cellule... */
	if(modele.abandon()) {
		this.removeAll();
		g.setColor(Color.WHITE);
		g.fillRect(0,  0,  TAILLE*CModele.LARGEUR,
				      TAILLE*CModele.HAUTEUR);
		fin.setLayout(new BoxLayout(fin, BoxLayout.X_AXIS));
		fin = new JLabel("ABANDON...");
		Font font = new Font("Arial", Font.BOLD, 96);
		fin.setFont(font);
		this.add(fin);
		this.validate();
	}
	
	if(!modele.victoire()) {
		for(int i=1; i<=CModele.LARGEUR; i++) {
		    for(int j=1; j<=CModele.HAUTEUR; j++) {
			/**
			 * ... Appeler une fonction d'affichage auxiliaire.
			 * On lui fournit les informations de dessin [g] et les
			 * coordonn�es du coin en haut � gauche.
			 */
			paint(g, modele.getCellule(i, j), (i-1)*TAILLE, (j-1)*TAILLE);
		    }
		}
	}else {
		this.removeAll();
		g.setColor(Color.WHITE);
		g.fillRect(0,  0,  TAILLE*CModele.LARGEUR,
				      TAILLE*CModele.HAUTEUR);
		fin.setLayout(new BoxLayout(fin, BoxLayout.X_AXIS));
		fin = new JLabel("VICTOIRE !");
		Font font = new Font("Arial", Font.BOLD, 96);
		fin.setFont(font);
		this.add(fin);
		this.validate();
		
	}
    }
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut �tre d�sign�e que par l'interm�diaire
     * de la classe [CModele] � laquelle elle est interne, d'o� type
     * [CModele.Cellule].
     * Ceci serait impossible si [Cellule] �tait d�clar�e priv�e dans [CModele].
     */
    private void paint(Graphics g, Cellule c, int x, int y) {
        /** S�lection d'une couleur. */
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
  				modele.getJoueurs()[i].cleEau + " Cl�s eau, " + 
  				modele.getJoueurs()[i].cleAir + " Cl�s air, " + 
  				modele.getJoueurs()[i].cleFeu + " Cl�s feu, " + 
  				modele.getJoueurs()[i].cleTerre + " Cl�s terre" ));
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
      				modele.getJoueurs()[i].cleEau + " Cl�s eau, " + 
      				modele.getJoueurs()[i].cleAir + " Cl�s air, " + 
      				modele.getJoueurs()[i].cleFeu + " Cl�s feu, " + 
      				modele.getJoueurs()[i].cleTerre + " Cl�s terre" ));
      	}
      	if(modele.artefacts[0] != -1) value0 = new JLabel("Art�fact d'eau poss�d� par Joueur " + modele.artefacts[0]);
      	if(modele.artefacts[1] != -1) value1 = new JLabel("Art�fact d'air poss�d� par Joueur " + modele.artefacts[1]);
      	if(modele.artefacts[2] != -1) value2 = new JLabel("Art�fact de feu poss�d� par Joueur " + modele.artefacts[2]);
      	if(modele.artefacts[3] != -1) value3 = new JLabel("Art�fact de terre poss�d� par Joueur " + modele.artefacts[3]);
      	this.add(value0);
      	this.add(value1);
      	this.add(value2);
      	this.add(value3);
        this.validate();
    	this.repaint(); }
}

/**
 * Une classe pour repr�senter la zone contenant le bouton.
 *
 * Cette zone n'aura pas � �tre mise � jour et ne sera donc pas un observateur.
 * En revanche, comme la zone pr�c�dente, celle-ci est un panneau [JPanel].
 */
class VueCommandes extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * r�f�rence au mod�le.
     */
    private CModele modele;
    
    /** Constructeur. */
    public VueCommandes(CModele modele) {
		this.modele = modele;
		/**
		 * On cr�e un nouveau bouton, de classe [JButton], en pr�cisant le
		 * texte qui doit l'�tiqueter.
		 * Puis on ajoute ce bouton au panneau [this].
		 */
		JButton AssecheHaut = new JButton("h");
		JButton AssecheBas = new JButton("b");
		JButton Asseche = new JButton("o");
		JButton AssecheGauche = new JButton("g");
		JButton AssecheDroite = new JButton("d"); 
		JButton Abandon = new JButton("Abandonner"); 
		this.add(AssecheHaut);
		this.add(AssecheBas);
		this.add(Asseche);
		this.add(AssecheGauche);
		this.add(AssecheDroite);
		this.add(Abandon);
		JButton osef = new JButton("t");
		Controleur ctrl = new Controleur(modele);
		/** Enregistrement du contr�leur comme auditeur du bouton. */
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
		Abandon.addActionListener(ctrl);
		
		/**
		 * Variante : une lambda-expression qui �vite de cr�er une classe
	         * sp�cifique pour un contr�leur simplissime.
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
 * Classe pour notre contr�leur rudimentaire.
 *
 * Le contr�leur impl�mente l'interface [ActionListener] qui demande
 * uniquement de fournir une m�thode [actionPerformed] indiquant la
 * r�ponse du contr�leur � la r�ception d'un �v�nement.
 */
class Controleur implements ActionListener, KeyListener {
    /**
     * On garde un pointeur vers le mod�le, car le contr�leur doit
     * provoquer un appel de m�thode du mod�le.
     * Remarque : comme cette classe est interne, cette inscription
     * explicite du mod�le est inutile. On pourrait se contenter de
     * faire directement r�f�rence au mod�le enregistr� pour la classe
     * englobante [VueCommandes].
     */
    CModele modele;
    public Controleur(CModele modele) { this.modele = modele; }

    public void keyPressed(KeyEvent e) {
    	if (modele.victoire()) return;
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
		if (modele.victoire()) return;
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
		}
		
	}
}

/** Fin du contr�leur. */

class CVueMenu {
    public static int resultat;
	/**
     * JFrame est une classe fournie pas Swing. Elle repr�sente la fen�tre
     * de l'application graphique.
     */
    private JFrame frame;
    /**
     * VueGrille et VueCommandes sont deux classes d�finies plus loin, pour
     * nos deux parties de l'interface graphique.
     */
    private VueMenu grille;
    private VueCommandesMenu commandes;
    /** Construction d'une vue attach�e � un mod�le. */
    public CVueMenu() {
	/** D�finition de la fen�tre principale. */
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
	 * On pr�cise un mode pour disposer les diff�rents �l�ments �
	 * l'int�rieur de la fen�tre. Quelques possibilit�s sont :
	 *  - BorderLayout (d�faut pour la classe JFrame) : chaque �l�ment est
	 *    dispos� au centre ou le long d'un bord.
	 *  - FlowLayout (d�faut pour un JPanel) : les �l�ments sont dispos�s
	 *    l'un � la suite de l'autre, dans l'ordre de leur ajout, les lignes
	 *    se formant de gauche � droite et de haut en bas. Un �l�ment peut
	 *    passer � la ligne lorsque l'on redimensionne la fen�tre.
	 *  - GridLayout : les �l�ments sont dispos�s l'un � la suite de
	 *    l'autre sur une grille avec un nombre de lignes et un nombre de
	 *    colonnes d�finis par le programmeur, dont toutes les cases ont la
	 *    m�me dimension. Cette dimension est calcul�e en fonction du
	 *    nombre de cases � placer et de la dimension du contenant.
	 */
	
	//frame.setLayout(new BorderLayout());


	/** D�finition des deux vues et ajout � la fen�tre. */
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
	 * Remarque : on peut passer � la m�thode [add] des param�tres
	 * suppl�mentaires indiquant o� placer l'�l�ment. Par exemple, si on
	 * avait conserv� la disposition par d�faut [BorderLayout], on aurait
	 * pu �crire le code suivant pour placer la grille � gauche et les
	 * commandes � droite.
	 *     frame.add(grille, BorderLayout.WEST);
	 *     frame.add(commandes, BorderLayout.EAST);
	 */

	/**
	 * Fin de la plomberie :
	 *  - Ajustement de la taille de la fen�tre en fonction du contenu.
	 *  - Indiquer qu'on quitte l'application si la fen�tre est ferm�e.
	 *  - Pr�ciser que la fen�tre doit bien appara�tre � l'�cran.
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
    /** On maintient une r�f�rence vers le mod�le. */
    /** D�finition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 20;

    /** Constructeur. */
    public VueMenu() {
	/**
	 * D�finition et application d'une taille fixe pour cette zone de
	 * l'interface, calcul�e en fonction du nombre de cellules et de la
	 * taille d'affichage.
	 */
	Dimension dim = new Dimension(TAILLE*CModele.LARGEUR,
				      TAILLE*CModele.HAUTEUR);
	this.setPreferredSize(dim);
    }

    /**
     * L'interface [Observer] demande de fournir une m�thode [update], qui
     * sera appel�e lorsque la vue sera notifi�e d'un changement dans le
     * mod�le. Ici on se content de r�afficher toute la grille avec la m�thode
     * pr�d�finie [repaint].
     */
    public void update() { repaint(); }

    /**
     * Les �l�ments graphiques comme [JPanel] poss�dent une m�thode
     * [paintComponent] qui d�finit l'action � accomplir pour afficher cet
     * �l�ment. On la red�finit ici pour lui confier l'affichage des cellules.
     *
     * La classe [Graphics] regroupe les �l�ments de style sur le dessin,
     * comme la couleur actuelle.
     */
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut �tre d�sign�e que par l'interm�diaire
     * de la classe [CModele] � laquelle elle est interne, d'o� le type
     * [CModele.Cellule].
     * Ceci serait impossible si [Cellule] �tait d�clar�e priv�e dans [CModele].
     */
        /** Coloration d'un rectangle. */
    
}






class VueCommandesMenu extends JPanel {
    /**
     * Pour que le bouton puisse transmettre ses ordres, on garde une
     * r�f�rence au mod�le.
     */
    /** Constructeur. */
    public VueCommandesMenu() {
		/**
		 * On cr�e un nouveau bouton, de classe [JButton], en pr�cisant le
		 * texte qui doit l'�tiqueter.
		 * Puis on ajoute ce bouton au panneau [this].
		 */
        JPanel boutons = new JPanel();
    	boutons.setLayout(new BoxLayout(boutons, BoxLayout.LINE_AXIS));
        boutons.add(new JLabel("Combien d'aventuriers partiront en exp�dition? "));
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
		/** Enregistrement du contr�leur comme auditeur du bouton. */
		deuxJoueurs.addActionListener(ctrlM);
		troisJoueurs.addActionListener(ctrlM);
		quatreJoueurs.addActionListener(ctrlM);
		
	
    }
}






class ControleurMenu implements ActionListener{
    /**
     * On garde un pointeur vers le mod�le, car le contr�leur doit
     * provoquer un appel de m�thode du mod�le.
     * Remarque : comme cette classe est interne, cette inscription
     * explicite du mod�le est inutile. On pourrait se contenter de
     * faire directement r�f�rence au mod�le enregistr� pour la classe
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

/** Fin du contr�leur. */