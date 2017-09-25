/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import pfg.log.Log;

/**
 * S'occupe d'enregistrer la vidéo
 * 
 * @author pf
 *
 */

public class ThreadSaveVideo extends Thread
{
	protected Log log;
	private GraphicDisplay buffer;

	public ThreadSaveVideo(Log log, GraphicDisplay buffer)
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
				Thread.sleep(100);
				synchronized(buffer)
				{
					buffer.wait(400);
					buffer.saveState();
				}
			}
		}
		catch(InterruptedException e)
		{
			buffer.destructor();
//			log.write("Arrêt de " + Thread.currentThread().getName(), Subject.DUMMY);
			Thread.currentThread().interrupt();
		}
	}

}