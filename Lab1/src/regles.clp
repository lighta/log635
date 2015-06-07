(clear)

;(watch all)

;;;;;;;;;;;;;;;;;;;
; TEMPLATES 
;;;;;;;;;;;;;;;;;;;

;Template pour motif-relation-personne
(deftemplate personnage-relation (slot of) (slot is))

;Template pour personne-arme_acces
(deftemplate personnage-arme_acces (slot of) (slot is))

;Template pour personne-arme_utilisable
(deftemplate personnage-arme_utilisable (slot of) (slot is))

;Template pour vitesse-par-vehicule
(deftemplate vehicule (slot type) (slot vitesse-moyenne))

;Template pour suspect-par-arme-et-motif
(deftemplate suspect-par-arme (slot is) (slot with))
(deftemplate suspect-par-arme-et-motif (slot is) (slot with))

;Template pour personne
(deftemplate personne-vehicule
   (slot nom)
   (multislot utilise-vehicule)
)

;Template pour dire qu'on a vu telle personne a tel endroit
(deftemplate a-ete-vu
   (slot nom)
   (multislot etait-a)
   (multislot a-heure)
)

;Template pour lieux
(deftemplate lieu
   (slot nom)
   (slot lat) ;latitude
   (slot long) ;longitude
)

; Chargement du monde commun
(batch "dictionaire.clp")
;chargement des fonctions
(batch "func_and_query.clp")

;;;;;;;;;;;;;;;
;; SCENARIO
;;;;;;;;;;;;;;;

; Chargement d'un scenarioen particulier, commentez celui que vous voulez
;(batch "scenarios/scenario_validation.clp")
;(batch "scenarios/scenario1.clp")
;(batch "scenarios/scenario2.clp")
;(batch "scenarios/scenario3.clp")
(batch "scenarios/scenario4.clp")




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; REGLES pour verifier que nos faits sont bien ecrits
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defrule verifier-integrite-donnees-01
	(declare (salience 999) )
	(a-ete-vu (nom ?nom) (etait-a $?lieux) (a-heure $?heures))
	=>
	(if (neq (length$ ?lieux) (length$ ?heures)) then
		(printout t "ERROR: La template a-ete-vu doit etre utilise avec le meme nombre de lieux et d'heures!" crlf)
		(halt)
	)
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; REGLES POUR BRANCHE: arme
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrule traumatise-determine-arme
	(declare (salience 30) )
	(trauma-vu ?trauma)
	(arme ?arme peut cree trauma $?list)
	(test (member$ ?trauma ?list))
	=>
	;(printout t "L'arme du crime (selon trauma "?trauma") peu etre le/la " ?arme "." crlf)
	(assert (arme-possible-par-trauma ?arme))
)

(defrule residu-determine-arme
	(declare (salience 31) )
	(residu-trouve ?residu)
	(arme ?arme donne residu $?list)
	(test (member$ ?residu ?list))
	=>
	;(printout t "L'arme du crime (selon residu "?residu") peu etre le/la " ?arme "." crlf)
	(assert (arme-possible-par-residu ?arme))
)

(defrule trace-determine-arme
	(declare (salience 32) )
	(trace-trouve ?trace)
	(arme ?arme laisse trace $?list)
	(test (member$ ?trace ?list))
	=>
	;(printout t "L'arme du crime (selon trace "?trace") peu etre le/la " ?arme "." crlf)
	(assert (arme-possible-par-trace ?arme))
)

;Arme potentiel (by cadavre) !
(defrule arme_potentiel
	(declare (salience 50) )
	(arme-possible-par-trauma ?arme)
	(arme-possible-par-residu ?arme)
	(arme-possible-par-trace ?arme)
	=>
	(printout t "L'arme potentiel selon cadavre est " ?arme "." crlf)
	(assert (arme-potentiel ?arme))
)

;Le metier d'une personne offre la possibilite d'avoir une certaine arme
(defrule personnage-metier-determine-armeAcces
	(declare (salience 50) )
	(Metier ?metier possede arme $?list)
	(Personnage ?personnage est ?metier)
	=>
	(bind ?i (length$ ?list))
	(while (> ?i 0) do
		(bind ?arme (nth$ ?i $?list))
		(printout t ?personnage " avait acces a " ?arme " puisqu il est " ?metier "." crlf)
		(assert (personnage-arme_acces (of ?personnage)(is ?arme)))
		(bind ?i (- ?i 1))
	)
)

