package school.hei.patrimoine.modele.possession;

import static school.hei.patrimoine.modele.possession.TypeAgregat.ENTREPRISE;
import static school.hei.patrimoine.modele.possession.TypeAgregat.IMMOBILISATION;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.objectif.Objectivable;
import school.hei.patrimoine.modele.vente.Vendable;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public abstract sealed class Possession extends Objectivable
    implements Serializable, Vendable /*note(no-serializable)*/
    permits AchatMaterielAuComptant,
        Compte,
        CompteCorrection,
        Correction,
        FluxArgent,
        GroupePossession,
        PatrimoinePersonnel,
        PersonneMorale,
        RemboursementDette,
        Materiel,
        TransfertArgent {
  protected final String nom;
  protected final LocalDate t;
  protected final Argent valeurComptable;
  @EqualsAndHashCode.Exclude @ToString.Exclude private CompteCorrection compteCorrection;

  protected LocalDate dateVente;
  protected Argent prixVente;
  protected Compte compteBeneficiaire;
  protected Map<LocalDate, Argent> historiqueValeurMarche = new HashMap<>();

  public Possession(String nom, LocalDate t, Argent valeurComptable) {
    super();
    this.nom = nom;
    this.t = t;
    this.valeurComptable = valeurComptable;
  }

  public CompteCorrection getCompteCorrection() {
    if (compteCorrection == null) {
      compteCorrection = new CompteCorrection(nom, valeurComptable.devise());
    }
    return compteCorrection;
  }

  public Argent valeurComptable() {
    return valeurComptable;
  }

  public void addValeurMarche(LocalDate date, Argent valeur) {
    if (typeAgregat() != IMMOBILISATION && typeAgregat() != ENTREPRISE) {
      throw new IllegalArgumentException(
          "Seules les possessions de type IMMOBILISATION ou ENTREPRISE peuvent avoir une valeur de"
              + " marché.");
    }
    historiqueValeurMarche.put(date, valeur);
  }

  public Argent valeurMarche() {
    if (estVendue(t)) {
      return new Argent(0, valeurComptable.devise());
    }

    return historiqueValeurMarche.entrySet().stream()
        .filter(e -> !e.getKey().isAfter(t))
        .max(Map.Entry.comparingByKey())
        .map(Map.Entry::getValue)
        .orElseGet(this::getValeurComptable);
  }

  public final Devise devise() {
    return valeurComptable.devise();
  }

  public Argent valeurMarcheeFutur(LocalDate date) {
    return projectionFuture(date).valeurMarche();
  }

  public final Argent valeurComptableFuture(LocalDate tFutur) {
    return projectionFuture(tFutur).valeurComptable();
  }

  public abstract Possession projectionFuture(LocalDate tFutur);

  public abstract TypeAgregat typeAgregat();

  @Override
  public String nom() {
    return nom;
  }

  @Override
  public Argent valeurAObjectifT(LocalDate t) {
    return projectionFuture(t).valeurComptable;
  }

  public Argent valeurActuelle(LocalDate date) {
    if (dateVente != null && (date.isEqual(dateVente) || date.isAfter(dateVente))) {
      return new Argent(0, valeurComptable.devise());
    }
    return valeurComptable;
  }

  public Argent valeurActuelle() {
    return valeurActuelle(LocalDate.now());
  }

  @Override
  public void vendre(LocalDate date, Argent prix, Compte compteBeneficiaire) {
    if (this.dateVente != null) {
      throw new IllegalStateException(
          "La possession '" + nom + "' est déjà vendue le " + dateVente);
    }
    if (date.isBefore(t)) {
      throw new IllegalArgumentException(
          "La date de vente ne peut pas être antérieure à l'acquisition (" + t + ")");
    }
    this.dateVente = date;
    this.prixVente = prix;
    this.compteBeneficiaire = compteBeneficiaire;
    var flux = new FluxArgent("Produit de vente de '" + nom + "'", compteBeneficiaire, date, prix);

    compteBeneficiaire.addFinancés(flux);
    historiqueValeurMarche.put(date, new Argent(0, prix.devise()));
  }

  @Override
  public boolean estVendue() {
    return dateVente != null;
  }

  @Override
  public boolean estVendue(LocalDate date) {
    return dateVente != null && !date.isBefore(dateVente);
  }

  @Override
  public LocalDate getDateVente() {
    return dateVente;
  }

  @Override
  public Argent getPrixVente() {
    return prixVente;
  }
}
