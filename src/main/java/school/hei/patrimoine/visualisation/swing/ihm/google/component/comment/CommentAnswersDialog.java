package school.hei.patrimoine.visualisation.swing.ihm.google.component.comment;

import static school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentCard.replyButton;
import static school.hei.patrimoine.visualisation.swing.ihm.google.component.comment.CommentCard.resolveButton;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.button.Button;

public class CommentAnswersDialog extends Dialog {
  private final String fileId;
  private final Comment parentComment;
  private final CommentListPanel commentListPanel;
  private final Runnable refresh;

  public CommentAnswersDialog(String fileId, Comment parentComment, Runnable refreshParent) {
    super("Réponses au commentaire", 800, 500, false);
    this.fileId = fileId;
    this.refresh =
        () -> {
          dispose();
          refreshParent.run();
        };

    this.parentComment = parentComment;
    this.commentListPanel = new CommentListPanel(this, false, this::dispose);

    setModal(true);
    setLayout(new BorderLayout());

    addCommentList();
    addActions();

    pack();
    setVisible(true);
  }

  private void addCommentList() {
    add(commentListPanel.toScrollPane(), BorderLayout.CENTER);
    update();
  }

  private void addActions() {
    var buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(replyButton(fileId, parentComment, refresh));
    buttonPanel.add(resolveButton(fileId, parentComment, refresh));
    buttonPanel.add(new Button("Fermer", e -> dispose()));
    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void update() {
    var comments = new ArrayList<>(List.of(parentComment));
    comments.addAll(parentComment.answers());

    commentListPanel.update(fileId, comments);
  }
}
