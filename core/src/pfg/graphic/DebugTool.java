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
	private static DebugTool instance = null;
	
	public static DebugTool getDebugTool(SeverityCategory cat)
	{
		if(instance == null)
			instance = new DebugTool(new HashMap<ConfigInfo, Object>(), cat, "graphic.conf", "default");
		return instance;
	}
	
	public static DebugTool getExistingDebugTool()
	{
		return instance;
	}
	
	public static DebugTool getDebugTool(HashMap<ConfigInfo, Object> override, SeverityCategory cat, String configFilename, String... configprofile)
	{
		if(instance == null)
			instance = new DebugTool(override, cat, configFilename, configprofile);
		return instance;
	}
	
	private DebugTool(HashMap<ConfigInfo, Object> override, SeverityCategory cat, String configFilename, String... configprofile)
	{
		config = new Config(ConfigInfoGraphic.values(), false, configFilename, configprofile);
		config.override(override);
		injector = new Injector();
		injector.addService(config);
		Log log;
		try {
			log = new Log(cat, injector.getService(ConsoleDisplay.class));
			log.useConfig(config);
			injector.addService(log);
		} catch (InjectorException e) {
			e.printStackTrace();
		}
	}

	public void startPrintClient(String hostname)
	{
		try {
			ThreadPrintClient th = injector.getService(ThreadPrintClient.class);
			th.setHostname(hostname);
			th.start();
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
		}
	}
	
	public void startPrintServer()
	{
		try {
			injector.getService(ThreadPrintServer.class).start();
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
		}
	}
	
	public void startSaveVideo()
	{
		try {
			injector.getService(ThreadSaveVideo.class).start();;
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
		}
	}
	
	public void startAutomaticRefresh()
	{
		try {
			injector.getService(ThreadPrinting.class).start();
		} catch (InjectorException e) {
			e.printStackTrace();
			assert false : e;
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
		return injector.getExistingService(Log.class);
	}

}
