;;;;;;;;;;;;;;;;;;;
; DICTIONNAIRES 
;;;;;;;;;;;;;;;;;;;

;Rigidite du cadavre
(deffacts map_rigidite-cadavre
	(Apres 2 heure du deces on observe la regidite membres superieurs)
	(Apres 4 heure du deces on observe la regidite membres inferieurs)
	(Apres 12 heure du deces on observe la regidite complete corps)
)

;Coloration du cadavre
(deffacts map_coloration-cadavre
	(Apres 3 heure du deces on observe une decoloration jaune de la peau)
	(Apres 4 heure du deces on observe une decoloration verte de la peau)
	(Apres 5 heure du deces on observe une decoloration mauve de la peau)
	(Apres 24 heure du deces on observe une decoloration noire de la peau)
)

;Temperature piece du cadavre
(deffacts map_temperature-difference
	(Une temperature de 16 Celcius engendre une difference 2 heures)
	(Une temperature de 18 Celcius engendre une difference 1 heures)
	(Une temperature de 20 Celcius engendre une difference 0 heures)
	(Une temperature de 22 Celcius engendre une difference -1 heures)
	(Une temperature de 24 Celcius engendre une difference -2 heures)
	(Une temperature de 26 Celcius engendre une difference -3 heures)
	(Une temperature de 28 Celcius engendre une difference -4 heures)
	(Une temperature de 30 Celcius engendre une difference -5 heures)
	(Une temperature de 32 Celcius engendre une difference -6 heures)
)



;Constante pour determiner un motif selon la relation avec un autre individu
(deffacts map_relation-motif
	(Relation Amicale insinue motif (list Chagrin Assistance))
	(Relation Hostile insinue motif (list Croyance Haine))
	(Relation Neutre insinue motif (list Folie Jalousie))
)

;Constante pour determiner un motif selon l'intensite de la blessure
(deffacts map_intensite-motif
	(Intensite Multitude_De_Coups insinue motif (list Haine Chagrin))
	(Intensite Simple_Sur_Point_Vitaux insinue motif (list Assistance Jalousie))
	(Intensite Membres_Non_Attache insinue motif (list Croyance Folie))
)

;Constantes pour déterminer une arme selon son residu
(deffacts map_arme-residu
	(arme masse donne residu (list fer))
	(arme barre_a_clou donne residu (list fer rouille))
	(arme tournevis donne residu (list fer rouille))
	(arme couteau donne residu (list acier))
	(arme fusil donne residu (list poudre))
	(arme crayon donne residu (list bois graphite))
	(arme corde donne residu (list nylon))
	(arme fourchette donne residu (list fer))
	(arme regle donne residu (list bois))
	(arme nourriture_immangeable donne residu (list aucun))
)

