
; Scenario 3- Francois-Guy Gallant GALF10038504

; Enumeration des faits.
(deffacts personnages

  ;;;;;;;;;;;;;; BOB
  
  (Personnage Bob avait une relation Hostile avec la victime)
  (Personnage Bob est Mecanicien)
  (Personnage Bob est une personne Experimente)
  (a-ete-vu (nom Bob)
     (etait-a ets-cafeteria ets-cafeteria ets-cafeteria ets-cafeteria ets-cafeteria)
     (a-heure 09h30 12h00 16h00 19h00 21h00)
  )
  (personne-vehicule (nom Bob) (utilise-vehicule marche autobus motocyclette))
  
  ;;;;;;;;;;;;;; ÉMILIE 
  
  (Personnage Emilie avait une relation Hostile avec la victime)
  (Personnage Emilie est une personne Novice)
  (a-ete-vu (nom Emilie)
     (etait-a ets-cafeteria metro-berri-uqam)
     (a-heure 11h00 22h30)
  )
  (personne-vehicule (nom Emilie) (utilise-vehicule marche bicyclette))
  
  ;;;;;;;;;;;;;; JULIEN
  
  (Personnage Julien avait une relation Amicale avec la victime)
  (Personnage Julien est une personne Novice)
  (a-ete-vu (nom Julien)
     (etait-a ets-cafeteria gare-centrale metro-berri-uqam)
     (a-heure 09h30 21h00 22h30)
  )
  (personne-vehicule (nom Julien) (utilise-vehicule marche autobus))
  
  ;;;;;;;;;;;;;; JIA YAN
  
  (Personnage Jia-Yan avait une relation Amicale avec la victime)
  (Personnage Jia-Yan est une personne Novice)
  (a-ete-vu (nom Jia-Yan)
     (etait-a chinatown berri-uqam)
     (a-heure 09h00 21h00)
  )
  (personne-vehicule (nom Jia-Yan) (utilise-vehicule marche autobus))
  
  ;;;;;;;;;;;;;; RICHARD
  
  (Personnage Richard avait une relation Neutre avec la victime)
  (Personnage Richard est une personne Experimente)
  (a-ete-vu (nom Richard)
     (etait-a centre-des-sciences gare-blainville)
     (a-heure 08h00 16h00)
  )
  (personne-vehicule (nom Richard) (utilise-vehicule marche automobile))
  (personnage-arme_acces (of Richard)(is fusil))
  (personnage-arme_acces (of Richard)(is couteau))
  
  ;;;;;;;;;;;;;; PAUL ARCAND
  
  (Personnage Paul-Arcand avait une relation Neutre avec la victime)
  (Personnage Paul-Arcand est une personne Experimente)
  (a-ete-vu (nom Paul-Arcand)
     (etait-a gare-centrale gare-centrale la-ronde)
     (a-heure 05h30 10h00 15h00)
  )
  (personne-vehicule (nom Paul-Arcand) (utilise-vehicule marche automobile))
  
  ;;;;;;;;;;;;;; BERNARD DRAINVILLE
  
  (Personnage Bernard-Drainville avait une relation Neutre avec la victime)
  (Personnage Bernard-Drainville est une personne Novice)
  (a-ete-vu (nom Bernard-Drainville)
     (etait-a gare-centrale metro-berri-uqam)
     (a-heure 08h00 18h00)
  )
  (personne-vehicule (nom Bernard-Drainville) (utilise-vehicule marche automobile))
  
)


; extrait du cadavre
(deffacts situation-cadavre

  (lieu-du-crime gare-centrale)
  (Le corps de Francois ete decouvers a 21 heure)
  (Le corps de la victime a ete trouve dans l'etat suivant: Membres_Non_Attache)
  (La temperature de la piece est de 20 Celcius)
	(Le cadavre a une decoloration jaune de la peau)
	(Le cadavre a les membres superieurs rigides)
  
  (trauma-vu Hematome)
  (trace-trouve Rectangulaire)
  (residu-trouve fer)
)

