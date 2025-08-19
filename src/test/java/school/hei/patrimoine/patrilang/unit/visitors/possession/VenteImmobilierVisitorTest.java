package school.hei.patrimoine.patrilang.unit.visitors.possession;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.IMMOBILIER;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.TRESORERIES;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Immobilier;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.VenteVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

public class VenteImmobilierVisitorTest {

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
    void visit_vente_success_for_immobilier() {

        variableVisitor.addToScope(
                "monCompte",
                TRESORERIES,
                new Compte("monCompte", LocalDate.of(2025, 1, 1), ariary(100_000))
        );


        variableVisitor.addToScope(
                "maMaison",
                IMMOBILIER,
                new Immobilier(
                        "maMaison",
                        LocalDate.of(2020, 1, 1),
                        LocalDate.of(2030, 1, 1),
                        ariary(50_000_000),
                        0.05
                )
        );


        var input =
                "*`ventePossession`, le 01 du 01-2025, vente de Immobilier:maMaison pour 50000000Ar vers Trésoreries:monCompte";


        assertDoesNotThrow(() -> {
            visitor.visit(input, PatriLangParser::vente);
        });
    }
}
