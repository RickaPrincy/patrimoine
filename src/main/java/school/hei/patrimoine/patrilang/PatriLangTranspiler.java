package school.hei.patrimoine.patrilang;

import static school.hei.patrimoine.patrilang.PatriLangParser.parseCas;

import java.nio.file.Paths;
import java.util.function.Function;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.patrilang.visitors.factory.SectionVisitorFactory;
import school.hei.patrimoine.patrilang.visitors.PatriLangCasVisitor;
import school.hei.patrimoine.patrilang.visitors.PatriLangToutCasVisitor;
import school.hei.patrimoine.patrilang.visitors.PatriLangVisitor;
import school.hei.patrimoine.patrilang.visitors.SectionVisitor;

public class PatriLangTranspiler implements Function<String, CasSet> {
  public static final String CAS_FILE_EXTENSION = ".cas.md";
  public static final String TOUT_CAS_FILE_EXTENSION = ".tout.md";

  @Override
  public CasSet apply(String casSetFilePath) {
    return transpileToutCas(casSetFilePath);
  }

  public static Cas transpileCas(String casName, SectionVisitor sectionVisitor) {
    var casPath =
        Paths.get(sectionVisitor.getCasSetFolderPath())
            .resolve(casName + CAS_FILE_EXTENSION)
            .toAbsolutePath();
    var tree = parseCas(casPath.toString());
    var patrilangVisitor = new PatriLangVisitor(null, new PatriLangCasVisitor(sectionVisitor));
    return patrilangVisitor.visitCas(tree);
  }

  public static CasSet transpileToutCas(String casSetPath) {
    var tree = PatriLangParser.parseToutCas(casSetPath);
    var casSetFolderPath = Paths.get(casSetPath).getParent().toAbsolutePath();
    var sectionVisitor = SectionVisitorFactory.make(casSetFolderPath.toString());
    var patrilangVisitor = new PatriLangVisitor(new PatriLangToutCasVisitor(sectionVisitor), null);

    return patrilangVisitor.visitToutCas(tree);
  }
}
