package school.hei.patrimoine.patrilang;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class PatriLangErrorListenerTest {

  private static final String VALID_RESOURCE_PATH = "/famille_rakoto_cas/Locationmaison.cas.md";
  private static final String INVALID_RESOURCE_PATH =
      "/famille_rakoto_cas/LocationMaisonInvalide.cas.md";

  private static final Path VALID_FILE_PATH;
  private static final Path INVALID_FILE_PATH;

  static {
    try {
      var validUri =
          requireNonNull(PatriLangErrorListenerTest.class.getResource(VALID_RESOURCE_PATH)).toURI();
      VALID_FILE_PATH = Paths.get(validUri);

      var invalidUri =
          requireNonNull(PatriLangErrorListenerTest.class.getResource(INVALID_RESOURCE_PATH))
              .toURI();
      INVALID_FILE_PATH = Paths.get(invalidUri);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testParsingValide_neDoitPasLancerException() {
    assertDoesNotThrow(
        () -> {
          var tree = PatriLangTranspiler.parseAsTree(VALID_FILE_PATH.toString());
          assertNotNull(tree);
        });
  }

  @Test
  public void testParsingInvalide_devraitEchouer() {
    var thrown =
        assertThrows(
            RuntimeException.class,
            () -> {
              PatriLangTranspiler.parseAsTree(INVALID_FILE_PATH.toString());
            });

    var expectedMessage =
        String.format(
            "Erreur de syntaxe à la ligne 8, colonne 26 dans le fichier '%s', raison : missing ')'"
                + " at '+'",
            INVALID_FILE_PATH.toString());

    assertEquals(expectedMessage, thrown.getMessage());
  }
}
