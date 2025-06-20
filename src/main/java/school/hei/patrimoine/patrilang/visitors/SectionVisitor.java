package school.hei.patrimoine.patrilang.visitors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.patrilang.PatriLangTranspiler.transpileCas;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.modele.variable.VariableType.*;
import static school.hei.patrimoine.patrilang.visitors.BaseVisitor.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;
import school.hei.patrimoine.patrilang.factory.SectionVisitorFactory;
import school.hei.patrimoine.patrilang.modele.variable.VariableType;
import school.hei.patrimoine.patrilang.visitors.possession.*;

@Builder
@Getter
public class SectionVisitor {
  private final String casSetFolderPath;
  private final VariableVisitor variableVisitor;
  private final DateVisitor dateVisitor;
  private final ArgentVisitor argentVisitor;
  private final CompteVisitor compteVisitor;
  private final ExpressionVisitor expressionVisitor;
  private final ObjectifVisitor objectifVisitor;
  private final CreanceVisitor creanceVisitor;
  private final DetteVisitor detteVisitor;
  private final MaterielVisitor materielVisitor;
  private final AchatMaterielVisitor achatMaterielVisitor;
  private final FluxArgentVisitor fluxArgentVisitor;
  private final TransferArgentVisitor transferArgentVisitor;
  private final CorrectionVisitor correctionVisitor;
  private final GroupPossessionVisitor groupPossessionVisitor;

  public Set<Cas> visitSectionCas(SectionCasContext ctx) {
    var newSectionVisitor =
        SectionVisitorFactory.make(
            this.casSetFolderPath, Optional.of(this.variableVisitor.getVariableScope()));
    return ctx.ligneNom().stream()
        .map(ligne -> visitText(ligne.nom))
        .map(nom -> transpileCas(nom, newSectionVisitor))
        .collect(toSet());
  }

  public void visitSectionDatesDeclarations(SectionDatesDeclarationsContext ctx) {
    ctx.ligneDateDeclaration()
        .forEach(
            ligne ->
                this.variableVisitor.addToScope(
                    visitText(ligne.nom), DATE, this.dateVisitor.apply(ligne.dateValue)));
  }

  public void visitSectionPersonnesDeclarations(SectionPersonnesDeclarationsContext ctx) {
    ctx.ligneNom()
        .forEach(
            ligne -> {
              var personne = visitPersonne(ligne.nom);
              this.variableVisitor.addToScope(personne.nom(), PERSONNE, personne);
            });
  }

  public void visitSectionInitialisation(SectionInitialisationContext ctx) {
    ctx.operations().forEach(this::visitOperations);
  }

  public void visitSectionSuivi(SectionSuiviContext ctx) {
    ctx.operations().forEach(this::visitOperations);
  }

  public Map<Personne, Double> visitSectionPossesseurs(SectionPossesseursContext ctx) {
    return ctx.lignePossesseur().stream()
        .collect(
            toMap(
                ligne -> variableVisitor.asPersonne(ligne.nom),
                ligne -> visitNombre(ligne.pourcentage) / 100));
  }

  public Set<Compte> visitSectionTrésoreries(SectionTresoreriesContext ctx) {
    return visitCompteElements(
        TRESORERIES, ctx.compteElement(), this.compteVisitor, this.variableVisitor::asCompte);
  }

  public Set<Creance> visitSectionCréances(SectionCreancesContext ctx) {
    return visitCompteElements(
        CREANCE, ctx.compteElement(), this.creanceVisitor, this.variableVisitor::asCreance);
  }

  public Set<Dette> visitSectionDettes(SectionDettesContext ctx) {
    return visitCompteElements(
        DETTE, ctx.compteElement(), this.detteVisitor, this.variableVisitor::asDette);
  }

  public Set<Possession> visitSectionOperations(SectionOperationsContext ctx) {
    return ctx.operations().stream().flatMap(op -> visitOperations(op).stream()).collect(toSet());
  }

  private Set<Possession> visitOperations(OperationsContext ctx) {
    var possessions =
        ctx.operation().stream()
            .map(this::visitOperation)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toSet());

    if (isNull(ctx.sousTitre())) {
      return possessions;
    }

    return Set.of(this.groupPossessionVisitor.apply(ctx.sousTitre(), possessions));
  }

  private Optional<Possession> visitOperation(OperationContext ctx) {
    if (nonNull(ctx.fluxArgentTransferer())) {
      return Optional.of(this.transferArgentVisitor.apply(ctx.fluxArgentTransferer()));
    }

    if (nonNull(ctx.fluxArgentEntrer())) {
      return Optional.of(this.fluxArgentVisitor.apply(ctx.fluxArgentEntrer()));
    }

    if (nonNull(ctx.fluxArgentSortir())) {
      return Optional.of(this.fluxArgentVisitor.apply(ctx.fluxArgentSortir()));
    }

    if (nonNull(ctx.acheterMateriel())) {
      return Optional.of(this.achatMaterielVisitor.apply(ctx.acheterMateriel()));
    }

    if (nonNull(ctx.possedeMateriel())) {
      return Optional.of(this.materielVisitor.apply(ctx.possedeMateriel()));
    }

    if (nonNull(ctx.correction())) {
      return Optional.of(this.correctionVisitor.apply(ctx.correction()));
    }

    if (nonNull(ctx.objectif())) {
      this.objectifVisitor.apply(ctx.objectif());
      return Optional.empty();
    }

    throw new IllegalArgumentException("Opération inconnue");
  }

  private <T extends Possession> Set<T> visitCompteElements(
      VariableType type,
      List<CompteElementContext> elements,
      SimpleVisitor<CompteContext, T> visitor,
      SimpleVisitor<VariableContext, T> variableGetter) {
    return elements.stream()
        .map(
            element -> {
              if (isNull(element.compte())) {
                return variableGetter.apply(element.variable());
              }

              var value = visitor.apply(element.compte());
              this.variableVisitor.addToScope(value.nom(), type, value);
              return value;
            })
        .collect(toSet());
  }
}
