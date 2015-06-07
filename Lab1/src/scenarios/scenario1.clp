;Scenario 4 - Max

; extrait du cadavre
(deffacts situation-cadavre
	(lieu-du-crime mont-royal)
	(Le corps de Benoit ete decouvers a 23 heure)
	(Le corps de la victime a ete trouve dans l'etat suivant: Simple_Sur_Point_Vitaux)
	(La temperature de la piece est de 30 Celcius)
	(Le cadavre a une decoloration verte de la peau)
	(Le cadavre a les membres inferieurs rigides)

	(trauma-vu Rupture_des_capillaires)
	(trace-trouve Marque_circulaire)
	(residu-trouve nylon)
	(residu-trouve bois)
)

; Enumeration des faits.
(deffacts personnages
	;cour le matin le long du canal lachine puis mange a chinatown avec Sandra, 
	;visite le centre puis bois au peel-pub avec guillaume
	(Personnage Max avait une relation Amicale avec la victime)
	(Personnage Max est Etudiant)
	(Personnage Max est une personne Experimente)
	(personne-vehicule (nom Max) (utilise-vehicule marche bicyclette))
	(a-ete-vu (nom Max) 
		(etait-a   canal-lachine chinatown centre-des-sciences peel-pub) 
		(a-heure   9h00          12h00     15h00               18h00))
	
	(Personnage Guillaume avait une relation Neutre avec la victime)
	;(Personnage Guillaume est Cuisinier)
	(Personnage Guillaume est une personne Novice)
	(personne-vehicule (nom Guillaume) (utilise-vehicule marche))
	(a-ete-vu (nom Guillaume) 
		(etait-a   peel-pub) 
		(a-heure   18h00))
	
	;fait ses papier le matin puis part voir la f1
	(Personnage Raby avait une relation Hostile avec la victime)
	(Personnage Raby est Mecanicien)
	(Personnage Raby est une personne Novice)
	(personne-vehicule (nom Raby) (utilise-vehicule marche autobus))
	(a-ete-vu (nom Raby) 
		(etait-a   hotel-de-ville metro-berri-uqam metro-jean-talon parc-jean-drapeau) 
		(a-heure   9h00           11h45            14h00            15h00))

	;accompagne raby le matin puis prend le train pour 2 montage
	(Personnage Rachid avait une relation Neutre avec la victime)
	(Personnage Rachid est Mecanicien)
	(Personnage Rachid est une personne Experimente)
	(personne-vehicule (nom Rachid) (utilise-vehicule automobile))
	(a-ete-vu (nom Rachid) 
		(etait-a   hotel-de-ville gare-centrale gare-deux-montagnes) 
		(a-heure   9h00           11h45           15h00))
	
	;cours puis mange a chinatown et visite le centre avec max
	(Personnage Sandra avait une relation Hostile avec la victime)
	(Personnage Sandra est Etudiant)
	(Personnage Sandra est une personne Novice)
	(personne-vehicule (nom Sandra) (utilise-vehicule marche bicyclette))
	(a-ete-vu (nom Sandra) 
		(etait-a   canal-lachine chinatown uqam) 
		(a-heure   9h00          12h00     15h00))
	
	;mange a la cafet de l'ets puis part a la ronde
	(Personnage Richard avait une relation Amicale avec la victime)
	(Personnage Richard est Professeur)
	(Personnage Richard est une personne Experimente)
	(personne-vehicule (nom Richard) (utilise-vehicule automobile))
	(a-ete-vu (nom Richard) 
		(etait-a   ets-cafeteria la-ronde) 
		(a-heure   12h20         14h40))
	
	;prend le metro le matin, mange a la cafet, puis au stade-olympique
	(Personnage Charly avait une relation Amicale avec la victime)
	(Personnage Charly est Cuisinier)
	(Personnage Charly est une personne Novice)
	(personne-vehicule (nom Charly) (utilise-vehicule marche autobus))
	(a-ete-vu (nom Charly) 
		(etait-a   metro-honore-beaugrand ets-cafeteria stade-olympique) 
		(a-heure   9h00                   11h45         16h00))
	
	;avec Charly a 9h puis etudie a poly a 15h
	(Personnage Caroline avait une relation Hostile avec la victime)
	(Personnage Caroline est Professeur)
	(Personnage Caroline est une personne Novice)
	(personne-vehicule (nom Caroline) (utilise-vehicule marathon))
	(a-ete-vu (nom Caroline) 
		(etait-a   metro-honore-beaugrand polytechnique) 
		(a-heure   9h00                   15h00))
  
	;se ballade au mont-royal le matin, puis rejoin Sandra a l'uqam a 15h
	(Personnage Samantha avait une relation Neutre avec la victime)
	(Personnage Samantha est Etudiant)
	(Personnage Samantha est une personne Novice)
	(personne-vehicule (nom Samantha) (utilise-vehicule marche autobus))
	(a-ete-vu (nom Samantha) 
		(etait-a   mont-royal   metro-atwater uqam) 
		(a-heure   9h00         15h30         15h00))
)
