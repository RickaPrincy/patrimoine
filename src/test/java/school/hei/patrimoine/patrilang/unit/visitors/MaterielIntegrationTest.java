package school.hei.patrimoine.patrilang.unit.visitors;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.utils.UnitTestVisitor;
import school.hei.patrimoine.patrilang.visitors.possession.MaterielVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;
import school.hei.patrimoine.patrilang.modele.variable.Variable;

class MaterielIntegrationTest {

    private static final LocalDate AJD = LocalDate.of(2025, 6, 23);

    private VariableVisitor variableVisitor;
    private MaterielVisitor materielVisitor;

    private UnitTestVisitor variableParserVisitor;
    private UnitTestVisitor possedeMaterielVisitor;

    @BeforeEach
    void setUp() {
        variableVisitor = new VariableVisitor();
        materielVisitor = new MaterielVisitor(variableVisitor);

        variableParserVisitor = new UnitTestVisitor() {
            @Override
            public Variable<?> visitVariable(VariableContext ctx) {
                return variableVisitor.apply(ctx);
            }
        };

        possedeMaterielVisitor = new UnitTestVisitor() {
            @Override
            public Materiel visitPossedeMateriel(PossedeMaterielContext ctx) {
                return materielVisitor.apply(ctx);
            }
        };
        variableVisitor.addToScope("ajd", DATE, AJD);
    }

    @Test
    void can_store_materiel_and_reference_it_via_parser() {
        var inputPossede =
                """
                * `possèdeMateriel`, Dates:ajd posséder ordinateur1 valant 200000Ar, s'appréciant annuellement de 1%
                """;

        Materiel expected = new Materiel("ordinateur1", AJD, AJD, ariary(200_000), 0.01);
        Materiel actual = possedeMaterielVisitor.visit(inputPossede, PatriLangParser::possedeMateriel);

        assertEquals(expected.nom(), actual.nom());
        assertEquals(expected.valeurComptable(), actual.valeurComptable());

        Variable<?> retrieved = variableVisitor.getVariableScope().get("ordinateur1", MATERIEL);
        assertNotNull(retrieved);
        assertSame(actual, retrieved.value());

        var inputVariable = "Materiel:ordinateur1";
        var fromVariable = (Variable<Materiel>) variableParserVisitor.visit(inputVariable, PatriLangParser::variable);

        assertNotNull(fromVariable);
        assertSame(actual, fromVariable.value(), "On doit récupérer exactement le même objet que celui du scope");
    }
}
