/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import pfg.graphic.printable.ColoredPrintable;
import pfg.graphic.printable.Plottable;
import pfg.graphic.printable.Printable;

/**
 * Buffer de ce qu'il faut afficher
 * 
 * @author pf
 *
 */

public class PrintBuffer
{	
	
	List<Integer> layersSupprimables = new ArrayList<Integer>();
	List<Integer> layers = new ArrayList<Integer>();
	List<Plottable> plottables = new ArrayList<Plottable>();
	HashMap<Integer, List<ColoredPrintable>> elementsAffichablesSupprimables = new HashMap<Integer, List<ColoredPrintable>>();
	HashMap<Integer, List<ColoredPrintable>> elementsAffichables = new HashMap<Integer, List<ColoredPrintable>>();

	private boolean needRefresh = false;
	private TimestampedList sauvegarde;
	private WindowFrame f;

	private String filename;
	
	public PrintBuffer()
	{
		sauvegarde = new TimestampedList(System.currentTimeMillis());
		filename = "videos/" + new SimpleDateFormat("dd-MM.HH:mm").format(new Date()) + ".dat";
	}
	
	void setWindowFrame(WindowFrame f)
	{
		this.f = f;
	}
	
	/**
	 * Force the refresh
	 */
	public void refresh()
	{
		f.refresh();
	}
	
	/**
	 * Supprime tous les obstacles supprimables
	 * 
	 * @param c
	 */
	public synchronized void clearTemporaryPrintables()
	{
		elementsAffichablesSupprimables.clear();
		layersSupprimables.clear();
		notify();
		needRefresh = true;
	}

	/**
	 * Ajoute un obstacle dans la liste des supprimables
	 * 
	 * @param o
	 */
	public synchronized void addTemporaryPrintable(Printable o, Color c, int layer)
	{
		List<ColoredPrintable> l = elementsAffichablesSupprimables.get(layer);
		if(l == null)
		{
			layersSupprimables.add(layer);
			Collections.sort(layersSupprimables);
			l = new ArrayList<ColoredPrintable>();
			elementsAffichablesSupprimables.put(layer, l);
		}
		l.add(new ColoredPrintable(o, c, layer));
		notify();
		needRefresh = true;
	}

	/**
	 * Ajoute un obstacle
	 * 
	 * @param o
	 */
	public synchronized void addPrintable(Printable o, Color c, int layer)
	{
		List<ColoredPrintable> l = elementsAffichables.get(layer);
		if(l == null)
		{
			layers.add(layer);
			Collections.sort(layers);
			l = new ArrayList<ColoredPrintable>();
			elementsAffichables.put(layer, l);
		}
		l.add(new ColoredPrintable(o, c, layer));
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
	synchronized void print(Graphics g, GraphicPanel f)
	{
		needRefresh = false;
		Iterator<ColoredPrintable> iter = new PrintableIterator(this);
		while(iter.hasNext())
		{
			ColoredPrintable p = iter.next();
			p.print(g, f);
		}
	}

	synchronized void plot(Chart aff)
	{
		for(Plottable p : plottables)
			p.plot(aff);
	}
	
	/**
	 * Register a new plottable
	 * @param p
	 */
	public synchronized void addPlottable(Plottable p)
	{
		plottables.add(p);
	}

	/**
	 * Unregister a plottable
	 * @param p
	 * @return
	 */
	public synchronized boolean removePlottable(Plottable p)
	{
		return plottables.remove(p);
	}

	/**
	 * Unregister a printable
	 * @param o
	 */
	public synchronized boolean removePrintable(Printable o)
	{
		Integer l = null;
		for(Integer layer : elementsAffichablesSupprimables.keySet())
			if(elementsAffichablesSupprimables.get(layer).contains(o))
			{
				l = layer;
				break;
			}
		
		if(l != null && elementsAffichablesSupprimables.get(l).remove(o))
		{
			if(elementsAffichablesSupprimables.get(l).isEmpty())
				layersSupprimables.remove(l);
			notify();
			needRefresh = true;
			return true;
		}
		
		l = null;
		for(Integer layer : elementsAffichables.keySet())
			if(elementsAffichables.get(layer).contains(o))
			{
				l = layer;
				break;
			}
		
		if(l != null && elementsAffichables.get(l).remove(o))
		{
			if(elementsAffichables.get(l).isEmpty())
				layers.remove(l);
			notify();
			needRefresh = true;
			return true;
		}
		
		return false;
	}

	boolean needRefresh()
	{
		return needRefresh || !plottables.isEmpty(); // si on a des plottables, on refresh quand même
	}

	private synchronized List<ColoredPrintable> prepareList()
	{
		List<ColoredPrintable> o = new ArrayList<ColoredPrintable>();
		Iterator<ColoredPrintable> iter = new PrintableIterator(this);
		
		while(iter.hasNext())
			o.add(iter.next());

		return o;
	}

	/**
	 * Ajoute à la sauvegarde
	 * 
	 * @param out
	 * @throws IOException
	 */
	synchronized void saveState()
	{
		List<ColoredPrintable> o = prepareList();
		// System.out.println("Ajout de "+o.size()+" objets, date =
		// "+System.currentTimeMillis());
		sauvegarde.add(o);
	}

	/**
	 * Envoie en sérialisant les objets à afficher
	 * 
	 * @param out
	 * @throws IOException
	 */
	synchronized void send(ObjectOutputStream out) throws IOException
	{
		out.writeObject(prepareList());
		out.flush(); // on force l'envoi !
	}

	synchronized void destructor()
	{
		// On ne sauvegarde que s'il y a quelque chose à sauvegarder…
		if(!sauvegarde.isEmpty())
		{
			System.out.println("Sauvegarde de la vidéo en cours… ÇA PEUT PRENDRE DU TEMPS !");
	
			try
			{
				FileOutputStream fichier = null;
				try
				{
					fichier = new FileOutputStream(filename);
				}
				catch(FileNotFoundException e)
				{
					try
					{
						Runtime.getRuntime().exec("mkdir videos");
						try
						{
							Thread.sleep(50);
						}
						catch(InterruptedException e1)
						{
							e1.printStackTrace();
						}
						fichier = new FileOutputStream(filename);
					}
					catch(FileNotFoundException e1)
					{
						System.err.println("Erreur (1) lors de la création du fichier : " + e1);
						return;
					}
				}
	
				ObjectOutputStream file = new ObjectOutputStream(fichier);
				file.writeObject(sauvegarde);
				file.flush();
				file.close();
				Runtime.getRuntime().exec("cp "+filename+" videos/last.dat");
	
				System.out.println("Sauvegarde de la vidéo terminée");
			}
			catch(IOException e)
			{
				System.err.println("Erreur lors de la sauvegarde du buffer graphique ! " + e);
			}
		}
	}
}