;Constantes pour déterminer une arme selon son traumatisme creer
(deffacts map_arme-trauma
	;(arme masse peut cree trauma (list fer))
	(arme barre_a_clou peut cree trauma (list Traumatism_cranien Hemorragie Rupture_des_capillaires Hematome Traumatisme_thoracique))
	(arme tournevis peut cree trauma (list Traumatism_cranien Hemorragie Plaie_de_l'artere Hematome Traumatisme_thoracique))
	(arme couteau peut cree trauma (list Hemorragie Plaie_de_l'artere Traumatisme_thoracique))
	(arme fusil peut cree trauma (list Hemorragie Plaie_de_l'artere Traumatisme_thoracique))
	(arme crayon peut cree trauma (list Hemorragie Traumatism_cranien Hematome Traumatisme_thoracique))
	(arme corde peut cree trauma (list Taches_rouges_au_niveau_du_blanc_de_l'oeil Rupture_des_capillaires Cyanose_visibles_de_la_face Hemorragie))
	;(arme fourchette peut cree trauma (list fer))
	;(arme regle peut cree trauma (list fer))
	;(arme nourriture_immangeable peut cree trauma (list fer))
)

;Constantes pour déterminer une arme selon son traumatisme creer
(deffacts map_arme-trace
	;(arme masse laisse trace (list fer))
	(arme barre_a_clou laisse trace (list Rectangulaire Plaie_profonde Plaie_superficiel Triangulaire Localise))
	(arme tournevis laisse trace (list Plaie_profonde Perforation Point_d'entre Point_de_sortie Cylindrique Disperse))
	(arme couteau laisse trace (list Plaie_profonde Perforation Point_d'entre Triangulaire Disperse Courbure))
	(arme fusil laisse trace (list Plaie_profonde Perforation Point_d'entre Point_de_sortie Disperse Sillon))
	(arme crayon laisse trace (list Plaie_profonde Perforation Point_d'entre Cylindrique Disperse))
	(arme corde laisse trace (list Marque_circulaire Localise))
	;(arme fourchette laisse trace (list fer))
	;(arme regle laisse trace (list fer))
	;(arme nourriture_immangeable laisse trace (list fer))
)

;Constante pour determiner les armes accessible par metiers
(deffacts map_arme-metier
	(Metier Cuisinier possede arme (list couteau fourchette nourriture_immangeable))
	(Metier Professeur possede arme (list crayon regle))
	(Metier Mecanicien possede arme (list tournevis))
)

;Constantes pour déterminer si une arme necessite de l experience
(deffacts map_arme-experience
	(Arme masse necessite Novice)
	(Arme barre_a_clou necessite Experimente)
	(Arme tournevis necessite Experimente)
	(Arme couteau necessite Experimente)
	(Arme fusil necessite Novice)
	(Arme crayon necessite Experimente)
	(Arme corde necessite Experimente)
	(Arme fourchette necessite Novice)
	(Arme regle necessite Experimente)
	(Arme nourriture_immangeable necessite Novice)
)

;Constante pour determiner un motif selon la relation avec un autre individu
(deffacts map_lieu_arme
	(Armes contenues dans canal-lachine sont (list barre_a_clou))
	(Armes contenues dans ets-100genie sont (list tournevis))
	(Armes contenues dans ets-cafeteria sont (list couteau))
	(Armes contenues dans la-ronde sont (list tournevis nourriture_immangeable))
	(Armes contenues dans metro-atwater sont (list corde couteau))
	(Armes contenues dans metro-jean-talon sont (list corde couteau))
	(Armes contenues dans mont-royal sont (list fusil masse))
	(Armes contenues dans peel-pub sont (list fusil nourriture_immangeable))
	(Armes contenues dans universite-de-montreal sont (list tournevis))
	(Armes contenues dans uqam sont (list tournevis barre_a_clou))
	
	(Armes contenues dans stade-olympique sont (list nourriture_immangeable))
)


; Lieux hors ETS. Typiquement il faudrait un vehicule pour aller de un a l'autre
(deffacts lieux-hors-ets
   (lieu (nom canal-lachine) (lat 45.4583) (long 73.6117))
   (lieu (nom centre-des-sciences) (lat 45.5050) (long 73.5500))
   (lieu (nom chinatown) (lat 45.5076) (long 73.5608))
   (lieu (nom gare-blainville) (lat 45.6722) (long 73.8658))
   (lieu (nom gare-centrale) (lat 45.4996) (long 73.5668))
   (lieu (nom gare-deux-montagnes) (lat 45.5456) (long 73.9122))
   (lieu (nom gare-repentigny) (lat 45.7356) (long 73.4858))
   (lieu (nom hopital-notre-dame) (lat 45.5260) (long 73.5640))
   (lieu (nom hotel-de-ville) (lat 45.5087) (long 73.5539))
   (lieu (nom la-ronde) (lat 45.5225) (long 73.5350))
   (lieu (nom metro-angrignon) (lat 45.4461) (long 73.6036))
   (lieu (nom metro-atwater) (lat 45.4897) (long 73.5861))
   (lieu (nom metro-berri-uqam) (lat 45.5153) (long 73.5611))
   (lieu (nom metro-honore-beaugrand) (lat 45.5967) (long 73.5356))
   (lieu (nom metro-jean-talon) (lat 45.5839) (long 73.6142))
   (lieu (nom metro-montmorency) (lat 45.5586) (long 73.7217))
   (lieu (nom mont-royal) (lat 45.5064) (long 73.5889))
   (lieu (nom parc-jean-drapeau) (lat 45.5095) (long 73.5274))
   (lieu (nom peel-pub) (lat 45.5008) (long 73.5747))
   (lieu (nom polytechnique) (lat 45.5050) (long 73.6140))
   (lieu (nom stade-olympique) (lat 45.5580) (long 73.5520))
   (lieu (nom station-train-roxboro-pierrefonds) (lat 45.5092) (long 73.8094))
   (lieu (nom uqam) (lat 45.5139) (long 73.5603))
   (lieu (nom universite-de-montreal) (lat 45.5033) (long 73.6175))
)

; Lieux dans ETS. On considere qu'ils ont une distance de zero entre chaque
(deffacts lieux-dans-ETS
   (lieu (nom ets-cafeteria) (lat 45.4953) (long 73.5629))
   (lieu (nom ets-coop) (lat 45.4953) (long 73.5629))
   (lieu (nom ets-100genie) (lat 45.4953) (long 73.5629))
)

(deffacts types-de-vehicule
   (vehicule (type motocyclette) (vitesse-moyenne 70))
   (vehicule (type automobile) (vitesse-moyenne 50))
   (vehicule (type autobus) (vitesse-moyenne 35))
   (vehicule (type bicyclette) (vitesse-moyenne 20))
   (vehicule (type marathon) (vitesse-moyenne 12))
   (vehicule (type marche) (vitesse-moyenne 7))
)

