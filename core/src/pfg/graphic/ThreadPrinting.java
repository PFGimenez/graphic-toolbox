/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import pfg.graphic.PrintBuffer;
import pfg.log.Log;

/**
 * S'occupe de la mise à jour graphique
 * 
 * @author pf
 *
 */

public class ThreadPrinting extends Thread
{

	protected Log log;
	private WindowFrame fenetre;
	private PrintBuffer buffer;

	public ThreadPrinting(Log log, PrintBuffer buffer, WindowFrame fenetre)
	{
		this.fenetre = fenetre;
		this.log = log;
		this.buffer = buffer;
	}

	@Override
	public void run()
	{
		Thread.currentThread().setName(getClass().getSimpleName());
//		log.write("Démarrage de " + Thread.currentThread().getName(), Subject.DUMMY);
		try
		{
			while(true)
			{
				synchronized(buffer)
				{
					if(!buffer.needRefresh())
						buffer.wait();
				}
				fenetre.refresh();
				Thread.sleep(50); // on ne met pas à jour plus souvent que
									// toutes les 50ms
			}
		}
		catch(InterruptedException e)
		{
//			log.write("Arrêt de " + Thread.currentThread().getName(), Subject.DUMMY);
			Thread.currentThread().interrupt();
		}
		catch(Exception e)
		{
//			log.write("Arrêt inattendu de " + Thread.currentThread().getName() + " : " + e, Subject.DUMMY);
			e.printStackTrace();
			e.printStackTrace(log.getPrintWriter());
			Thread.currentThread().interrupt();
		}
	}

}
