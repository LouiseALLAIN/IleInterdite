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
		JButton AssecheHautGauche = new JButton("hg");
		JButton AssecheHautDroite = new JButton("hd");
		JButton AssecheBasGauche = new JButton("bg");
		JButton AssecheBasDroite = new JButton("bd"); 
		this.add(AssecheHaut);
		this.add(AssecheBas);
		this.add(Asseche);
		this.add(AssecheGauche);
		this.add(AssecheDroite);
		this.add(AssecheHautDroite);
		this.add(AssecheHautGauche);
		this.add(AssecheBasDroite);
		this.add(AssecheBasGauche);
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
		AssecheHautDroite.addActionListener(ctrl);
		AssecheBasDroite.addActionListener(ctrl);
		AssecheBasGauche.addActionListener(ctrl);
		AssecheHautGauche.addActionListener(ctrl);
		AssecheHautDroite.addKeyListener(ctrl);
		AssecheBasDroite.addKeyListener(ctrl);
		AssecheBasGauche.addKeyListener(ctrl);
		AssecheHautGauche.addKeyListener(ctrl);
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
	/**
     * JFrame est une classe fournie pas Swing. Elle représente la fenêtre
     * de l'application graphique.
     */
    private JFrame frame;
    /**
     * VueGrille et VueCommandes sont deux classes définies plus loin, pour
     * nos deux parties de l'interface graphique.
     */
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
class CVueControles {
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
    /** Construction d'une vue attachée à un modèle. */
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
    text.add(new JLabel("     Explorateur : Boutons pour assécher + clique sur la roulette puis clique gauche sur la zone où aller pour le déplacement diagonal"));
    JPanel position = new JPanel();
    position.add(text);
    frame.add(position);
    frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }
}



