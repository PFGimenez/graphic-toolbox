/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic.printable;

import java.io.Serializable;

import pfg.graphic.Chart;

/**
 * Élément affichable
 * 
 * @author pf
 *
 */

public interface Plottable extends Serializable
{
	/**
	 * Plot that object
	 * 
	 * @param g
	 */
	public void plot(Chart a);
}
