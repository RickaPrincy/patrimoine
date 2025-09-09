package school.hei.patrimoine.visualisation.swing.ihm.google.component.files;

import static javax.swing.JOptionPane.showMessageDialog;
import static school.hei.patrimoine.google.api.CommentApi.COMMENTS_CACHE_KEY;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import school.hei.patrimoine.google.api.CommentApi;
import school.hei.patrimoine.google.cache.ApiCache;
import school.hei.patrimoine.google.model.Comment;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.Dialog;
import school.hei.patrimoine.visualisation.swing.ihm.google.component.app.AppContext;

public class CommentSideBar extends JPanel {
  private final Supplier<File> currentFileSupplier;
  private final Supplier<String> currentFileIdSupplier;

  private final ApiCache apiCache;
  private final CommentApi commentApi;
  private final JPanel listContainer;

  public CommentSideBar(
      Supplier<File> currentFileSupplier, Supplier<String> currentFileIdSupplier) {
    super(new BorderLayout());

    this.apiCache = ApiCache.getInstance();
    this.currentFileSupplier = currentFileSupplier;
    this.currentFileIdSupplier = currentFileIdSupplier;
    this.commentApi = new CommentApi(AppContext.getDefault().getData("drive-api"));

    var topPanel = new JPanel(new BorderLayout());
    topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    var title = new JLabel("Commentaires");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
    topPanel.add(title, BorderLayout.WEST);

    // var addCommentBtn = new Button("Ajouter un commentaire");
    // topPanel.add(addCommentBtn, BorderLayout.EAST);

    add(topPanel, BorderLayout.NORTH);

    listContainer = new JPanel(new BorderLayout());
    add(listContainer, BorderLayout.CENTER);
    AppContext.getDefault().setData("comment-api", commentApi);
  }

  public JScrollPane toScrollPane() {
    return new JScrollPane(
        this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
  }

  public void update() {
    SwingWorker<List<Comment>, Void> worker =
        new SwingWorker<>() {
          @Override
          protected List<Comment> doInBackground() throws Exception {
            if (currentFileSupplier.get() == null) {
              return List.of();
            }

            return commentApi.getByFileId(currentFileIdSupplier.get());
          }

          @Override
          protected void done() {
            try {
              var comments = get();
              listContainer.removeAll();

              var contentPanel = new JPanel();
              contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
              contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
              contentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

              if (comments.isEmpty()) {
                var label = new JLabel("Aucun commentaire pour ce fichier");
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                label.setBorder(new EmptyBorder(20, 20, 20, 20));
                contentPanel.add(label);
              } else {
                for (var comment : comments) {
                  var commentCard =
                      new CommentCard(
                          currentFileIdSupplier.get(),
                          comment,
                          (toReply, content) -> sendReply(toReply, content),
                          false);
                  contentPanel.add(commentCard);
                  contentPanel.add(Box.createVerticalStrut(10));
                }
              }

              var scroll =
                  new JScrollPane(
                      contentPanel,
                      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
              scroll.getVerticalScrollBar().setUnitIncrement(16);
              listContainer.add(scroll, BorderLayout.CENTER);

              revalidate();
              repaint();

            } catch (Exception e) {
              showMessageDialog(
                  AppContext.getDefault().app(),
                  "Erreur lors de get comment : " + e.getMessage(),
                  "Erreur",
                  JOptionPane.ERROR_MESSAGE);
            }
          }
        };

    worker.execute();
  }

  private void sendReply(Comment toReply, String newCommentContent) {
    var loading = new Dialog("Envoi en cours...", 300, 100);
    SwingWorker<Void, Void> worker =
        new SwingWorker<>() {
          @Override
          protected Void doInBackground() throws Exception {
            commentApi.reply(currentFileIdSupplier.get(), toReply.id(), newCommentContent);
            return null;
          }

          @Override
          protected void done() {
            refetch();
            loading.dispose();
          }
        };
    worker.execute();
  }

  private void refetch() {
    if (currentFileSupplier.get() != null) {
      this.apiCache.invalidate(COMMENTS_CACHE_KEY, currentFileIdSupplier.get());
    }

    this.update();
  }
}
