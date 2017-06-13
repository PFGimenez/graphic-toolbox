/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package graphic;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import graphic.printable.Printable;

/**
 * PrintBuffer prévu pour l'externalisation de l'affichage
 * 
 * @author pf
 *
 */

public class ExternalPrintBuffer extends AbstractPrintBuffer
{
	private TimestampedList sauvegarde;

	private ObjectOutputStream file;
	private String filename;

	public ExternalPrintBuffer()
	{
		sauvegarde = new TimestampedList(System.currentTimeMillis());
		filename = "videos/" + new SimpleDateFormat("dd-MM.HH:mm").format(new Date()) + ".dat";
	}

	/**
	 * Supprime tous les obstacles supprimables
	 * 
	 * @param c
	 */
	@Override
	public synchronized void clearSupprimables()
	{
		super.clearSupprimables();
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
			return true;
		}
		return false;
	}

	private synchronized List<Serializable> prepareList()
	{
		List<Serializable> o = new ArrayList<Serializable>();
		Iterator<Printable> iter = new PrintableIterator(this);
		
		while(iter.hasNext())
			o.add(iter.next());

		return o;
	}

	/**
	 * Envoie en sérialisant les objets à afficher
	 * 
	 * @param out
	 * @throws IOException
	 */
	public synchronized void write() throws IOException
	{
		List<Serializable> o = prepareList();
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

	@Override
	public synchronized void destructor()
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
			file = new ObjectOutputStream(fichier);
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
