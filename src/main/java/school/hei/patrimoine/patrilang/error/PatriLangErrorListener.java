package school.hei.patrimoine.patrilang.error;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class PatriLangErrorListener extends BaseErrorListener {

  private final String fileName;

  public PatriLangErrorListener(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public void syntaxError(
      Recognizer<?, ?> recognizer,
      Object offendingSymbol,
      int line,
      int charPositionInLine,
      String msg,
      RecognitionException e) {

    String errorMessage =
        String.format(
            "Erreur de syntaxe à la ligne %d, colonne %d dans le fichier '%s', raison : %s",
            line, charPositionInLine, fileName, msg);

    throw new ParseCancellationException(errorMessage, e);
  }
}
