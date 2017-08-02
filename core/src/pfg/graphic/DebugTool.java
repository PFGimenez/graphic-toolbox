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
	private Fenetre fenetre;
	
	public DebugTool()
	{
		 config = new Config(ConfigInfoGraphic.values(), "graphic.conf", false);
	}
	
	public Fenetre getFenetre(FocusPoint center)
	{
		if(fenetre == null)
			fenetre = new Fenetre(center, config);
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
