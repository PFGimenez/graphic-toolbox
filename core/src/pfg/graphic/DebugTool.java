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
		this("graphic.conf");
	}
	
	public DebugTool(String configFilename)
	{
		 config = new Config(ConfigInfoGraphic.values(), configFilename, false);
	}
	
	public Fenetre getFenetre(Position center)
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
