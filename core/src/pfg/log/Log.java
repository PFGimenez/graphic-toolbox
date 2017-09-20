/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.log;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import pfg.config.Config;
import pfg.graphic.ConfigInfoGraphic;
import pfg.graphic.ConsoleDisplay;

/**
 * Service de log, affiche à l'écran des informations avec différents niveaux de
 * couleurs
 * 
 * @author pf
 *
 */

public class Log
{
	private boolean logClosed = false;
	private BufferedWriter writer = null;
	private String file;

	// Ecriture plus rapide sans appel à la pile d'exécution
	private boolean fastLog = false;
	private boolean stdoutLog;
	private SeverityCategory defaultSeverity;
	private ConsoleDisplay console;

	/**
	 * date du démarrage
	 */
	private static final long dateInitiale = System.currentTimeMillis();
	private long dateDebutMatch = -1;

	public Log(SeverityCategory defaultSeverity, ConsoleDisplay console)
	{
		this.defaultSeverity = defaultSeverity;
		this.console = console;
		try {
			Runtime.getRuntime().exec("rm logs/last.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void write(String message, LogCategory categorie)
	{
		write_(message, defaultSeverity, categorie);
	}

	public synchronized void write(Object message, LogCategory categorie)
	{
		write_(message == null ? "null" : message.toString(), defaultSeverity, categorie);
	}

	public synchronized void write(Object message, SeverityCategory niveau, LogCategory categorie)
	{
		write_(message == null ? "null" : message.toString(), niveau, categorie);
	}
	
	public synchronized void write(String message, SeverityCategory niveau, LogCategory categorie)
	{
		write_(message, niveau, categorie);
	}
	
	/**
	 * Ce synchronized peut ralentir le programme, mais s'assure que les logs ne
	 * se chevauchent pas.
	 * 
	 * @param niveau
	 * @param message
	 * @param couleur
	 * @param ou
	 */
	private synchronized void write_(String message, SeverityCategory niveau, LogCategory categorie)
	{
		if(logClosed)
			System.out.println("WARNING * Log fermé! Message: " + message);
		else
		{
			long date = System.currentTimeMillis() - dateInitiale;
			String tempsMatch = "";
			if(dateDebutMatch != -1)
				tempsMatch = " T+" + (System.currentTimeMillis() - dateDebutMatch);

			String affichage;
			if(fastLog)
				affichage = date + tempsMatch + " > " + message;
			else
			{
				StackTraceElement elem = Thread.currentThread().getStackTrace()[3];
				affichage = date + tempsMatch + " "+ niveau + " " + elem.getClassName().substring(elem.getClassName().lastIndexOf(".") + 1) + ":" + elem.getLineNumber() + " (" + Thread.currentThread().getName() + ") > " + message;
			}
			console.write(affichage + "\n");

			if(writer != null)
			{
				try
				{
					if(stdoutLog)
						System.out.println(affichage);
					writer.write(categorie.getMask() + " " + affichage + "\n");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sorte de destructeur, dans lequel le fichier est sauvegardé.
	 */
	public void close()
	{
		try
		{
			if(writer != null)
			{
				writer.flush();
				writer.close();
			}
			Runtime.getRuntime().exec("cp "+file+" logs/last.txt");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		logClosed = true;
	}

	public void useConfig(Config config)
	{
		fastLog = config.getBoolean(ConfigInfoGraphic.FAST_LOG);
		stdoutLog = config.getBoolean(ConfigInfoGraphic.STDOUT_LOG);
		
		file = "logs/" + new SimpleDateFormat("dd-MM.HH:mm").format(new Date()) + ".txt";
		try
		{
			writer = new BufferedWriter(new FileWriter(file));
		}
		catch(FileNotFoundException e)
		{
			try
			{
				Runtime.getRuntime().exec("mkdir logs");
				try
				{
					Thread.sleep(50);
				}
				catch(InterruptedException e1)
				{
					e1.printStackTrace();
				}
				writer = new BufferedWriter(new FileWriter(file));
			}
			catch(IOException e1)
			{
				System.err.println("Erreur (1) lors de la création du fichier : " + e1);
			}
		}
		catch(IOException e)
		{
			System.err.println("Erreur (2) lors de la création du fichier : " + e);
		}
	}
	
	public void setInitTime(long date)
	{
		dateDebutMatch = date;
	}

	public PrintWriter getPrintWriter()
	{
		return new PrintWriter(writer);
	}

}