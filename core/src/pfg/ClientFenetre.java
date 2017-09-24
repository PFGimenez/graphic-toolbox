/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import pfg.config.Config;
import pfg.graphic.ConfigInfoGraphic;
import pfg.graphic.DebugTool;
import pfg.graphic.GraphicPanel;
import pfg.graphic.PrintBuffer;
import pfg.graphic.Vec2RO;
import pfg.graphic.printable.ColoredPrintable;
import pfg.graphic.printable.Printable;

/**
 * Le client du serveur de fenêtre. Lit les infos depuis un socket et les
 * affiche
 * 
 * @author pf
 *
 */

public class ClientFenetre
{

	public static void main(String[] args) throws InterruptedException
	{
		GraphicPanel f = null;//new DebugTool().getFenetre(new Vec2RO(0, 1000));
		PrintBuffer buffer = null;//f.getPrintBuffer();
		InetAddress rpiAdresse = null;
		Config config = new Config(ConfigInfoGraphic.values(), false, "graphic.conf", "default");
		boolean loop = false;
		System.out.println("Démarrage du client d'affichage");
		try
		{
			if(args.length != 0)
			{
				for(int i = 0; i < args.length; i++)
				{
					if(args[i].equals("-d"))
						loop = true;
					else if(!args[i].startsWith("-"))
					{
						String[] s = args[i].split("\\."); // on découpe
															// avec les
															// points
						if(s.length == 4) // une adresse ip,
											// probablement
						{
							System.out.println("Recherche du serveur à partir de son adresse ip : " + args[i]);
							byte[] addr = new byte[4];
							for(int j = 0; j < 4; j++)
								addr[j] = Byte.parseByte(s[j]);
							rpiAdresse = InetAddress.getByAddress(addr);
						}
						else // le nom du serveur, probablement
						{
							System.out.println("Recherche du serveur à partir de son nom : " + args[i]);
							rpiAdresse = InetAddress.getByName(args[i]);
						}
					}
					else
						System.err.println("Paramètre inconnu : " + args[i]);
				}
			}

			if(rpiAdresse == null) // par défaut, la raspi (ip fixe)
			{
				rpiAdresse = InetAddress.getByAddress(new byte[] { (byte) 172, 24, 1, 1 });
				System.out.println("Utilisation de l'adresse par défaut : " + rpiAdresse);
			}
		}
		catch(UnknownHostException e)
		{
			System.err.println("La recherche du serveur a échoué ! " + e);
			return;
		}

		Socket socket = null;
		do
		{

			boolean ko;
			System.out.println("Tentative de connexion…");

			do
			{
				try
				{
					socket = new Socket(rpiAdresse, config.getInt(ConfigInfoGraphic.GRAPHIC_SERVER_PORT_NUMBER));
					socket.setTcpNoDelay(true);
					ko = false;
				}
				catch(IOException e)
				{
					Thread.sleep(500); // on attend un peu avant de
										// réessayer
					ko = true;
				}
			} while(ko);

			System.out.println("Connexion réussie !");
			ObjectInputStream in;
			try
			{
				in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			}
			catch(IOException e)
			{
				System.err.println("Le serveur a coupé la connexion : " + e);
				continue; // on relance la recherche
			}

			try
			{
				while(true)
				{
					@SuppressWarnings("unchecked")
					List<Object> tab = (List<Object>) in.readObject();
					synchronized(buffer)
					{
						buffer.clearTemporaryPrintables();
						int i = 0;
						while(i < tab.size())
						{
							Object o = tab.get(i++);
/*							if(o instanceof Cinematique)
							{
								// System.out.println("Cinématique !
								// "+((Cinematique)o).getPosition());
								robot.setCinematique((Cinematique) o);
							}
							else */if(o instanceof ColoredPrintable)
							{
//								Layer l = (Layer) tab.get(i++);
								buffer.addTemporaryPrintable(((ColoredPrintable) o).p, ((ColoredPrintable) o).c, ((ColoredPrintable) o).l);
							}
							else
								System.err.println("Erreur ! Objet non affichable : " + o.getClass());
						}
					}
				}
			}
			catch(IOException e)
			{
				System.err.println("Le serveur a coupé la connexion : " + e);
				e.printStackTrace();
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					in.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}

		} while(loop);

		try
		{
			socket.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
//		if(f != null)
//			f.waitUntilExit(0);
		System.out.println("Arrêt du client d'affichage");

	}

}
