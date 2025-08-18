package school.hei.patrimoine.modele.possession;

import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.possession.TypeAgregat.TRESORERIE;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;
import school.hei.patrimoine.modele.Argent;

@ToString(callSuper = true)
@Getter
public sealed class Compte extends Possession permits Dette, Creance {
  private final LocalDate dateOuverture;
  private final Set<FluxArgent> fluxArgents;

  public Compte(String nom, LocalDate t, Argent valeurComptable) {
    this(nom, t, t, valeurComptable);
  }

  public Compte(
      String nom,
      LocalDate t,
      Argent valeurComptable,
      Map<LocalDate, Argent> historiqueValeurMarche) {
    this(nom, t, t, valeurComptable);
    this.historiqueValeurMarche = historiqueValeurMarche;
  }

  public Compte(
      String nom,
      LocalDate dateOuverture,
      LocalDate t,
      Argent valeurComptable,
      Set<FluxArgent> fluxArgents) {
    super(nom, t, valeurComptable);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
  }

  public Compte(
      String nom,
      LocalDate dateOuverture,
      LocalDate t,
      Argent valeurComptable,
      Set<FluxArgent> fluxArgents,
      Map<LocalDate, Argent> historiqueValeurMarche) {
    super(nom, t, valeurComptable);
    this.fluxArgents = fluxArgents;
    this.dateOuverture = dateOuverture;
    this.historiqueValeurMarche = historiqueValeurMarche;
  }

  public Compte(String nom, LocalDate dateOuverture, LocalDate t, Argent valeurComptable) {
    this(nom, dateOuverture, t, valeurComptable, new HashSet<>(), new HashMap<>());
  }

  @Override
  public Compte projectionFuture(LocalDate tFutur) {
    if (tFutur.isBefore(dateOuverture)) {
      return new Compte(
          nom, tFutur, new Argent(0, valeurComptable.devise()), getHistoriqueValeurMarche());
    }

    return new Compte(
        nom,
        dateOuverture,
        tFutur,
        valeurComptable.minus(financementsFuturs(tFutur), tFutur),
        fluxArgents.stream().map(f -> f.projectionFuture(tFutur)).collect(toSet()),
        getHistoriqueValeurMarche());
  }

  @Override
  public TypeAgregat typeAgregat() {
    return TRESORERIE;
  }

  private Argent financementsFuturs(LocalDate tFutur) {
    return fluxArgents.stream()
        .map(
            f ->
                valeurComptable.minus(
                    f.projectionFuture(tFutur).getCompte().valeurComptable(), tFutur))
        .reduce(new Argent(0, valeurComptable.devise()), (a1, a2) -> a1.add(a2, tFutur));
  }

  public void addFinancés(FluxArgent fluxArgent) {
    fluxArgents.add(fluxArgent);
  }

  @Override
  public Argent valeurActuelle(LocalDate date) {
    return projectionFuture(date).valeurComptable();
  }

  @Override
  public Argent valeurActuelle() {
    return valeurActuelle(LocalDate.now());
  }
}
