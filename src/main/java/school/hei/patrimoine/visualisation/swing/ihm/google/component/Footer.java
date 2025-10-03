package school.hei.patrimoine.visualisation.swing.ihm.google.component;

import java.awt.*;
import java.util.stream.IntStream;
import javax.swing.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.modele.State;

public class Footer extends JPanel {
  private final State state;

  private final Button previousPageButton;
  private final JComboBox<Integer> pageSelector;
  private final Button nextPageButton;

  public Footer(State state) {
    this.state = state;

    setLayout(new FlowLayout(FlowLayout.RIGHT));
    setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

    previousPageButton = new Button("Précédente");
    previousPageButton.addActionListener(e -> goToPreviousPage());
    pageSelector = new JComboBox<>();
    nextPageButton = new Button("Suivante");
    nextPageButton.addActionListener(e -> goToNextPage());

    add(previousPageButton);
    add(pageSelector);
    add(nextPageButton);

    // Initialisation de la pagination
    state.update("currentPage", 1);
    state.update("totalPages", 1);

    // Synchroniser affichage avec le state
    state.subscribe(java.util.Set.of("currentPage", "totalPages"), () -> updatePageSelector());

    pageSelector.addActionListener(
        e -> {
          if (pageSelector.getSelectedItem() != null) {
            int selectedPage = (Integer) pageSelector.getSelectedItem();
            state.update("currentPage", selectedPage);
          }
        });
  }

  private void goToPreviousPage() {
    int current = (int) state.get("currentPage");
    if (current > 1) {
      state.update("currentPage", current - 1);
    }
  }

  private void goToNextPage() {
    int current = (int) state.get("currentPage");
    int total = (int) state.get("totalPages");
    if (current < total) {
      state.update("currentPage", current + 1);
    }
  }

  private void updatePageSelector() {
    int totalPages = (int) state.get("totalPages");
    int currentPage = (int) state.get("currentPage");

    pageSelector.removeAllItems();
    IntStream.rangeClosed(1, totalPages).forEach(pageSelector::addItem);
    pageSelector.setSelectedItem(currentPage);

    previousPageButton.setEnabled(currentPage > 1);
    nextPageButton.setEnabled(currentPage < totalPages);
  }
}
