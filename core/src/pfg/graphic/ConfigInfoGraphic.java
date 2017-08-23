/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package pfg.graphic;

import pfg.config.ConfigInfo;

/**
 * Informations accessibles par la config
 * Les informations de config.ini surchargent celles-ci
 * Certaines valeurs sont constantes, ce qui signifie qu'elles ne peuvent être
 * modifiées dynamiquement au cours d'un match.
 * Chaque variable a une valeur par défaut, afin de pouvoir lancer le programme
 * sans config.ini.
 * 
 * @author pf
 *
 */

public enum ConfigInfoGraphic implements ConfigInfo
{
	FAST_LOG(false), // log rapide, sans reflection
	STDOUT_LOG(false), // log into the stdout
	
	CONSOLE_NB_ROWS(10), // nombre de lignes dans la console affichée
	CONSOLE_NB_COLUMNS(30), // nombre de colonnes dans la console affichée
	
	BACKGROUND_PATH(""), // background path ; empty if none	
	GRAPHIC_SERVER_PORT_NUMBER(13370), // port number of the graphic server
	SIZE_X_WINDOW(900), // taille par défaut (sans image) de la fenêtre
	SIZE_Y_WINDOW(600), // taille par défaut (sans image) de la fenêtre
	SIZE_X_WITH_UNITARY_ZOOM(50), // taille en mm de la zone à afficher (sur l'axe X)
	SIZE_Y_WITH_UNITARY_ZOOM(50); // taille en mm de la zone à afficher (sur l'axe Y)

	private Object defaultValue;
	public boolean overridden = false;
	public volatile boolean uptodate;
	public final boolean constant;

	/**
	 * Par défaut, une valeur est constante
	 * 
	 * @param defaultValue
	 */
	private ConfigInfoGraphic(Object defaultValue)
	{
		this(defaultValue, true);
	}

	private ConfigInfoGraphic(Object defaultValue, boolean constant)
	{
		uptodate = constant;
		this.defaultValue = defaultValue;
		this.constant = constant;
	}

	public Object getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * Pour les modifications de config avant même de démarrer le service de
	 * config
	 * 
	 * @param o
	 */
	public void setDefaultValue(Object o)
	{
		defaultValue = o;
		overridden = true;
	}

	@Override
	public boolean isMutable()
	{
		return !overridden;
	}

}
