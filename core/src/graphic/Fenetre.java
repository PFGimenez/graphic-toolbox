/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package graphic;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import config.Config;
import graphic.printable.BackgroundImage;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface graphique
 * 
 * @author pf
 *
 */

public class Fenetre extends JPanel
{

	/**
	 * Couleurs surtout utilisées pour le dstarlite
	 * 
	 * @author pf
	 *
	 */

	private static final long serialVersionUID = 1L;
	private PrintBuffer buffer;

	private boolean afficheFond;
	private int sizeX, sizeY;
	private JFrame frame;
	private WindowExit exit;
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
	private boolean needInit = true;
	private double zoom;
	private FocusPoint center;
	private Vec2RO deltaBasGauche, deltaHautDroite;
	private String backgroundPath;
	private Vec2RW coinBasGaucheEcran;
	private Vec2RW coinHautDroiteEcran;


	public Fenetre(FocusPoint center)
	{
		Config config = new Config(ConfigInfoGraphic.values(), "graphic.conf", false);
		buffer = new PrintBuffer();

		backgroundPath = config.getString(ConfigInfoGraphic.BACKGROUND_PATH);
		afficheFond = !backgroundPath.isEmpty();
		zoom = config.getDouble(ConfigInfoGraphic.ZOOM);
		double x = config.getInt(ConfigInfoGraphic.SIZE_X_WITH_UNITARY_ZOOM);
		double y = config.getInt(ConfigInfoGraphic.SIZE_Y_WITH_UNITARY_ZOOM);

		if(zoom != 0)
		{
			this.center = center;
			double deltaX, deltaY;
			deltaX = x / (2*zoom);
			deltaY = y / (2*zoom);
			deltaBasGauche = new Vec2RO(-deltaX, -deltaY);
			deltaHautDroite = new Vec2RO(deltaX, deltaY);
		}
		coinBasGaucheEcran = new Vec2RW(-x, -y);
		coinHautDroiteEcran = new Vec2RW(x, y);
		
		sizeX = config.getInt(ConfigInfoGraphic.SIZE_X_WINDOW);
		sizeY = config.getInt(ConfigInfoGraphic.SIZE_Y_WINDOW);
	}
	
	public PrintBuffer getPrintBuffer()
	{
		return buffer;
	}

	private class WindowExit extends WindowAdapter
	{
		public volatile boolean alreadyExited = false;

		@Override
		public synchronized void windowClosing(WindowEvent e)
		{
			notify();
			alreadyExited = true;
			frame.dispose();
		}
	}

	/**
	 * Initialisation
	 */
	private void init()
	{
		needInit = false;
		exit = new WindowExit();
		if(afficheFond)
		{
			try
			{
				Image image = ImageIO.read(new File(backgroundPath));
				sizeX = image.getWidth(this); // on ajuste la taille de la
												// fenêtre à l'image
				sizeY = image.getHeight(this);
				buffer.add(new BackgroundImage(image));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		showOnFrame();
	}

	public int distanceXtoWindow(int dist)
	{
		return (int) (dist * sizeX / (coinHautDroiteEcran.getX() - coinBasGaucheEcran.getX()));
	}

	public int distanceYtoWindow(int dist)
	{
		return (int) (dist * sizeY / (coinHautDroiteEcran.getY() - coinBasGaucheEcran.getY()));
	}

	public int XtoWindow(double x)
	{
		return (int) ((x - coinBasGaucheEcran.getX()) * sizeX / (coinHautDroiteEcran.getX() - coinBasGaucheEcran.getX()));
	}

	public int YtoWindow(double y)
	{
		return (int) ((coinHautDroiteEcran.getY() - y) * sizeY / (coinHautDroiteEcran.getY() - coinBasGaucheEcran.getY()));
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.clearRect(0, 0, sizeX, sizeY);
		if(zoom != 0)
		{
			Vec2RO positionRobot = center.getPosition();
			Vec2RO currentCenter = positionRobot;
//			Vec2RO currentCenter = new Vec2RO((int)(positionRobot.getX() / petitDeltaX) * petitDeltaX, (int)(positionRobot.getY() / petitDeltaY) * petitDeltaY);

			currentCenter.copy(coinBasGaucheEcran);
			coinBasGaucheEcran.plus(deltaBasGauche);
			currentCenter.copy(coinHautDroiteEcran);
			coinHautDroiteEcran.plus(deltaHautDroite);
		}
		buffer.print(g, this);
	}

	/**
	 * Affiche la fenêtre
	 */
	private void showOnFrame()
	{
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(sizeX, sizeY));
		frame = new JFrame();

		/*
		 * Fermeture de la fenêtre quand on clique sur la croix
		 */
		frame.addWindowListener(exit);
		frame.getContentPane().add(this);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Réaffiche
	 */
	public void refresh()
	{
		if(needInit)
			init();
		repaint();
	}

	/**
	 * Attend que la fenêtre soit fermée
	 * 
	 * @throws InterruptedException
	 */
	public void waitUntilExit() throws InterruptedException
	{
		refresh();
		synchronized(exit)
		{
			if(!needInit && !exit.alreadyExited)
			{
				System.out.println("Attente de l'arrêt de la fenêtre…");
				exit.wait(5000);
			}
		}
	}

	/**
	 * Ajoute une image au gif final
	 */
	public void saveImage()
	{
		BufferedImage bi = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
		paint(bi.getGraphics());
		images.add(bi);
	}

	/**
	 * @param file
	 * @param delay
	 */
	public void saveGif(String file, int delay)
	{
		if(!images.isEmpty())
		{
			System.out.println("Sauvegarde du gif de " + images.size() + " images…");
			ImageOutputStream output;
			try
			{
				try
				{
					output = new FileImageOutputStream(new File(file));
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
					return;
				}
				catch(IOException e)
				{
					e.printStackTrace();
					return;
				}

				GifSequenceWriter writer;
				try
				{
					writer = new GifSequenceWriter(output, images.get(0).getType(), delay, true);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
					output.close();
					return;
				}
				catch(IOException e)
				{
					e.printStackTrace();
					output.close();
					return;
				}

				try
				{
					for(BufferedImage i : images)
						writer.writeToSequence(i);
				}
				finally
				{
					writer.close();
					output.close();
				}
				System.out.println("Sauvegarde finie !");

			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
