;scenario 4 - Steven

; Enumeration des faits.
(deffacts personnages
	(Personnage Paul avait une relation Amicale avec la victime)
	(Personnage Paul est Cuisinier)
	(Personnage Paul est une personne Experimente)
  
	(Personnage Frank est Professeur)
	(Personnage Frank avait une relation Neutre avec la victime)
	(Personnage Frank est une personne Inexperimente)
	
	(Personnage Benoit est Mecanicien)
	(Personnage Benoit avait une relation Amicale avec la victime)
	(Personnage Benoit est une personne Inexperimente)
	
	(Personnage Pierre est Professeur)
	(Personnage Pierre avait une relation Hostile avec la victime)
	(Personnage Pierre est une personne Novice)
	
	(Personnage Levis est Professeur)
	(Personnage Levis avait une relation Hostile avec la victime)
	(Personnage Levis est une personne Experimente)
	
	(Personnage Cardinal est Agent_Securite)
	(Personnage Cardinal avait une relation Neutre avec la victime)
	(Personnage Cardinal est une personne Experimente)
)


; extrait du cadavre
(deffacts situation-cadavre
	;observations results
	(lieu-du-crime metro-atwater)
	(Le corps de Jean ete decouvers a 22 heure)
	(Le corps de la victime a ete trouve dans l'etat suivant: Multitude_De_Coups)
	(La temperature de la piece est de 32 Celcius)
	
	;autopsie results
	(Le cadavre a une decoloration mauve de la peau)
	(Le cadavre a les membres inferieurs rigides)
	(residu-trouve rouille)
	(residu-trouve fer)
	(residu-trouve graphite)
	(trauma-vu Hematome)
	(trauma-vu Plaie_de_l'artere)
	(trauma-vu Traumatisme_thoracique)
	(trace-trouve Perforation)
	(trace-trouve Perforation)
	(trace-trouve Point_d'entre)
)

(deffacts temoins
  ; On doit lire: Bob etait a l'ets a 9h00, puis a la gare Repentigny a 11h30, puis a l'ets a 15h00
  (a-ete-vu (nom Paul) 
     (etait-a metro-angrignon gare-centrale ets-cafeteria) 
     (a-heure   8h00         9h45           11h00))
  (a-ete-vu (nom Frank)
     (etait-a metro-berri-uqam metro-berri-uqam ets-cafeteria) 
     (a-heure 10h00                   11h30         14h00))   
  (a-ete-vu (nom Benoit) 
     (etait-a ets-100genie ets-coop ets-cafeteria) 
     (a-heure   9h00         11h45           15h00))
  (a-ete-vu (nom Pierre)
     (etait-a uqam ets-cafeteria ets-cafeteria) 
     (a-heure 7h00                   11h30         14h00))
  (a-ete-vu (nom Levis)
     (etait-a uqam ets-cafeteria ets-cafeteria) 
     (a-heure 10h00                   11h30         14h00))
  (a-ete-vu (nom Cardinal)
     (etait-a metro-berri-uqam peel-pub ets-cafeteria) 
     (a-heure 7h00                   11h30         22h00))
)

(deffacts vehicules
  (personne-vehicule (nom Paul) (utilise-vehicule marche autobus))
  (personne-vehicule (nom Frank) (utilise-vehicule marche motocyclette))
  (personne-vehicule (nom Benoit) (utilise-vehicule automobile))
  (personne-vehicule (nom Pierre) (utilise-vehicule automobile))
  (personne-vehicule (nom Levis) (utilise-vehicule autobus))
  (personne-vehicule (nom Cardinal) (utilise-vehicule autobus))
)
