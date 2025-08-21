package school.hei.patrimoine.patrilang.visitors.possession;

import static java.util.Objects.nonNull;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitMaterielAppreciationFacteur;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.visitText;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Immobilier;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.PossedeImmobilierContext;
import school.hei.patrimoine.patrilang.modele.variable.VariableType;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class ImmobilierVisitor implements SimpleVisitor<PossedeImmobilierContext, Immobilier> {
  private final VariableVisitor variableVisitor;

  @Override
  public Immobilier apply(PossedeImmobilierContext ctx) {
    String nom = visitText(ctx.immobilierNom);
    double facteur = visitMaterielAppreciationFacteur(ctx.MATERIEL_APPRECIATION());
    double taux = variableVisitor.asNombre(ctx.pourcentageAppreciation);
    Argent valeur = variableVisitor.asArgent(ctx.valeurComptable);
    LocalDate t = variableVisitor.asDate(ctx.dateValue);
    LocalDate dateAcq = nonNull(ctx.dateObtention) ? variableVisitor.asDate(ctx.dateObtention) : t;

    var bien = new Immobilier(nom, dateAcq, t, valeur, (taux / 100) * facteur);

    variableVisitor.addToScope(nom, VariableType.IMMOBILIER, bien);
    return bien;
  }
}
