/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.io.Serializable;
import java.util.Comparator;

import pfg.graphic.printable.ColoredPrintable;

/**
 * Used by the PriorityQueue
 * @author pf
 *
 */

public class ColoredPrintableComparator implements Comparator<ColoredPrintable>, Serializable
{
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(ColoredPrintable arg0, ColoredPrintable arg1)
	{
		return arg0.l - arg1.l;
	}
}