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

	HEURISTIQUE(false), // affichage des orientations heuristiques
								// données par le D* Lite
	ENABLE(false), // désactive tout affichage si faux (empêche le
							// thread d'affichage de se lancer)
	D_STAR_LITE(false), // affiche les calculs du D* Lite
	D_STAR_LITE_FINAL(false), // affiche l'itinéraire final du D* Lite
	PROXIMITY_OBSTACLES(true), // affiche les obstacles de proximité
	TRAJECTORY(false), // affiche les trajectoires temporaires
	TRAJECTORY_ALL(false), // affiche TOUTES les trajectoires
									// temporaires
	TRAJECTORY_FINAL(true), // affiche les trajectoires
	FIXED_OBSTACLES(true), // affiche les obstacles fixes
	GAME_ELEMENTS(true), // affiche les éléments de jeux
	ROBOT_COLLISION(false), // affiche les obstacles du robot lors de la
									// vérification des collisions
	BACKGROUND_PATH("img/background-2017-color.png"), // affiche d'image
																// de la table
	ROBOT_PATH("img/robot_sans_roues.png"), // image du robot sans les
													// roues
	ROBOT_ROUE_GAUCHE_PATH("img/robot_roue_gauche.png"), // image de la
																	// roue
																	// gauche
	ROBOT_ROUE_DROITE_PATH("img/robot_roue_droite.png"), // image de la
																	// roue
																	// droite
	PRODUCE_GIF(false), // produit un gif ?
	GIF_FILENAME("output.gif"), // le nom du fichier du gif généré
	BACKGROUND(true), // affiche d'image de la table
	SIZE_X_WINDOW(900), // taille par défaut (sans image) de la fenêtre
	SIZE_Y_WINDOW(600), // taille par défaut (sans image) de la fenêtre
	SIZE_X_TABLE(3000),
	SIZE_Y_TABLE(2000),
	ALL_OBSTACLES(false), // affiche absolument tous les obstacles créés
	ROBOT_AND_SENSORS(true), // affiche le robot et ses capteurs
	CERCLE_ARRIVEE(false), // affiche le cercle d'arrivée
	TIME(false), // affiche le temps écoulé
	TRACE_ROBOT(true), // affiche la trace du robot
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
