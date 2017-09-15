/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

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
	HashMap<Integer, List<Printable>> elementsAffichablesSupprimables = new HashMap<Integer, List<Printable>>();
	HashMap<Integer, List<Printable>> elementsAffichables = new HashMap<Integer, List<Printable>>();

	private boolean needRefresh = false;
	private TimestampedList sauvegarde;

	private String filename;
	
	public PrintBuffer()
	{
		sauvegarde = new TimestampedList(System.currentTimeMillis());
		filename = "videos/" + new SimpleDateFormat("dd-MM.HH:mm").format(new Date()) + ".dat";
	}
	
	/**
	 * Supprime tous les obstacles supprimables
	 * 
	 * @param c
	 */
	public synchronized void clearSupprimables()
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
	public synchronized void addSupprimable(Printable o)
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
		notify();
		needRefresh = true;
	}

	/**
	 * Ajoute un obstacle
	 * 
	 * @param o
	 */
	public synchronized void add(Printable o)
	{
		List<Printable> l = elementsAffichables.get(o.getLayer());
		if(l == null)
		{
			layers.add(o.getLayer());
			Collections.sort(layers);
			l = new ArrayList<Printable>();
			elementsAffichables.put(o.getLayer(), l);
		}
		l.add(o);
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
	synchronized void print(Graphics g, GraphicPanel f, Chart a)
	{
		needRefresh = false;
		Iterator<Printable> iter = new PrintableIterator(this);
		while(iter.hasNext())
		{
			Printable p = iter.next();
			p.print(g, f, a);
		}
	}

	/**
	 * Supprime un printable ajouté à la liste des supprimables
	 * Ce n'est pas grave s'il y a une double suppression
	 * 
	 * @param o
	 */
	public synchronized boolean removeSupprimable(Printable o)
	{
		if(elementsAffichablesSupprimables.get(o.getLayer()).remove(o))
		{
			if(elementsAffichablesSupprimables.get(o.getLayer()).isEmpty())
				layersSupprimables.remove(o.getLayer());
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

	private synchronized List<Printable> prepareList()
	{
		List<Printable> o = new ArrayList<Printable>();
		Iterator<Printable> iter = new PrintableIterator(this);
		
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
	public synchronized void saveState()
	{
		List<Printable> o = prepareList();
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
	public synchronized void send(ObjectOutputStream out) throws IOException
	{
		out.writeObject(prepareList());
		out.flush(); // on force l'envoi !
	}

	public synchronized void destructor()
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
