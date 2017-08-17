/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.io.IOException;

import pfg.log.Log;

/**
 * S'occupe d'enregistrer la vidéo
 * 
 * @author pf
 *
 */

public class ThreadSaveVideo implements Runnable
{
	protected Log log;
	private PrintBuffer buffer;

	public ThreadSaveVideo(Log log, PrintBuffer buffer)
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
					buffer.write();
				}
			}
		}
		catch(InterruptedException | IOException e)
		{
			buffer.destructor();
//			log.write("Arrêt de " + Thread.currentThread().getName(), Subject.DUMMY);
			Thread.currentThread().interrupt();
		}
	}

}