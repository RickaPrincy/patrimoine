# Général
* Spécifier Dates:ajd
* Fin de simulation Dates:finSimulation - 1 année et 2 mois et 10 jours `/* Retard sur les actions */`
* Cas de LocationMaison 
* Devise en Ar

# Possesseurs
* Personnes:Zety ((40 * 2 / 2) - 2 + 3 * 1 - 1)%
* Personnes:Lita 10%
* Personnes:Rasoa 50%

# Trésoreries
* Trésoreries:loyerMaison

# Initialisation
* `objectifInitLocationMaison` Dates:ajd, objectif de 500000Ar pour Trésoreries:loyerMaison
* `initCompteLoyerMaison` Dates:ajd, entrer 500000Ar vers Trésoreries:loyerMaison `/* Commentaire */` 

# Opérations
## Rem2025, Dates:ajd, devise en Ar
* `remZety2025` `/* Commentaire */` Dates:ajd, entrer 400000Ar vers Trésoreries:zetyLoyerMaison , jusqu'à le 31 du 12-2025 tous les 1 du mois
* `remRasoa2025` Dates:ajd, entrer 500000Ar vers Trésoreries:rasoaPersonnel , jusqu'à le 31 du 12-2025 tous les 1 du mois
* `remLita2025` Dates:ajd, entrer 100000Ar vers Trésoreries:litaPersonnel , jusqu'à le 31 du 12-2025 tous les 1 du mois

## maison, Dates:ajd, devise en Ar
* `maMaison` Dates:ajd, posséder immobilier maison valant 100000000Ar, s'appréciant annuellement de 5%
* `venteMaison` Dates:ajd + 2 mois, vente de Immobilier:maison pour 120000000Ar vers Trésoreries:zetyPersonnel
 
## RevenusLoyer, Dates:ajd, devise en Ar
* `receptionLoyer + Dates:ajd` Dates:ajd, entrer 1000000Ar vers Trésoreries:loyerMaison , jusqu'à date indéterminée tous les 29 du mois

## ChargesLoyer, Dates:ajd, devise en Ar
* `paiementCommune + Dates:ajd` Dates:ajd, sortir 200000Ar depuis Trésoreries:loyerMaison, jusqu'à date indéterminée tous les 01 du mois
* `JIRAMA + Dates:ajd` Dates:ajd, sortir 100000Ar depuis Trésoreries:loyerMaison, jusqu'à date indéterminée tous les 01 du mois