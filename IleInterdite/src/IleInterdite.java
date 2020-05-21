import java.util.*;
import java.awt.*;
import java.awt.event.*;
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
                CModele modele = new CModele();
                CVue vue = new CVue(modele);
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
    Joueur joueur1;
    Joueur joueur2;
    int tour;

    /** Construction : on initialise un tableau de cellules. */
    public CModele() {
	/**
	 * Pour éviter les problèmes aux bords, on ajoute une ligne et une
	 * colonne de chaque côté, dont les cellules n'évolueront pas.
	 */ 
    	int x = (int)(Math.random() * LARGEUR + 1);
    	int y = (int)(Math.random() * HAUTEUR + 1);
	    joueur1 = new Joueur(x, y);
	    joueur2 = new Joueur(x, y);
	    tour = 1;
		cellules = new Cellule[LARGEUR+2][HAUTEUR+2];
		for(int i=0; i<LARGEUR+2; i++) {
		    for(int j=0; j<HAUTEUR+2; j++) {
			cellules[i][j] = new Cellule(this,i, j);
				if (i == joueur1.x && j == joueur1.y) {
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
		if(tour == 1) {
			joueur1.nbActions=0;
			tour = 2;
		}
		else {
			joueur2.nbActions = 0;
			tour = 1;
		}
		/**
		 * Pour finir, le modèle ayant changé, on signale aux observateurs
		 * qu'ils doivent se mettre à jour.
		 */
		notifyObservers();
    }
    
    public void deplace(int k) {
    	if(tour == 1) {
	    	if(joueur1.nbActions < 3) {
		    	cellules[joueur1.x][joueur1.y].presenceJoueur = false;
		    	if(k == KeyEvent.VK_RIGHT && cellules[joueur1.x+1][joueur1.y].etat != etat.submergee && joueur1.x+1 <= LARGEUR) joueur1.x+=1;
				else if(k == KeyEvent.VK_LEFT && cellules[joueur1.x-1][joueur1.y].etat != etat.submergee && joueur1.x-1 > 0) joueur1.x-=1;
				else if(k == KeyEvent.VK_UP && cellules[joueur1.x][joueur1.y-1].etat != etat.submergee && joueur1.y-1 > 0) joueur1.y-=1;
				else if (k == KeyEvent.VK_DOWN && cellules[joueur1.x][joueur1.y+1].etat != etat.submergee && joueur1.y+1 <= HAUTEUR) joueur1.y+=1;
	    	}
	    	joueur1.nbActions++;
    	}
    	else {
    		if(joueur2.nbActions < 3) {
		    	cellules[joueur2.x][joueur2.y].presenceJoueur = false;
		    	if(k == KeyEvent.VK_RIGHT && cellules[joueur2.x+1][joueur2.y].etat != etat.submergee && joueur2.x+1 <= LARGEUR) joueur2.x+=1;
				else if(k == KeyEvent.VK_LEFT && cellules[joueur2.x-1][joueur2.y].etat != etat.submergee && joueur2.x-1 > 0) joueur2.x-=1;
				else if(k == KeyEvent.VK_UP && cellules[joueur2.x][joueur2.y-1].etat != etat.submergee && joueur2.y-1 > 0) joueur2.y-=1;
				else if (k == KeyEvent.VK_DOWN && cellules[joueur2.x][joueur2.y+1].etat != etat.submergee && joueur2.y+1 <= HAUTEUR) joueur2.y+=1;
		    	cellules[joueur2.x][joueur2.y].presenceJoueur = true;
	    	}
	    	joueur2.nbActions++;
    	}
    	cellules[joueur1.x][joueur1.y].presenceJoueur = true;
    	cellules[joueur2.x][joueur2.y].presenceJoueur = true;
    	if (joueur1.nbActions == 3 || joueur2.nbActions == 3) avance();
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
	public int x, y;
	public int nbActions;
	private CModele modele;
	
	public Joueur(int x, int y) {
		this.modele = modele;
		this.x = x;
		this.y = y;
		this.nbActions = 0;
	}
	
	protected void caseSuivante(int k) {
		if(k == KeyEvent.VK_RIGHT) x+=1;
		else if(k == KeyEvent.VK_LEFT) x-=1;
		else if(k == KeyEvent.VK_UP) y-=1;
		else if (k == KeyEvent.VK_DOWN) y+=1;
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

    /** Construction d'une vue attachée à un modèle. */
    public CVue(CModele modele) {
	/** Définition de la fenêtre principale. */
	frame = new JFrame();
	frame.setTitle("Jeu de la vie de Conway");
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

	/** Définition des deux vues et ajout à la fenêtre. */
	grille = new VueGrille(modele);
	frame.add(grille);
	commandes = new VueCommandes(modele);
	frame.add(commandes);
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


/**
 * Une classe pour représenter la zone d'affichage des cellules.
 *
 * JPanel est une classe d'éléments graphiques, pouvant comme JFrame contenir
 * d'autres éléments graphiques.
 *
 * Cette vue va être un observateur du modèle et sera mise à jour à chaque
 * nouvelle génération des cellules.
 */
class VueGrille extends JPanel implements Observer {
    /** On maintient une référence vers le modèle. */
    private CModele modele;
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 40;

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
    public void paintComponent(Graphics g) {
	super.repaint();
	/** Pour chaque cellule... */
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
    }
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut être désignée que par l'intermédiaire
     * de la classe [CModele] à laquelle elle est interne, d'où le type
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
		JButton boutonAvance = new JButton("prochain tour");
		this.add(boutonAvance);
		Controleur ctrl = new Controleur(modele);
		Controleur2 ctrl2 = new Controleur2(modele);
		/** Enregistrement du contrôleur comme auditeur du bouton. */
		boutonAvance.addKeyListener(ctrl);
		boutonAvance.addActionListener(ctrl2);
		
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
class Controleur implements KeyListener {
    /**
     * On garde un pointeur vers le modèle, car le contrôleur doit
     * provoquer un appel de méthode du modèle.
     * Remarque : comme cette classe est interne, cette inscription
     * explicite du modèle est inutile. On pourrait se contenter de
     * faire directement référence au modèle enregistré pour la classe
     * englobante [VueCommandes].
     */
    CModele modele;
    public Controleur(CModele modele) { this.modele = modele; }
    /**
     * Action effectuée à réception d'un événement : appeler la
     * méthode [avance] du modèle.
     */
    public void actionPerformed(ActionEvent e) {
    	if (e.getActionCommand() == "prochain tour") modele.avance();
    	else if (e.getActionCommand() == "^") modele.deplace(KeyEvent.VK_UP);
    	else if (e.getActionCommand() == "v") modele.deplace(KeyEvent.VK_DOWN);
    	else if (e.getActionCommand() == ">") modele.deplace(KeyEvent.VK_RIGHT);
    	else if (e.getActionCommand() == "<") modele.deplace(KeyEvent.VK_LEFT);
    }
    
    public void keyPressed(KeyEvent e) {
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
}



class Controleur2 implements ActionListener {
    /**
     * On garde un pointeur vers le modèle, car le contrôleur doit
     * provoquer un appel de méthode du modèle.
     * Remarque : comme cette classe est interne, cette inscription
     * explicite du modèle est inutile. On pourrait se contenter de
     * faire directement référence au modèle enregistré pour la classe
     * englobante [VueCommandes].
     */
    CModele modele;
    public Controleur2(CModele modele) { this.modele = modele; }
    /**
     * Action effectuée à réception d'un événement : appeler la
     * méthode [avance] du modèle.
     */
    public void actionPerformed(ActionEvent e) {
    	if (e.getActionCommand() == "prochain tour") modele.avance();
    	else if (e.getActionCommand() == "^") modele.deplace(KeyEvent.VK_UP);
    	else if (e.getActionCommand() == "v") modele.deplace(KeyEvent.VK_DOWN);
    	else if (e.getActionCommand() == ">") modele.deplace(KeyEvent.VK_RIGHT);
    	else if (e.getActionCommand() == "<") modele.deplace(KeyEvent.VK_LEFT);
    }
}

/** Fin du contrôleur. */