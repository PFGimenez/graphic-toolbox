/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import pfg.config.Config;
import pfg.log.Log;
import pfg.log.SeverityCategory;

/**
 * The debug tool
 * @author pf
 *
 */

public class DebugTool {

	private Config config;
	private Log log;
	private WindowFrame fenetre;
	private ThreadPrinting tp;
	private PrintBuffer buffer;
	
	public DebugTool()
	{
		this("graphic.conf");
	}
	
	public DebugTool(String configFilename)
	{
		 config = new Config(ConfigInfoGraphic.values(), configFilename, false);
	}
	
	public ThreadPrinting getThreadPrinting()
	{
		if(tp == null)
			tp = new ThreadPrinting(log, buffer, fenetre);
		return tp;
	}
	
	public WindowFrame getFenetre(Position center)
	{
		if(fenetre == null)
			fenetre = new WindowFrame(new GraphicPanel(center, config), new ConsoleDisplay(2, 10));
		return fenetre;
	}
	
	public Log getLog(SeverityCategory cat)
	{
		if(log == null)
		{
			log = new Log(cat);
			log.useConfig(config);
		}
		return log;
	}

}
