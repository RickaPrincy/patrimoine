package school.hei.patrimoine.visualisation.swing.ihm.google.modele;

import static school.hei.patrimoine.visualisation.swing.ihm.google.modele.GoogleLinkList.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.compiler.FileNameExtractor;
import school.hei.patrimoine.compiler.PatriLangFileNameExtractor;
import school.hei.patrimoine.google.GoogleApiUtilities;
import school.hei.patrimoine.google.api.DriveApi;
import school.hei.patrimoine.google.exception.GoogleIntegrationException;

@Slf4j
public record GoogleLinkListDownloader(FileNameExtractor fileNameExtractor, DriveApi driveApi) {
  public GoogleLinkListDownloader(DriveApi driveApi) {
    this(new PatriLangFileNameExtractor(), driveApi);
  }

  public void download(GoogleLinkList<NamedID> ids) throws GoogleIntegrationException {
    for (var namedId : ids.planned()) {
      driveApi.download(namedId.id(), fileNameExtractor, getPlannedDirectoryPath());
    }

    for (var namedId : ids.done()) {
      driveApi.download(namedId.id(), fileNameExtractor, getDoneDirectoryPath());
    }
  }

  public static String getPlannedDirectoryPath() {
    return GoogleApiUtilities.getDownloadDirectoryPath() + "/planifies";
  }

  public static String getDoneDirectoryPath() {
    return GoogleApiUtilities.getDownloadDirectoryPath() + "/realises";
  }

  public static void setup() {
    resetDirectory(getPlannedDirectoryPath());
    resetDirectory(getDoneDirectoryPath());
  }

  @SuppressWarnings("all")
  private static void resetDirectory(String directoryPath) {
    var path = Paths.get(directoryPath);
    var directory = path.toFile();

    try {
      if (directory.exists()) {
        Files.walk(path).map(Path::toFile).forEach(File::delete);
      }
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new RuntimeException("Directory reset error : " + directoryPath, e);
    }
  }
}
