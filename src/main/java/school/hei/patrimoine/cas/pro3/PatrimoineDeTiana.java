package school.hei.patrimoine.cas.pro3;

import static java.time.Month.APRIL;
import static java.time.Month.AUGUST;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static java.time.Month.JULY;
import static java.time.Month.JUNE;
import static java.time.Month.MAY;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.Devise.MGA;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.TransfertArgent;

public class PatrimoineDeTiana implements Supplier<Patrimoine> {
  private static final LocalDate AU_8_AVRIL_2025 = LocalDate.of(2025, APRIL, 8);
  private static final LocalDate AU_27_JUILLET_2025 = LocalDate.of(2025, JULY, 27);

  @Override
  public Patrimoine get() {
    return Patrimoine.of(
        "Patrimoine de Tiana",
        MGA,
        AU_8_AVRIL_2025,
        new Personne("Tiana"),
        tianaPossessionAu8Avril2025());
  }

  private static Set<Possession> tianaPossessionAu8Avril2025() {
    var compteBancaire = new Compte("Compte bancaire", AU_8_AVRIL_2025, ariary(60_000_000));

    var terrainBati =
        new Materiel("Terrain bâti", AU_8_AVRIL_2025, AU_8_AVRIL_2025, ariary(100_000_000), 0.10);

    var depenseMensuel =
        new FluxArgent(
            "Dépense emensuel",
            compteBancaire,
            AU_8_AVRIL_2025,
            LocalDate.MAX,
            1,
            ariary(-4_000_000));

    var projetInvestissement =
        new FluxArgent(
            "Projet Investissement",
            compteBancaire,
            LocalDate.of(2025, JUNE, 1),
            LocalDate.of(2025, DECEMBER, 31),
            5,
            ariary(-5_000_000));

    var salaire10Pourcent =
        new FluxArgent("10%", compteBancaire, LocalDate.of(2025, MAY, 1), ariary(7_000_000));

    var salaire90Pourcent =
        new FluxArgent("90%", compteBancaire, LocalDate.of(2026, JANUARY, 31), ariary(63_000_000));

    var detteBancaire =
        new FluxArgent(
            "Dette de la banque", compteBancaire, AU_27_JUILLET_2025, ariary(20_000_000));

    var detteDeTiana = new Dette("Dette issue du prêt", AU_27_JUILLET_2025, ariary(-24_000_000));

    var remboursementDeLaDette =
        new TransfertArgent(
            "Remboursement de la dette",
            compteBancaire,
            detteDeTiana,
            LocalDate.of(2025, AUGUST, 27),
            LocalDate.of(2026, JULY, 27),
            27,
            ariary(2_000_000));

    return Set.of(
        compteBancaire,
        terrainBati,
        depenseMensuel,
        projetInvestissement,
        salaire10Pourcent,
        salaire90Pourcent,
        detteBancaire,
        detteDeTiana,
        remboursementDeLaDette);
  }
}
