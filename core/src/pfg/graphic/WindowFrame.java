/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * La fenêtre
 * @author pf
 *
 */

public class WindowFrame extends JFrame
{

	private class WindowExit extends WindowAdapter
	{
		private JFrame frame;
		public volatile boolean alreadyExited = false;

		public WindowExit(JFrame frame)
		{
			this.frame = frame;
		}
		
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

	private WindowExit exit;
	private GraphicPanel graphic;
	
	public WindowFrame(GraphicPanel graphic, ConsoleDisplay console)
	{
		super("Debug window");
		this.graphic = graphic;
		
		/*
		 * Fermeture de la fenêtre quand on clique sur la croix
		 */
		exit = new WindowExit(this);
		addWindowListener(exit);
		
		
		getContentPane().add(graphic);
//		getContentPane().add(console);
		pack();
		setVisible(true);
	}
	

	/**
	 * Attend que la fenêtre soit fermée
	 * 
	 * @throws InterruptedException
	 */
	public void waitUntilExit(long timeout) throws InterruptedException
	{
		synchronized(exit)
		{
			if(!exit.alreadyExited)
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

	/**
	 * Réaffiche
	 */
	public void refresh()
	{
		graphic.repaint();
	}

	public PrintBuffer getPrintBuffer()
	{
		return graphic.getPrintBuffer();
	}

}
