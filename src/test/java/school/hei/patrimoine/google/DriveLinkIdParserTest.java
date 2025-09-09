package school.hei.patrimoine.google;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DriveLinkIdParserTest {

  @Test
  void extract_google_drive_id_from_link_ok() {
    DriveLinkIdParser driveLinkIdParser = new DriveLinkIdParser();
    String validLink = "https://drive.google.com/file/d/1abc2DEF3ghi_456JKL/view?usp=drive_link";

    String result = driveLinkIdParser.apply(validLink);

    assertEquals("1abc2DEF3ghi_456JKL", result);
  }

  @Test
  void extract_google_drive_id_from_link_ko() {
    DriveLinkIdParser driveLinkIdParser = new DriveLinkIdParser();
    String invalidLink = "https://example.com/invalid-link";

    Exception exception =
        assertThrows(RuntimeException.class, () -> driveLinkIdParser.apply(invalidLink));

    assertEquals("Invalid Google Drive Link: " + invalidLink, exception.getMessage());
  }
}
