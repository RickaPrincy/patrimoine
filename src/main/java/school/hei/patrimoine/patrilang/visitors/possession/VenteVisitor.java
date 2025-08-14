package school.hei.patrimoine.patrilang.visitors.possession;

import lombok.RequiredArgsConstructor;
import school.hei.patrimoine.modele.possession.Vente;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.VenteContext;
import school.hei.patrimoine.patrilang.visitors.SimpleVisitor;
import school.hei.patrimoine.patrilang.visitors.variable.VariableVisitor;

@RequiredArgsConstructor
public class VenteVisitor implements SimpleVisitor<VenteContext, Vente> {
  private final VariableVisitor variableVisitor;

  @Override
  public Vente apply(VenteContext ctx) {
    var possessionAVendre = variableVisitor.asPossession(ctx.possessionAVendre);
    var t = variableVisitor.asDate(ctx.dateDeVente);
    var valeurDeVente = variableVisitor.asArgent(ctx.prixDeVente);
    var dateDeVente = variableVisitor.asDate(ctx.dateDeVente);
    var compteBeneficiaire = variableVisitor.asCompte(ctx.compteBeneficiaire);

    return new Vente(t, possessionAVendre, valeurDeVente, dateDeVente, compteBeneficiaire);
  }
}
