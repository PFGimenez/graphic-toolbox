/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.util.Iterator;
import pfg.graphic.printable.ColoredPrintable;

/**
 * An iterotar on the printable
 * @author pf
 *
 */

public class PrintableIterator implements Iterator<ColoredPrintable>
{
	private PrintBuffer buffer;
	private int indexObs = 0, indexObsSupp = 0;
	private Iterator<ColoredPrintable> current = null;
	
	PrintableIterator(PrintBuffer buffer)
	{
		this.buffer = buffer;
	}
	
	@Override
	public boolean hasNext()
	{
		if(current != null && current.hasNext())
			return true;

		if(indexObsSupp == buffer.layersSupprimables.size() && indexObs == buffer.layers.size())
			return false;
		
		if(indexObsSupp == buffer.layersSupprimables.size())
		{
			current = buffer.elementsAffichables.get(buffer.layers.get(indexObs)).iterator();
			indexObs++;
		}
		else if(indexObs == buffer.layers.size())
		{
			current = buffer.elementsAffichablesSupprimables.get(buffer.layersSupprimables.get(indexObsSupp)).iterator();
			indexObsSupp++;
		}
		else if(buffer.layers.get(indexObs) <= buffer.layersSupprimables.get(indexObsSupp))
		{
			current = buffer.elementsAffichables.get(buffer.layers.get(indexObs)).iterator();
			indexObs++;
		}
		else
		{
			current = buffer.elementsAffichablesSupprimables.get(buffer.layersSupprimables.get(indexObsSupp)).iterator();
			indexObsSupp++;
		}
		
		return true;
	}

	@Override
	public ColoredPrintable next()
	{
		return current.next();
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}

}
