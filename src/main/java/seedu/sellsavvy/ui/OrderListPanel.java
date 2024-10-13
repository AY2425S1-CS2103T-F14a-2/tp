package seedu.sellsavvy.ui;

import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.sellsavvy.commons.core.LogsCenter;
import seedu.sellsavvy.logic.commands.personcommands.AddPersonCommand;
import seedu.sellsavvy.model.order.Order;
import seedu.sellsavvy.model.person.Person;

/**
 * Panel containing the list of orders of a person.
 */
public class OrderListPanel extends UiPart<Region> {
    private static final String FXML = "OrderListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(OrderListPanel.class);
    private static final String DEFAULT_TITLE = "Order";
    private static final String TITLE_WITH_SELECTED_PERSON = "Order( %1$s )";

    @FXML
    private ListView<Order> orderListView;
    @FXML
    private Label orderGuide;
    @FXML
    private Label orderListTitle;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public OrderListPanel(ReadOnlyObjectProperty<Person> selectedPerson) {
        super(FXML);
        orderGuide.setText("Use one of the following commands below to view order:\n" + "1. " + AddPersonCommand.MESSAGE_USAGE);
        updateOrderList(selectedPerson.get());
        selectedPerson.addListener(((observable, oldPerson, newPerson) -> {
            updateOrderList(newPerson);
        }));
    }

    /**
     * Updates the orderListView with the new selected person's orders.
     * @param person The newly selected person whose orders will be displayed.
     */
    private void updateOrderList(Person person) {
        if (person == null) {
            orderListView.setItems(FXCollections.observableArrayList());
            orderListView.setManaged(false);
            orderGuide.setManaged(true);
            orderListTitle.setText(DEFAULT_TITLE);
            return;
        }
        orderListView.setItems(person.getOrderList().asUnmodifiableObservableList());
        orderListView.setCellFactory(listView -> new OrderListViewCell());
        orderListView.setManaged(true);
        orderGuide.setManaged(false);
        orderListTitle.setText(String.format(TITLE_WITH_SELECTED_PERSON,person.getName().fullName));
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Order} using a {@code OrderCard}.
     */
    class OrderListViewCell extends ListCell<Order> {
        @Override
        protected void updateItem(Order order, boolean empty) {
            super.updateItem(order, empty);

            if (empty || order == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new OrderCard(order, getIndex() + 1).getRoot());
            }
        }
    }

}
