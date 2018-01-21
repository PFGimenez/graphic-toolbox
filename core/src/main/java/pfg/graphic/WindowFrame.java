/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import pfg.config.Config;
import pfg.injector.Injector;

/**
 * La fenêtre
 * @author pf
 *
 */

public class WindowFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	private class WindowExit extends WindowAdapter
	{
		private JFrame frame;
		private Injector injector;
		public volatile boolean alreadyExited = false;

		public WindowExit(JFrame frame, Injector injector)
		{
			this.injector = injector;
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
			if(injector.getExistingService(ThreadRefresh.class) != null)
				injector.getExistingService(ThreadRefresh.class).interrupt();
			if(injector.getExistingService(ThreadPrintClient.class) != null)
				injector.getExistingService(ThreadPrintClient.class).interrupt();
			if(injector.getExistingService(ThreadPrintServer.class) != null)
				injector.getExistingService(ThreadPrintServer.class).interrupt();
			if(injector.getExistingService(ThreadSaveVideo.class) != null)
				injector.getExistingService(ThreadSaveVideo.class).interrupt();
		}
	}

	private WindowExit exit;
	private GraphicPanel graphic;
	private Container contentPane;
//	private ConsoleDisplay console;
	
	public WindowFrame(Config config, GraphicPanel graphic, ConsoleDisplay console, Injector injector)
	{
		super("Debug window");
		this.graphic = graphic;
		graphic.getPrintBuffer().setWindowFrame(this);
//		this.console = console;
		contentPane = getContentPane();
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		/*
		 * Fermeture de la fenêtre quand on clique sur la croix
		 */
		exit = new WindowExit(this, injector);
		addWindowListener(exit);
		
		contentPane.add(graphic);
		if(config.getBoolean(ConfigInfoGraphic.ENABLE_CONSOLE))
			contentPane.add(new JScrollPane(console), BorderLayout.CENTER);

		setBackground(Color.WHITE);
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

	public GraphicDisplay getPrintBuffer()
	{
		return graphic.getPrintBuffer();
	}

}