(defrule personnage-location-determine-armeAcces
	(a-ete-vu (nom ?nom) (etait-a $?lieux) (a-heure $?heures))
	(Armes contenues dans ?lieu sont $?list)
	;(lieu (armes $?list))
	(test (member$ ?lieu $?lieux))
	(L'Heure du deces reel ?hDeces heures)
	=>
	(bind ?hDeces (str-cat ?hDeces "h00"))
	(bind ?position (member$ ?lieu ?lieux))
	(bind ?heure-a-lieu (nth$ ?position ?heures))
  
	(if (< (convertir-en-minutes ?heure-a-lieu) (convertir-en-minutes ?hDeces)) then
		; La personne etait dans un lieu avant le crime; elle a donc pu prendre les armes du lieu
		; pour faire le crime. On lui assigne toutes les armes du lieu
		(for (bind ?j 1) (<= ?j (length$ ?list)) (++ ?j)
			(bind ?arme-disponible (nth$ ?j ?list))
			(printout t ?nom " avait acces avant le crime a l'arme " ?arme-disponible " dans le lieu " ?lieu "." crlf)
			(assert (personnage-arme_acces (of ?nom)(is ?arme-disponible)))
		)
	)
)

(defrule personnage-experienceProffessionel-armeAcces-determine-armeUtilisable
	(declare (salience 49) )
	(Personnage ?personnage est une personne ?experience)
	(Arme ?arme necessite ?exp)
	(personnage-arme_acces (of ?personnage) (is ?arme))
	(test(= (str-compare ?experience "Experimente") 0))
	=>
	(printout t ?personnage " peux utiliser " ?arme " puisqu il est " ?experience "." crlf)
	(assert (personnage-arme_utilisable (of ?personnage)(is ?arme)))
)

(defrule personnage-AucuneExperienceProffessionel-armeAcces-determine-armeUtilisable
	(declare (salience 49) )
	(Personnage ?personnage est une personne ?experience)
	(Arme ?arme necessite ?exp)
	(personnage-arme_acces (of ?personnage) (is ?arme))
	;(test(= (str-compare ?experience "Inexperimente") 0)) ;undefined should work for novice arm
	(test(= (str-compare ?exp "Novice") 0))
	=>
	(printout t ?personnage " peux utiliser " ?arme " puisqu il est " ?experience "." crlf)
	(assert (personnage-arme_utilisable (of ?personnage)(is ?arme)))
)


;Arme potentiel intersec PersonneArmeUtilisable !
(defrule suspect-par-arme
	(declare (salience 19) )
	(arme-potentiel ?arme)
	(personnage-arme_utilisable (of ?personnage) (is ?arme))
	=>
	(printout t "Le crime pourrait avoir ete fait par (selon arme) " ?personnage " avec le/la " ?arme "." crlf)
	(assert (suspect-par-arme (is ?personnage) (with ?arme)))
)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; REGLES POUR BRANCHE: motif
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrule relation-determine-motif
	(declare (salience 50) )
	(Personnage ?personnage avait une relation ?relation avec la victime)
	(Relation ?relation insinue motif $?list)
	=>
	(bind ?i (length$ ?list))
	(while (> ?i 0) do
		(bind ?motifPossible (nth$ ?i $?list))
		;(printout t "Le motif du crime (selon la relation) peut-etre le/la " ?motifPossible "." crlf)
		(assert (motif-possible-par-relation ?motifPossible))
		(bind ?i (- ?i 1))
	)
)

