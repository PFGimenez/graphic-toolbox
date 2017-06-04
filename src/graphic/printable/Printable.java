/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package graphic.printable;

import java.awt.Graphics;
import graphic.Fenetre;

/**
 * Élément affichable
 * 
 * @author pf
 *
 */

public interface Printable
{
	/**
	 * Affiche cet objet
	 * 
	 * @param g
	 */
	public void print(Graphics g, Fenetre f);

	/**
	 * Récupère le layer sur lequel afficher l'objet
	 * 
	 * @return
	 */
	public Layer getLayer();
}
