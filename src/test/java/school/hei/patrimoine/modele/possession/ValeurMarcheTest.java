package school.hei.patrimoine.modele.possession;

import static java.time.Month.JUNE;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.vente.ValeurMarche;

class ValeurMarcheTest {

  @Test
  void valeurMarche_peut_etre_attribuee_a_un_materiel() {
    assertDoesNotThrow(
        () ->
            new ValeurMarche(
                new Materiel(
                    "Ordinateur",
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2023, 1, 1),
                    new Argent(1_000, Devise.MGA),
                    0.05),
                LocalDate.of(2023, 6, 1),
                new Argent(900, Devise.MGA)));
  }

  @Disabled
  @Test
  void valeurMarche_ne_peut_pas_etre_attribuee_a_un_compte() {
    Executable executable =
        () ->
            new ValeurMarche(
                new Compte(
                    "Compte courant",
                    LocalDate.of(2023, 1, 1),
                    LocalDate.of(2023, 1, 1),
                    new Argent(2_000, Devise.MGA)),
                LocalDate.of(2023, 6, 1),
                new Argent(2_500, Devise.MGA));

    assertThrows(IllegalArgumentException.class, executable);
  }

  @Test
  void valeur_marchee_futur_should_return_correct_value() {
    var materiel =
        new Materiel(
            "Ordinateur", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1), ariary(300), 0.05);
    var date = LocalDate.of(2025, JUNE, 1);
    var valeurMarchee = ariary(500);

    assertDoesNotThrow(() -> new ValeurMarche(materiel, date, valeurMarchee));

    assertEquals(valeurMarchee, materiel.valeurMarcheeFutur(date.plusDays(1)));
  }
}
