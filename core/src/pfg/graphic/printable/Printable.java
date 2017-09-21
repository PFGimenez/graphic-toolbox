/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic.printable;

import java.awt.Graphics;
import java.io.Serializable;

import pfg.graphic.Chart;
import pfg.graphic.GraphicPanel;

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
	public void print(Graphics g, GraphicPanel f, Chart a);
}
