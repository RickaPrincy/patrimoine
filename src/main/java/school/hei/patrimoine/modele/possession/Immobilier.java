package school.hei.patrimoine.modele.possession;

import static java.time.temporal.ChronoUnit.DAYS;
import static school.hei.patrimoine.modele.possession.TypeAgregat.IMMOBILISATION;

import java.time.LocalDate;
import school.hei.patrimoine.modele.Argent;

public final class Immobilier extends Possession {
  private final LocalDate dateAcquisition;
  private final double tauxDAppreciationAnnuelle;

  public Immobilier(
      String nom,
      LocalDate dateAcquisition,
      LocalDate t,
      Argent valeurComptable,
      double tauxDAppreciationAnnuelle) {
    super(nom, t, valeurComptable);
    this.dateAcquisition = dateAcquisition;
    this.tauxDAppreciationAnnuelle = tauxDAppreciationAnnuelle;
  }

  @Override
  public Possession projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateAcquisition)) {
      return new Immobilier(
          nom,
          dateAcquisition,
          tFutur,
          new Argent(0, valeurComptable.devise()),
          tauxDAppreciationAnnuelle);
    }
    var jours = DAYS.between(t, tFutur);
    var ajoutQuotidien = valeurComptable.mult(tauxDAppreciationAnnuelle / 365.);
    var valeurF = valeurComptable.add(ajoutQuotidien.mult(jours), t);
    return new Immobilier(
        nom,
        dateAcquisition,
        tFutur,
        valeurF.lt(0) ? new Argent(0, devise()) : valeurF,
        tauxDAppreciationAnnuelle);
  }

  @Override
  public TypeAgregat typeAgregat() {
    return IMMOBILISATION;
  }
}
