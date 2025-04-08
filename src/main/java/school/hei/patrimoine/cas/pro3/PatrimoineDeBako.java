package school.hei.patrimoine.cas.pro3;

import static java.time.Month.*;
import static school.hei.patrimoine.modele.Argent.*;
import static school.hei.patrimoine.modele.Devise.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.possession.TransfertArgent;

public class PatrimoineDeBako implements Supplier<Patrimoine> {
  private static final LocalDate AU_8_AVRIL_2025 = LocalDate.of(2025, APRIL, 8);

  @Override
  public Patrimoine get() {
    return Patrimoine.of(
        "Bako Patrimoine",
        MGA,
        AU_8_AVRIL_2025,
        new Personne("Bako"),
        bakoPossessionsAu8Avril2025());
  }

  private static Set<Possession> bakoPossessionsAu8Avril2025() {
    var bni = new Compte("BNI", AU_8_AVRIL_2025, ariary(2_000_000));

    var bmoi = new Compte("BMOI", AU_8_AVRIL_2025, ariary(625_000));

    var coffreFort = new Compte("Coffre fort", AU_8_AVRIL_2025, ariary(1_750_000));

    var salaireMensuel =
        new FluxArgent(
            "Salaire Mensuel", bni, AU_8_AVRIL_2025, LocalDate.MAX, 2, ariary(2_125_000));

    var epargneAu3DuMois =
        new TransfertArgent(
            "Transfert Epargne", bni, bmoi, AU_8_AVRIL_2025, LocalDate.MAX, 3, ariary(200_000));

    var colocation =
        new FluxArgent("Colocation", bni, AU_8_AVRIL_2025, LocalDate.MAX, 26, ariary(-600_000));

    var trainDeVie =
        new FluxArgent("Train De Vie", bni, AU_8_AVRIL_2025, LocalDate.MAX, 1, ariary(-700_000));

    var ordinateur =
        new Materiel("Ordinateur", AU_8_AVRIL_2025, AU_8_AVRIL_2025, ariary(3_000_000), -0.12);

    return Set.of(
        bni,
        bmoi,
        coffreFort,
        salaireMensuel,
        epargneAu3DuMois,
        colocation,
        trainDeVie,
        ordinateur);
  }
}
