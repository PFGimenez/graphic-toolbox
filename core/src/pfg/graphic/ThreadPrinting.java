/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>
 */

package pfg.graphic;

import pfg.config.Config;
import pfg.graphic.Fenetre;
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
	private Fenetre fenetre;
	private PrintBuffer buffer;

	public ThreadPrinting(Log log, PrintBuffer buffer, Config config)
	{
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
