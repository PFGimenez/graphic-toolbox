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

/**
 * Service de log, affiche à l'écran des informations avec différents niveaux de
 * couleurs
 * 
 * @author pf
 *
 */

public class Log
{
/*	public enum Verbose
	{
		SERIE(ConfigInfo.DEBUG_SERIE, true),
		CORRECTION(ConfigInfo.DEBUG_CORRECTION, false),
		TRAME(ConfigInfo.DEBUG_SERIE_TRAME, false),
		CAPTEURS(ConfigInfo.DEBUG_CAPTEURS, true),
		ASSER(ConfigInfo.DEBUG_ASSER, true),
		REPLANIF(ConfigInfo.DEBUG_REPLANIF, true),
		SCRIPTS(ConfigInfo.DEBUG_SCRIPTS, true),
		PF(ConfigInfo.DEBUG_PF, true),
		INFO(ConfigInfoGraphic.VERBOSE_INFO, true);

		public final int masque;
		public final ConfigInfoGraphic c;
		protected boolean status;
		public boolean printInFile;

		public static final int all = (1 << (Verbose.values().length + 1)) - 1;
		private static Verbose[] values = values();

		private Verbose(ConfigInfoGraphic c, boolean printInFile)
		{
			masque = 1 << ordinal();
			this.c = c;
			this.printInFile = printInFile;
		}

		public static boolean shouldPrintInFile(int value)
		{
			if(value == all)
				return true;
			for(Verbose v : values)
				if(v.printInFile && (value & v.masque) != 0)
					return true;
			return false;
		}

		public static boolean shouldPrint(int value)
		{
			if(value == all)
				return true;
			for(Verbose v : values)
				if(v.status && (value & v.masque) != 0)
					return true;
			return false;
		}
	}
*/
/*	private enum Niveau
	{
		DEBUG(" ", "\u001B[0m", System.out),
		WARNING(" WARNING ", "\u001B[33m", System.out),
		CRITICAL(" CRITICAL ", "\u001B[31m", System.err);

		public String entete, couleur;
		public PrintStream stream;

		private Niveau(String entete, String couleur, PrintStream stream)
		{
			this.entete = entete;
			this.couleur = couleur;
			this.stream = stream;
		}
	}*/

	private boolean logClosed = false;
	private BufferedWriter writer = null;
	private String file;

	// Ecriture plus rapide sans appel à la pile d'exécution
	private boolean fastLog = false;
	private SeverityCategory defaultSeverity;

	/**
	 * date du démarrage
	 */
	private long dateInitiale = System.currentTimeMillis();
	private long dateDebutMatch = -1;

	public Log(SeverityCategory defaultSeverity)
	{
		this.defaultSeverity = defaultSeverity;
		try {
			Runtime.getRuntime().exec("rm logs/last.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public long getDateInitiale()
	{
		return dateInitiale;
	}
	
	public synchronized void write(String message, LogCategory categorie)
	{
		write(message, defaultSeverity, categorie);
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
	public synchronized void write(String message, SeverityCategory niveau, LogCategory categorie)
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
				affichage = date + tempsMatch + niveau + elem.getClassName().substring(elem.getClassName().lastIndexOf(".") + 1) + ":" + elem.getLineNumber() + " (" + Thread.currentThread().getName() + ") > " + message;
			}

			if(writer != null)
			{
				try
				{
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
