/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package graphic;

import java.util.Iterator;
import graphic.printable.Printable;

/**
 * An iterotar on the printable
 * @author pf
 *
 */

public class PrintableIterator implements Iterator<Printable>
{
	private AbstractPrintBuffer buffer;
	private int i = -1;
	private Iterator<Printable> current = null;
	
	PrintableIterator(AbstractPrintBuffer buffer)
	{
		this.buffer = buffer;
	}
	
	@Override
	public boolean hasNext()
	{
		if(current != null && current.hasNext())
			return true;

		i = buffer.getNextLayer(i);
		current = buffer.getNextIterator(i);
		return current != null;
	}

	@Override
	public Printable next()
	{
		return current.next();
	}

}
