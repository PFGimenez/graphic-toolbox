/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.awt.Graphics;
import java.util.Iterator;

import pfg.graphic.printable.Printable;

/**
 * Buffer de ce qu'il faut afficher
 * 
 * @author pf
 *
 */

public class PrintBuffer extends AbstractPrintBuffer
{	
	private boolean needRefresh = false;

	/**
	 * Supprime tous les obstacles supprimables
	 * 
	 * @param c
	 */
	@Override
	public synchronized void clearSupprimables()
	{
		super.clearSupprimables();
		notify();
		needRefresh = true;
	}

	/**
	 * Ajoute un obstacle dans la liste des supprimables
	 * 
	 * @param o
	 */
	@Override
	public synchronized void addSupprimable(Printable o)
	{
		super.addSupprimable(o);
		notify();
		needRefresh = true;
	}

	/**
	 * Ajoute un obstacle
	 * 
	 * @param o
	 */
	@Override
	public synchronized void add(Printable o)
	{
		super.add(o);
		notify();
		needRefresh = true;
	}

	/**
	 * Affiche tout
	 * 
	 * @param g
	 * @param f
	 * @param robot
	 */
	synchronized void print(Graphics g, Fenetre f)
	{
		needRefresh = false;
		Iterator<Printable> iter = new PrintableIterator(this);
		System.out.println("Affichage !");
		while(iter.hasNext())
		{
			Printable p = iter.next();
			System.out.println("Affichage de "+p);
			p.print(g, f);
		}
	}

	/**
	 * Supprime un printable ajouté à la liste des supprimables
	 * Ce n'est pas grave s'il y a une double suppression
	 * 
	 * @param o
	 */
	@Override
	public synchronized boolean removeSupprimable(Printable o)
	{
		if(super.removeSupprimable(o))
		{
			notify();
			needRefresh = true;
			return true;
		}
		return false;
	}

	public boolean needRefresh()
	{
		return needRefresh;
	}

	@Override
	public void destructor()
	{}

}
