/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 */

package graphic;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import config.Config;
import graphic.printable.Couleur;
import graphic.printable.Layer;
import graphic.printable.Printable;

/**
 * Buffer de ce qu'il faut afficher
 * 
 * @author pf
 *
 */

public class PrintBuffer implements PrintBufferInterface
{
	private List<ArrayList<Printable>> elementsAffichablesSupprimables = new ArrayList<ArrayList<Printable>>();
	private List<ArrayList<Printable>> elementsAffichables = new ArrayList<ArrayList<Printable>>();

	private boolean afficheFond;
	private boolean needRefresh = false;
	private boolean time;
	private long initTime = System.currentTimeMillis();

	public PrintBuffer(Config config)
	{
		for(int i = 0; i < Layer.values().length; i++)
		{
			elementsAffichablesSupprimables.add(new ArrayList<Printable>());
			elementsAffichables.add(new ArrayList<Printable>());
		}
		afficheFond = config.getBoolean(ConfigInfoGraphic.BACKGROUND);
		time = config.getBoolean(ConfigInfoGraphic.TIME);
	}

	/**
	 * Supprime tous les obstacles supprimables
	 * 
	 * @param c
	 */
	@Override
	public synchronized void clearSupprimables()
	{
		for(int i = 0; i < Layer.values().length; i++)
			elementsAffichablesSupprimables.get(i).clear();
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
		elementsAffichablesSupprimables.get(o.getLayer().ordinal()).add(o);
		notify();
		needRefresh = true;
	}

	/**
	 * Ajoute un obstacle dans la liste des supprimables
	 * 
	 * @param o
	 */
	@Override
	public synchronized void addSupprimable(Printable o, Layer l)
	{
		elementsAffichablesSupprimables.get(l.ordinal()).add(o);
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
		elementsAffichables.get(o.getLayer().ordinal()).add(o);
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
	public synchronized void print(Graphics g, Fenetre f)
	{
		needRefresh = false;
		for(int i = 0; i < Layer.values().length; i++)
		{
			if(afficheFond)
				g.setColor(Couleur.VERT.couleur);
			else
				g.setColor(Couleur.NOIR.couleur);

			for(Printable p : elementsAffichablesSupprimables.get(i))
				p.print(g, f);

			if(afficheFond)
				g.setColor(Couleur.VERT.couleur);
			else
				g.setColor(Couleur.NOIR.couleur);

			for(Printable p : elementsAffichables.get(i))
				p.print(g, f);
		}
		if(time)
		{
			g.setFont(new Font("Courier New", 1, 36));
			g.setColor(Couleur.NOIR.couleur);
			g.drawString("Date : " + Long.toString(System.currentTimeMillis() - initTime), f.XtoWindow(600), f.YtoWindow(1900));
		}
	}

	/**
	 * Supprime un printable ajouté à la liste des supprimables
	 * Ce n'est pas grave s'il y a une double suppression
	 * 
	 * @param o
	 */
	@Override
	public synchronized void removeSupprimable(Printable o)
	{
		if(elementsAffichablesSupprimables.get(o.getLayer().ordinal()).remove(o))
		{
			notify();
			needRefresh = true;
		}
	}

	public boolean needRefresh()
	{
		return needRefresh;
	}

	@Override
	public void destructor()
	{}

}
