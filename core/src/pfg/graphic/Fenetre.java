/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import javax.imageio.ImageIO;
import javax.swing.*;
import pfg.config.Config;
import pfg.graphic.printable.BackgroundImage;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

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
	private Chart aff;

	private boolean afficheFond;
	private int sizeX, sizeY;
	private JFrame frame;
	private WindowExit exit;
	private boolean needInit = true;
	private double zoom;
	private Vec2RO center;
	private Vec2RO deltaBasGauche, deltaHautDroite;
	private String backgroundPath;
	private Vec2RW coinBasGaucheEcran;
	private Vec2RW coinHautDroiteEcran;
	private double sizeXUnitaryZoom, sizeYUnitaryZoom;

	public Fenetre(Position center, Config config)
	{
		super();
		buffer = new PrintBuffer();
		aff = new Chart("Debug", "Time", "Value");
		backgroundPath = config.getString(ConfigInfoGraphic.BACKGROUND_PATH);
		afficheFond = !backgroundPath.isEmpty();
		zoom = 0;
		sizeXUnitaryZoom = config.getInt(ConfigInfoGraphic.SIZE_X_WITH_UNITARY_ZOOM);
		sizeYUnitaryZoom = config.getInt(ConfigInfoGraphic.SIZE_Y_WITH_UNITARY_ZOOM);

		coinBasGaucheEcran = new Vec2RW(-sizeXUnitaryZoom / 2 + center.getX(), -sizeYUnitaryZoom / 2 + center.getY());
		coinHautDroiteEcran = new Vec2RW(sizeXUnitaryZoom / 2 + center.getX(), sizeYUnitaryZoom / 2 + center.getY());
		
		sizeX = config.getInt(ConfigInfoGraphic.SIZE_X_WINDOW);
		sizeY = config.getInt(ConfigInfoGraphic.SIZE_Y_WINDOW);
	}
	
	/**
	 * zoom de la fenêtre. Si 0, aucun zoom. Sinon, zoom + focus sur le robot
	 * @param zoom
	 */
	public void setZoom(double zoom)
	{
		if(zoom != 0)
		{
			double deltaX, deltaY;
			deltaX = sizeXUnitaryZoom / (2*zoom);
			deltaY = sizeYUnitaryZoom / (2*zoom);
			deltaBasGauche = new Vec2RO(-deltaX, -deltaY);
			deltaHautDroite = new Vec2RO(deltaX, deltaY);
		}
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
			close();
		}
		
		public synchronized void close()
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
			Vec2RO positionRobot = new Vec2RO(center.getX(), center.getY());
			Vec2RO currentCenter = positionRobot;
//			Vec2RO currentCenter = new Vec2RO((int)(positionRobot.getX() / petitDeltaX) * petitDeltaX, (int)(positionRobot.getY() / petitDeltaY) * petitDeltaY);

			currentCenter.copy(coinBasGaucheEcran);
			coinBasGaucheEcran.plus(deltaBasGauche);
			currentCenter.copy(coinHautDroiteEcran);
			coinHautDroiteEcran.plus(deltaHautDroite);
		}
		buffer.print(g, this, aff);
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
	public void waitUntilExit(long timeout) throws InterruptedException
	{
		refresh();
		synchronized(exit)
		{
			if(!needInit && !exit.alreadyExited)
			{
				System.out.println("Attente de l'arrêt de la fenêtre…");
				exit.wait(timeout);
			}
		}
	}

	public void close()
	{
		exit.close();
	}

}
