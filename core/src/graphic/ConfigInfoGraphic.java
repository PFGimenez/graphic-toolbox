/*
 * Copyright (C) 2013-2017 Pierre-François Gimenez
 * Distributed under the MIT License.
 */

package graphic;

import config.ConfigInfo;

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

	BACKGROUND_PATH("img/background-2017-color.png"), // affiche d'image
																// de la table
	PRODUCE_GIF(false), // produit un gif ?
	GIF_FILENAME("output.gif"), // le nom du fichier du gif généré
	BACKGROUND(true), // affiche d'image de la table
	SIZE_X_WINDOW(900), // taille par défaut (sans image) de la fenêtre
	SIZE_Y_WINDOW(600), // taille par défaut (sans image) de la fenêtre
	SIZE_X_TABLE(3000),
	SIZE_Y_TABLE(2000),
	ALL_OBSTACLES(false), // affiche absolument tous les obstacles créés
	TIME(false), // affiche le temps écoulé
	EXTERNAL(true), // l'affichage doit-il être déporté par le serveur
							// d'affichage ?
	DIFFERENTIAL(true), // sauvegarde d'une "vidéo" pour visionner les
								// images plus tard
	ZOOM(0); // zoom de la fenêtre. Si 0, aucun zoom. Sinon, zoom + focus sur le robot

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