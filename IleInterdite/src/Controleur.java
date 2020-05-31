import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Classe pour notre contrôleur rudimentaire.
 *
 * Le contrôleur implémente l'interface [ActionListener] qui demande
 * uniquement de fournir une méthode [actionPerformed] indiquant la
 * réponse du contrôleur à la réception d'un événement.
 */
class Controleur implements ActionListener, KeyListener, MouseListener {
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
			modele.tourSuivant();
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
	public void mouseClicked(MouseEvent m) {
		int mouseCode = m.getButton();
		switch (mouseCode) {
		case MouseEvent.BUTTON1:
			modele.prendreHelicoptere(m.getX()/40+1, m.getY()/40+1);
			break;
		case MouseEvent.BUTTON3:
			modele.asseche(true, m.getX()/40+1, m.getY()/40+1);
			break;
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
