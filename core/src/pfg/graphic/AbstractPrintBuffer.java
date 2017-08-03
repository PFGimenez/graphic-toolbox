/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import pfg.graphic.printable.Printable;

/**
 * Une interface qui permet de regrouper le print buffer déporté et celui qui ne
 * l'est pas
 * 
 * @author pf
 *
 */

public abstract class AbstractPrintBuffer
{
	List<Integer> layersSupprimables = new ArrayList<Integer>();
	List<Integer> layers = new ArrayList<Integer>();
	HashMap<Integer, List<Printable>> elementsAffichablesSupprimables = new HashMap<Integer, List<Printable>>();
	HashMap<Integer, List<Printable>> elementsAffichables = new HashMap<Integer, List<Printable>>();

	public synchronized void clearSupprimables()
	{
		elementsAffichablesSupprimables.clear();
		layersSupprimables.clear();
	}

	public void addSupprimable(Printable o)
	{
		List<Printable> l = elementsAffichablesSupprimables.get(o.getLayer());
		if(l == null)
		{
			layersSupprimables.add(o.getLayer());
			Collections.sort(layersSupprimables);
			l = new ArrayList<Printable>();
			elementsAffichablesSupprimables.put(o.getLayer(), l);
		}
		l.add(o);
	}

	public void add(Printable o)
	{
		List<Printable> l = elementsAffichables.get(o.getLayer());
		if(l == null)
		{
			System.out.println("Ajout de "+o+", layer "+o.getLayer());
			layers.add(o.getLayer());
			Collections.sort(layers);
			l = new ArrayList<Printable>();
			elementsAffichables.put(o.getLayer(), l);
		}
		l.add(o);
	}

	public boolean removeSupprimable(Printable o)
	{
		layersSupprimables.clear();
		return elementsAffichablesSupprimables.get(o.getLayer()).remove(o);
	}

	public abstract void destructor();
}
