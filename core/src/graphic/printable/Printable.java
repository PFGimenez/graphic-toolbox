/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package graphic.printable;

import java.awt.Graphics;
import java.io.Serializable;
import graphic.Fenetre;

/**
 * Élément affichable
 * 
 * @author pf
 *
 */

public interface Printable extends Serializable
{
	/**
	 * Affiche cet objet
	 * 
	 * @param g
	 */
	public void print(Graphics g, Fenetre f);

	/**
	 * Get the layer the object will be printed on.
	 * Low values for background, high values for foreground
	 * 
	 * @return
	 */
	public int getLayer();
}
