package school.hei.patrimoine.cas.pro3;

import static java.time.Month.*;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;

@Slf4j
public class PatrimoineDeBakoTest {
  private Patrimoine subject = new PatrimoineDeBako().get();

  @Test
  void patrimoine_de_bako_au_31_decembre_2025() {
    var valeurPatrimoineDeBakoAu31Decembre2025 =
        subject.projectionFuture(LocalDate.of(2025, DECEMBER, 31)).getValeurComptable().ppMontant();

    log.info(valeurPatrimoineDeBakoAu31Decembre2025);
  }
}
