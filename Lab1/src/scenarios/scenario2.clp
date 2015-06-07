;scenario 2 - Mathieu


(deffacts faits
	(Personnage Marc avait une relation Neutre avec la victime)
	(Personnage Marc est Mecanicien)
	(Personnage Marc est une personne Experimente)
	(a-ete-vu (nom Marc)
     (etait-a metro-atwater ets-coop peel-pub polytechnique universite-de-montreal)
     (a-heure 09h30 12h00 16h00 19h00 21h00)
	)
	(personne-vehicule (nom Marc) (utilise-vehicule motocyclette))
	
	(Personnage Jean avait une relation Neutre avec la victime)
	(Personnage Jean est Professeur)
	(Personnage Jean est une personne Novice)
	(a-ete-vu (nom Jean)
     (etait-a universite-de-montreal ets-coop uqam ets-100genie chinatown)
     (a-heure 09h30 12h00 16h00 19h00 21h00)
	)
	(personne-vehicule (nom Jean) (utilise-vehicule marche autobus automobile))
	
	(Personnage Eric avait une relation Hostile avec la victime)
	(Personnage Eric est Cuisinier)
	(Personnage Eric est une personne Novice)
	(a-ete-vu (nom Eric)
     (etait-a metro-angrignon ets-cafeteria peel-pub hotel-de-ville metro-angrignon)
     (a-heure 09h30 12h00 16h00 19h00 21h00)
	)
	(personne-vehicule (nom Eric) (utilise-vehicule marche automobile))
	
	(Personnage Gloriette avait une relation Hostile avec la victime)
	(Personnage Gloriette est Agent_Securite)
	(Personnage Gloriette est une personne Experimente)
	(a-ete-vu (nom Gloriette)
     (etait-a gare-repentigny ets-cafeteria ets-coop ets-100genie ets-cafeteria)
     (a-heure 09h30 12h00 16h00 19h00 21h00)
	)
	(personne-vehicule (nom Gloriette) (utilise-vehicule marche automobile))
	
	(Personnage George avait une relation Hostile avec la victime)
	(Personnage George est une personne Experimente)
	(a-ete-vu (nom George)
     (etait-a ets-cafeteria ets-cafeteria polytechnique peel-pub stade-olympique)
     (a-heure 13h00 16h30 18h00 20h00 22h00)
	)
	(personne-vehicule (nom George) (utilise-vehicule automobile marche))
	
	(Personnage Marise avait une relation Amicale avec la victime)
	(Personnage Marise est une personne Novice)
	(a-ete-vu (nom Marise)
     (etait-a ets-cafeteria stade-olympique ets-cafeteria universite-de-montreal)
     (a-heure 12h45 17h00 19h00 23h00)
	)
	(personne-vehicule (nom Marise) (utilise-vehicule marche bicyclette))
	
	(Personnage Julie avait une relation Amicale avec la victime)
	(Personnage Julie est une personne Novice)
	(a-ete-vu (nom Julie)
     (etait-a ets-cafeteria ets-cafeteria ets-cafeteria uqam metro-berri-uqam)
     (a-heure 08h30 12h00 17h15 21h30 22h30)
	)
	(personne-vehicule (nom Julie) (utilise-vehicule marche autobus))
)


;Information sur le cadavre
(deffacts cadavre
	(lieu-du-crime ets-cafeteria)
	(Le corps de Tom ete decouvers a 20 heure)
	(Le corps de la victime a ete trouve dans l'etat suivant: Simple_Sur_Point_Vitaux)
	(residu-trouve nylon)
	(residu-trouve acier)
	(residu-trouve graphite)
	(trauma-vu Hemorragie)
	(trauma-vu Rupture_des_capillaires)
	(trace-trouve Marque_circulaire Localise)
	(trace-trouve Plaie_profonde)
	(trace-trouve Plaie_superficiel)
	(Le cadavre a une decoloration mauve de la peau)
	(Le cadavre a les membres inferieurs rigides)
	(La temperature de la piece est de 20 Celcius)
)
