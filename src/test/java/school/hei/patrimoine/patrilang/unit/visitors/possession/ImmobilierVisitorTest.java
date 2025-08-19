package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.IMMOBILIER;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Immobilier;
import school.hei.patrimoine.modele.vente.ValeurMarchee;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.ValeurMarcheeVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

class ImmobilierVisitorTest {
  private static final VariableVisitor variableVisitor = new VariableVisitor();
  private static final ValeurMarcheeVisitor subject = new ValeurMarcheeVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public ValeurMarchee visitValeurMarchee(PatriLangParser.ValeurMarcheeContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void visit_valeur_marchee_success_for_immobilier() {

    variableVisitor.addToScope(
        "maMaison",
        IMMOBILIER,
        new Immobilier(
            "maMaison",
            LocalDate.of(2020, 1, 1),
            LocalDate.of(2030, 1, 1),
            ariary(50_000_000),
            0.05));

    var input =
        "*`valeurMarchée`, le 01 du 01-2025, valeur marchée de 12_000_000Ar pour"
            + " Immobilier:maMaison";

    assertDoesNotThrow(
        () -> {
          visitor.visit(input, PatriLangParser::valeurMarchee);
        });
  }
}
