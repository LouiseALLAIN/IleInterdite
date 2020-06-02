import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

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
