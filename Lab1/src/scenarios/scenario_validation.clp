;scenario de test (validation)

; Enumeration des faits.
(deffacts personnages
	(Personnage Marc avait une relation Neutre avec la victime)
	(Personnage Bob avait une relation Hostile avec la victime)
  
	(Personnage Marc est Cuisinier)
	(Personnage Marc est une personne Experimente)
  
	(Personnage Bob est Mecanicien)
	(Personnage Bob est une personne Inexperimente)
)


; extrait du cadavre
(deffacts situation-cadavre
	;observations results
	(lieu-du-crime gare-centrale)
	(Le corps de Francois ete decouvers a 20 heure)
	(Le corps de la victime a ete trouve dans l'etat suivant: Simple_Sur_Point_Vitaux)
	(La temperature de la piece est de 22 Celcius)
	
	;autopsie results
	(Le cadavre a une decoloration jaune de la peau)
	(Le cadavre a les membres superieurs rigides)
	(residu-trouve fer)
	(residu-trouve nylon)
	(trauma-vu Traumatism_cranien)
	(trace-trouve Cylindrique)
)

(deffacts temoins
  ; On doit lire: Bob etait a l'ets a 9h00, puis a la gare Repentigny a 11h30, puis a l'ets a 15h00
  (a-ete-vu (nom Bob) 
     (etait-a   ets-100genie gare-repentigny ets-cafeteria) 
     (a-heure   9h00         11h45           15h00))
  (a-ete-vu (nom Marc)
     (etait-a universite-de-montreal gare-centrale ets-cafeteria) 
     (a-heure 9h00                   11h30         14h00))
)

(deffacts vehicules
  (personne-vehicule (nom Bob) (utilise-vehicule marche autobus))
  (personne-vehicule (nom Marc) (utilise-vehicule marche motocyclette))
)