(defrule intensite-determine-motif
	(declare (salience 50) )
	(Le corps de la victime a ete trouve dans l'etat suivant: ?etat)
	(Intensite ?etat insinue motif $?list)
	=>
	(bind ?i (length$ ?list))
	(while (> ?i 0) do
		(bind ?motifPossible (nth$ ?i $?list))
		;(printout t "Le motif (selon l'intensite) peut-etre le/la " ?motifPossible "." crlf)
		(assert (motif-possible-par-intensite ?motifPossible))
		(bind ?i (- ?i 1))
	)
)

(defrule personnage-intensite-relation-determine-motif
	(declare (salience 49) )
	(Personnage ?personnage avait une relation ?relation avec la victime)
	(Le corps de la victime a ete trouve dans l'etat suivant: ?etat)
	(Relation ?relation insinue motif $?list2)
	(Intensite ?etat insinue motif $?list1)
	(intersection$ $?list1 $?list2)
	=>
	(bind ?motif (intersection$ $?list1 $?list2))
	(printout t "Le motif (selon l'intensite et la relation) associe a " ?personnage " est le/la" ?motif "." crlf)
	(assert (personnage-relation (of ?personnage)(is ?motif)))
)

(defrule suspect-par-arme-et-motif
	(personnage-relation (of ?personnage)(is ?motif))
	(suspect-par-arme (is ?personnage) (with ?arme))
	=>
	(printout t "Le crime pourrait avoir ete fait par " ?personnage " (selon motif+arme) avec l'arme " ?arme " et le motif " ?motif "." crlf)
	(assert (suspect-par-arme-et-motif (is ?personnage) (with ?arme)))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



;On estime l'heure de la mort a partir de l'etat du cadavre
(defrule etat-decoloration-cadavre
	(declare (salience 200))
	(Le cadavre a une decoloration ?couleur de la peau)
	?fait1 <- (Apres ?temps heure du deces on observe une decoloration ?couleur2 de la peau)
	(test (neq ?couleur ?couleur2))
	=>
	;(printout t "retract temps " ?temps crlf) ;trace
	(retract ?fait1)
)


;On estime l'heure de la mort a partir de l'etat du cadavre
(defrule etat-regidite-cadavre
	(declare (salience 190))
	(Le cadavre a les ?type1 ?type2 rigides)
	?fait1 <- (Apres ?temps heure du deces on observe la regidite ?type3 ?type4)
	(test  (not(and (eq ?type1 ?type3) (eq ?type2 ?type4))))
	=>
	;(printout t "retract rigidite " ?type3 " " ?type4 crlf) ;trace
	(retract ?fait1)
)

;On estime l'heure de la mort a partir de l'etat du cadavre
(defrule etat-regidite-decoloration-cadavre
	(declare (salience 180))
	(Apres ?temps1 heure du deces on observe la regidite ?type ?type2)
	?fait1 <- (Apres ?temps2 heure du deces on observe une decoloration ?couleur de la peau)
	(test (>= ?temps1 ?temps2))
	=>
	;(printout t "apres " ?temps1 " marques physiques 1" crlf) ;trace
	(assert (Apres ?temps1 heures on observe des marques physique))
)

;On estime l'heure de la mort a partir de l'etat du cadavre
(defrule etat-decoloration-regidite-cadavre
	(declare (salience 170))
	(Apres ?temps1 heure du deces on observe une decoloration ?couleur de la peau)
	?fait1 <- (Apres ?temps2 heure du deces on observe la regidite ?type ?type2)
	(test (>= ?temps1 ?temps2))
	=>
	;(printout t "apres " ?temps1 " marques physiques 2" crlf) ;test
	(assert (Apres ?temps1 heures on observe des marques physiques))
)

;On ajuste la fourchette de temps
(defrule temperature-piece
	(declare (salience 160))
	(La temperature de la piece est de ?degre Celcius)
	(Une temperature de ?degre Celcius engendre une difference ?hrs heures)
	=>
	(printout t "Difference d'heure due a la temperature " ?hrs crlf) ;trace
	(assert (Difference d'heure du a la temperature est de ?hrs))
)


;On determine l'heure du deces
;l'heure du crime en fait
(defrule heure-deces
	(declare (salience 150))
	(Le corps de ?nomCadavre ete decouvers a ?heureDecouverte heure)
	(Apres ?heureMarque heures on observe des marques physiques)
	(Difference d'heure du a la temperature est de ?hrsTemp)
	=>
	(bind ?hDeces (- ?heureDecouverte (- 24 (+ (- ?heureDecouverte ?heureMarque) ?hrsTemp))))
	(assert (L'Heure du deces reel ?hDeces heures))
	(printout t "On a trouve le corp de "?nomCadavre" a " ?heureDecouverte "h, neanmoins l'heure du deces est " ?hDeces "h" crlf)
)



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; REGLES POUR BRANCHE: transport
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrule suspect-vitesse-la-plus-rapide
	(personne-vehicule (nom ?nom) (utilise-vehicule $?vehicules))
	=>
	(printout t ?nom " pouvait utiliser ces vehicules: " ?vehicules crlf)
	; Calculer seulement la vitesse avec le vehicule le plus rapide. Pas besoin de verifier la marche si la personne a une moto
	(bind ?vitesse-la-plus-rapide 0)
	(foreach ?un-vehicule ?vehicules
		(bind ?query-result (run-query* chercher-vehicule ?un-vehicule))
		(?query-result next)
		(bind ?vitesse-un-vehicule (?query-result getString vitesse))
		(if (> ?vitesse-un-vehicule ?vitesse-la-plus-rapide) then (bind ?vitesse-la-plus-rapide ?vitesse-un-vehicule))
	)
	(printout t "La vitesse la plus rapide a laquelle " ?nom " peut voyager est " ?vitesse-la-plus-rapide "Km/h" crlf)
	(assert (vitesse-la-plus-rapide ?nom ?vitesse-la-plus-rapide))
)



(defrule suspect-a-ete-vu-avant-et-apres-le-crime
	(a-ete-vu (nom ?nom) (etait-a $?lieux) (a-heure $?heures))
	(L'Heure du deces reel ?hDeces heures)
	=>
	(bind ?hDeces (str-cat ?hDeces "h00")) ; pour mettre en format 15h00

	;On donne un lieu avant crime et lieu apres crime nuls par defaut
	(bind ?lieu-avant-crime nil)
	(bind ?lieu-apres-crime nil)
	;On donne une heure avant crime et apres crime extremes, seront remplaces dans notre for
	(bind ?heure-avant-crime -1)
	(bind ?heure-apres-crime 9999)

	(for (bind ?i 1) (<= ?i (length$ ?lieux)) (++ ?i)
		(if (> (convertir-en-minutes (nth$ ?i ?heures)) (convertir-en-minutes ?heure-avant-crime)) then
			 (if (< (convertir-en-minutes (nth$ ?i ?heures)) (convertir-en-minutes ?hDeces)) then
				(bind ?heure-avant-crime (nth$ ?i ?heures))
				(bind ?lieu-avant-crime (nth$ ?i ?lieux))
			 )
		)
		(if (< (convertir-en-minutes (nth$ ?i ?heures)) (convertir-en-minutes ?heure-apres-crime)) then
			(if (> (convertir-en-minutes (nth$ ?i ?heures)) (convertir-en-minutes ?hDeces)) then
				(bind ?heure-apres-crime (nth$ ?i ?heures))
				(bind ?lieu-apres-crime (nth$ ?i ?lieux))
			)
		)
	)
	(printout t ?nom " a ete vu pour la derniere fois avant le crime a " ?lieu-avant-crime " a " ?heure-avant-crime crlf)
	(printout t ?nom " a ete vu pour la premiere fois apres le crime a " ?lieu-apres-crime " a " ?heure-apres-crime crlf)
	(assert (a-ete-vu-avant-crime ?nom ?lieu-avant-crime ?heure-avant-crime))
	(assert (a-ete-vu-apres-crime ?nom ?lieu-apres-crime ?heure-apres-crime))
)

(defrule suspect-minutes-pour-se-rendre-au-lieu-du-crime
	(a-ete-vu-avant-crime ?nom ?lieu-avant-crime ?heure-avant-crime)
	(L'Heure du deces reel ?hDeces heures)
	=>
	(bind ?hDeces (str-cat ?hDeces "h00"))  ; format 12h00
	(bind ?minutes-avant-crime (- (convertir-en-minutes ?hDeces) (convertir-en-minutes ?heure-avant-crime)))
	(printout t ?nom " avait " ?minutes-avant-crime "min pour se rendre sur les lieux du crime" crlf)
	(assert (temps-avant-crime ?nom ?minutes-avant-crime))
)

(defrule suspect-minutes-pour-echapper-au-lieu-du-crime
	(a-ete-vu-apres-crime ?nom ?lieu-apres-crime ?heure-apres-crime)
	(L'Heure du deces reel ?hDeces heures)
	=>
	(bind ?hDeces (str-cat ?hDeces "h00"))  ; format 12h00
	(bind ?minutes-apres-crime (- (convertir-en-minutes ?heure-apres-crime) (convertir-en-minutes ?hDeces)))
	(printout t ?nom " avait " ?minutes-apres-crime "min pour echapper au lieu du crime" crlf)
	(assert (temps-apres-crime ?nom ?minutes-apres-crime))
)

(defrule suspect-distance-pour-se-rendre-au-lieu-du-crime
	; Position du lieu du crime
	(lieu-du-crime ?lieu-crime)
	(lieu (nom ?lieu-crime) (lat ?lat-crime) (long ?long-crime))
	; Position de la personne avant le crime
	(a-ete-vu-avant-crime ?nom ?lieu-avant-crime ?heure-avant-crime)
	(lieu (nom ?lieu-avant-crime) (lat ?lat-suspect) (long ?long-suspect))
	=>
	(bind ?distance (distance-entre-deux-points ?lat-crime ?long-crime ?lat-suspect ?long-suspect))
	(printout t "Le suspect " ?nom " devait parcourir " ?distance "km pour aller au lieu du crime" crlf)
	(assert (distance-avant-crime ?nom ?distance))
)

(defrule suspect-distance-pour-echapper-au-lieu-du-crime
	; Position du lieu du crime
	(lieu-du-crime ?lieu-crime)
	(lieu (nom ?lieu-crime) (lat ?lat-crime) (long ?long-crime))
	; Position de la personne apres le crime
	(a-ete-vu-apres-crime ?nom ?lieu-apres-crime ?heure-apres-crime)
	(lieu (nom ?lieu-apres-crime) (lat ?lat-suspect) (long ?long-suspect))
	=>
	(bind ?distance (distance-entre-deux-points ?lat-crime ?long-crime ?lat-suspect ?long-suspect))
	(printout t "Le suspect " ?nom " devait parcourir " ?distance "km pour echapper au lieu du crime" crlf)
	(assert (distance-apres-crime ?nom ?distance))
)

(defrule suspect-pouvait-se-rendre-au-lieu-du-crime
	(temps-avant-crime ?nom ?minutes)
	(distance-avant-crime ?nom ?distance)
	(vitesse-la-plus-rapide ?nom ?vitesse-la-plus-rapide)
	=>
	(printout t ?nom " devait parcourir " ?distance "km en " ?minutes " minutes pour se rendre au lieu du crime" crlf)
	(bind ?vitesse-necessaire (/ (round (* 100 (/ ?distance (/ ?minutes 60)))) 100))
	(printout t "La vitesse necessaire est de " ?vitesse-necessaire " km/h" crlf)

	(if (> ?vitesse-necessaire ?vitesse-la-plus-rapide) then
		(printout t "La vitesse maximale de " ?nom " est de " ?vitesse-la-plus-rapide
			"km/h; il n'aurait pas pu se rendre a temps." crlf)
	else
		(printout t ?nom " aurait pu se rendre sur les lieux du crime car sa vitesse max est de " 
			?vitesse-la-plus-rapide "km/h." crlf)
		(assert (aurait-pu-se-rendre ?nom))
	)
)

(defrule suspect-pouvait-echapper-au-lieu-du-crime
	(temps-apres-crime ?nom ?minutes)
	(distance-apres-crime ?nom ?distance)
	(vitesse-la-plus-rapide ?nom ?vitesse-la-plus-rapide)
	=>
	(printout t ?nom " devait parcourir " ?distance "km en " ?minutes " minutes pour echapper au lieu du crime" crlf)
	(bind ?vitesse-necessaire (/ (round (* 100 (/ ?distance (/ ?minutes 60)))) 100))
	(printout t "La vitesse necessaire est de " ?vitesse-necessaire " km/h" crlf)

	(if (> ?vitesse-necessaire ?vitesse-la-plus-rapide) then
		(printout t "La vitesse maximale de " ?nom " est de " ?vitesse-la-plus-rapide
			"km/h; il n'aurait pas pu s'echapper a temps." crlf)
	else
		(printout t ?nom " aurait pu s'echapper des lieux du crime car sa vitesse max est de " 
			?vitesse-la-plus-rapide "km/h." crlf)
		(assert (aurait-pu-sechapper ?nom))
	)
)


(defrule pouvait-etre-sur-les-lieux
	(aurait-pu-se-rendre ?nom)
	(aurait-pu-sechapper ?nom)
	=>
	(printout t "Selon les temoignages, " ?nom " aurait pu etre sur les lieux du crime." crlf)
	(assert (suspect-pouvait-etre-sur-lieux ?nom))
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; REGLE FINALE combine arme, motifs et transport
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; si pouvait etre sur les lieux, a le motif et l'arme alors c'est notre meutrier !
(defrule trouver-meurtrier
	(suspect-par-arme-et-motif (is ?nom)(with ?arme))
	(suspect-pouvait-etre-sur-lieux ?nom)
	=>
	(printout t "Le meutrier trouve est " ?nom " avec arme " ?arme crlf)
	(assert (meutrier ?nom))
  (halt) ; pas besoin de continuer a rouler si on a la reponse :D
)


(reset)
(run)
;(facts)



