/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import java.util.HashMap;

import pfg.config.Config;
import pfg.config.ConfigInfo;
import pfg.injector.Injector;
import pfg.injector.InjectorException;
import pfg.log.Log;
import pfg.log.SeverityCategory;

/**
 * The debug tool
 * @author pf
 *
 */

public class DebugTool {

	private Config config;
	private Injector injector;
	private SeverityCategory cat;
	
	public DebugTool(SeverityCategory cat)
	{
		this("graphic.conf", "default", new HashMap<ConfigInfo, Object>(), cat);
	}
	
	public DebugTool(String configFilename, String configprofile, HashMap<ConfigInfo, Object> override, SeverityCategory cat)
	{
		this.cat = cat;
		config = new Config(ConfigInfoGraphic.values(), configFilename, configprofile, false);
		config.override(override);
		injector = new Injector();
		injector.addService(Config.class, config);
	}

	public ThreadComm getThreadComm()
	{
		try {
			return injector.getService(ThreadComm.class);
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
			return null;
		}
	}
	
	public ThreadSaveVideo getThreadSaveVideo()
	{
		try {
			return injector.getService(ThreadSaveVideo.class);
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
			return null;
		}
	}
	
	public ThreadPrinting getThreadPrinting()
	{
		try {
			return injector.getService(ThreadPrinting.class);
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
			return null;
		}
	}
	
	public WindowFrame getWindowFrame(Position center)
	{
		WindowFrame fenetre;
		try {
			fenetre = injector.getExistingService(WindowFrame.class);
			if(fenetre == null)
			{
				GraphicPanel g = injector.getExistingService(GraphicPanel.class);
				if(g == null)
				{
					g = new GraphicPanel(center, config);
					injector.addService(GraphicPanel.class, g);
					fenetre = injector.getService(WindowFrame.class);
				}
			}
			injector.addService(WindowFrame.class, fenetre);
			return fenetre;
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
			return null;
		}
	}
	
	public Log getLog()
	{
		Log log;
		try {
			log = injector.getExistingService(Log.class);
			if(log == null)
				log = new Log(cat, injector.getService(ConsoleDisplay.class));
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
			return null;
		}
		injector.addService(Log.class, log);
		return log;
	}

}
