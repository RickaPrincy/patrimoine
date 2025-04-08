package school.hei.patrimoine.cas.pro3;

import static java.time.Month.*;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;

@Slf4j
public class PatrimoineDeTianaTest {
  private Patrimoine subject = new PatrimoineDeTiana().get();

  @Test
  void patrimoine_de_bako_au_31_decembre_2025() {
    var valeurPatrimoineDeTianaAu31Decembre2025 =
        subject.projectionFuture(LocalDate.of(2026, MARCH, 31)).getValeurComptable().ppMontant();

    log.info(valeurPatrimoineDeTianaAu31Decembre2025);
  }
}
