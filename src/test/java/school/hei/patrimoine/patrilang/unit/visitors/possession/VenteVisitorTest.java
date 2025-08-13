package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.MATERIEL;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class VenteVisitorTest {
  VariableVisitor variableVisitor = new VariableVisitor();
  VenteVisitor subject = new VenteVisitor(variableVisitor);

  UnitTestVisitor visitor =
      new UnitTestVisitor() {
        @Override
        public Vente visitVente(PatriLangParser.VenteContext ctx) {
          return subject.apply(ctx);
        }
      };

  @Test
  void visit_success() {
    variableVisitor.addToScope(
        "monCompte",
        TRESORERIES,
        new Compte("monCompte", LocalDate.of(2025, 1, 1), ariary(100_000)));
    variableVisitor.addToScope(
        "donut",
        MATERIEL,
        new Materiel("donut", LocalDate.of(2025, 1, 1), LocalDate.now(), ariary(5_000), 0)
    );

    var input =
        "*`ventePossession`, le 01 du 01-2025, vente de Trésoreries:monCompte pour 300000Ar vers"
            + " Trésoreries:monCompte";

    assertDoesNotThrow(() -> {
      visitor.visit(input, PatriLangParser::vente);
    });
  }
}
