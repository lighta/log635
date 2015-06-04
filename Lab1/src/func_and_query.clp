;;;;;;;;;;;;;;;;;;;
; FUNCTIONS
;;;;;;;;;;;;;;;;;;;

(deffunction convertir-en-minutes (?temps)
   "Convertit une heure (ex. 5h30) ou des minutes apres minuit (ex. 300) en minutes apres minuit. Si on a recu des minutes on a rien a convertir, on renvoie les minutes. Utile pour faire des operations + - sur des heures"
   ;(printout t "TRACE convertir-en-minutes: recu " ?temps crlf)
   (if (str-index "h" ?temps) then
      ;on a une heure, format 5h30
      (bind ?retour (transforme-heure-en-minutes ?temps))
    else
      ;on a des minutes
      (bind ?retour ?temps)
   )
   (return ?retour)
)


;non utilise pour l'instant
/*
(deffunction convertir-en-heures (?temps)
   "Convertit une heure (ex. 5h30) ou des minutes apres minuit (ex. 300) en heures. Si on a recu des heures on a rien a convertir, on renvoie les minutes. Utile pour display"
   ;(printout t "TRACE convertir-en-minutes: recu " ?temps crlf)
   (if (str-index "h" ?temps) then
      ;on a une heure, format 5h30
      (bind ?retour ?temps)
    else
      ;on a des minutes
      (bind ?retour (transforme-minutes-en-heure ?temps))
   )
   (return ?retour)
)
*/
(deffunction transforme-heure-en-minutes (?heure)
   "Transforme une heure en minutes depuis minuit, ex. 05h25 donne 325"
   ;(printout t "TRACE transforme-heure-en-minutes: recu " ?heure crlf)
   (bind ?heures (sub-string 1 (- (str-index "h" ?heure) 1) ?heure))
   (bind ?minutes (sub-string (+ (str-index "h" ?heure) 1) (str-length ?heure) ?heure))
   (return (integer (+ (* 60 ?heures) ?minutes)))
)

;non utilise pour l'instant
/*
(deffunction transforme-minutes-en-heure (?minutes)
   "Transforme des minutes apres minuit en une heure, ex. 120 donne 2h00"
   ;(printout t "TRACE transforme-minutes-en-heures: recu " ?minutes crlf)
   ;round -0.5 ca donne la fonction floor qui manque a Jess
   (bind ?partie-heure (round (- (/ ?minutes 60) 0.5))) 
   (bind ?partie-minutes (mod ?minutes 60))
   
   ; On rajoute des zeros si besoin est, pour avoir ex. 08h03
   (if (= 1 (str-length ?partie-heure)) then
      (bind ?partie-heure (str-cat "0" ?partie-heure)))
   (if (= 1 (str-length ?partie-minutes)) then
      (bind ?partie-minutes (str-cat "0" ?partie-minutes)))
      
   (return (str-cat ?partie-heure "h" ?partie-minutes))
)
*/

; calcul distance entre deux lieux sur la planete, avec approximation equirectangulaire
; selon la reponse de TreyA sur StackOverflow: http://bit.ly/1ACXaGY
(deffunction distance-entre-deux-points (?latitude1 ?longitude1 ?latitude2 ?longitude2)
   (bind ?r 6371) ; rayon de la terre en km
   (bind ?latitude1 (angle-en-radians ?latitude1))
   (bind ?latitude2 (angle-en-radians ?latitude2))
   (bind ?longitude1 (angle-en-radians ?longitude1))
   (bind ?longitude2 (angle-en-radians ?longitude2))
   
   (bind ?x (* (- ?longitude2 ?longitude1) (call Math cos (* 0.5 (+ ?latitude1 ?latitude2)))))
   (bind ?y (- ?latitude2 ?latitude1))
   (bind ?d (* ?r (sqrt (+ (* ?x ?x) (* ?y ?y)))))
   ; Approximer pour garder la distance au metre près
   (bind ?d2 (/ (round (* ?d 1000)) 1000))
   (return ?d2)
)

(deffunction angle-en-radians (?angle)
   return (/ (* (pi) ?angle) 180)
)

;;;;;;;;;;;;;;;;;;;
; QUERIES
;;;;;;;;;;;;;;;;;;;

(defquery chercher-vehicule
   (declare (variables ?vtype))
   (vehicule (type ?vtype) (vitesse-moyenne ?vitesse))
)
