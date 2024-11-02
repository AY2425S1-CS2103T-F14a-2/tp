package seedu.sellsavvy.logic.commands.ordercommands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.DESC_ATLAS;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.DESC_BOTTLE;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.VALID_DATE_ATLAS;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.VALID_DATE_OUTDATED;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.VALID_ITEM_ATLAS;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.VALID_QUANTITY_ATLAS;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.VALID_QUANTITY_BOTTLE;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.assertCommandFailure;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.getOrderByIndex;
import static seedu.sellsavvy.logic.commands.personcommands.PersonCommandTestUtil.assertCommandSuccess;
import static seedu.sellsavvy.model.order.Date.MESSAGE_OUTDATED_WARNING;
import static seedu.sellsavvy.testutil.TypicalIndexes.INDEX_FIRST_ORDER;
import static seedu.sellsavvy.testutil.TypicalIndexes.INDEX_FOURTH_PERSON;
import static seedu.sellsavvy.testutil.TypicalIndexes.INDEX_SECOND_ORDER;
import static seedu.sellsavvy.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.sellsavvy.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.sellsavvy.commons.core.index.Index;
import seedu.sellsavvy.logic.Messages;
import seedu.sellsavvy.logic.commands.generalcommands.ClearCommand;
import seedu.sellsavvy.logic.commands.ordercommands.EditOrderCommand.EditOrderDescriptor;
import seedu.sellsavvy.model.Model;
import seedu.sellsavvy.model.ModelManager;
import seedu.sellsavvy.model.UserPrefs;
import seedu.sellsavvy.model.order.Order;
import seedu.sellsavvy.model.order.Status;
import seedu.sellsavvy.model.order.StatusEqualsKeywordPredicate;
import seedu.sellsavvy.model.person.Person;
import seedu.sellsavvy.testutil.EditOrderDescriptorBuilder;
import seedu.sellsavvy.testutil.OrderBuilder;

public class EditOrderCommandTest {
    private Model model;
    private Person selectedPerson;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs()).createCopy();
        selectedPerson = model.getFilteredPersonList().get(INDEX_FOURTH_PERSON.getZeroBased());
        model.updateSelectedPerson(selectedPerson);
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Order editedOrder = new OrderBuilder().build();
        EditOrderDescriptor descriptor = new EditOrderDescriptorBuilder(editedOrder).build();
        EditOrderCommand editOrderCommand = new EditOrderCommand(INDEX_FIRST_ORDER, descriptor);

        String expectedMessage = String.format(EditOrderCommand.MESSAGE_EDIT_ORDER_SUCCESS,
                Messages.format(editedOrder));

        Model expectedModel = model.createCopy();
        expectedModel.setOrder(getOrderByIndex(expectedModel, INDEX_FIRST_ORDER), editedOrder);

        assertCommandSuccess(editOrderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastOrder = Index.fromOneBased(model.getFilteredOrderList().size());
        Order lastOrder = getOrderByIndex(model, indexLastOrder);

        OrderBuilder orderBuilder = new OrderBuilder(lastOrder);
        Order editedOrder = orderBuilder.withQuantity(VALID_QUANTITY_ATLAS)
                .withDate(VALID_DATE_ATLAS).build();

        EditOrderDescriptor descriptor = new EditOrderDescriptorBuilder().withQuantity(VALID_QUANTITY_ATLAS)
                .withDate(VALID_DATE_ATLAS).build();
        EditOrderCommand editOrderCommand = new EditOrderCommand(indexLastOrder, descriptor);

        String expectedMessage = String.format(EditOrderCommand.MESSAGE_EDIT_ORDER_SUCCESS,
                Messages.format(editedOrder));

        Model expectedModel = model.createCopy();
        expectedModel.setOrder(getOrderByIndex(expectedModel, indexLastOrder), editedOrder);

        assertCommandSuccess(editOrderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noOrderListDisplayed_failure() {
        model.updateSelectedPerson(null);
        Order editedOrder = new OrderBuilder().build();
        EditOrderDescriptor descriptor = new EditOrderDescriptorBuilder(editedOrder).build();
        EditOrderCommand editOrderCommand = new EditOrderCommand(INDEX_FIRST_ORDER, descriptor);

        String expectedMessage = Messages.MESSAGE_ORDERLIST_DOES_NOT_EXIST;

        assertCommandFailure(editOrderCommand, model, expectedMessage);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditOrderCommand editOrderCommand = new EditOrderCommand(INDEX_FIRST_ORDER, new EditOrderDescriptor());
        Order editedOrder = getOrderByIndex(model, INDEX_FIRST_ORDER);

        String expectedMessage = String.format(EditOrderCommand.MESSAGE_EDIT_ORDER_SUCCESS,
                Messages.format(editedOrder));

        Model expectedModel = model.createCopy();

        assertCommandSuccess(editOrderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_completedOrderUnfilteredList_success() {
        Order completedOrder = getOrderByIndex(model, INDEX_SECOND_ORDER);

        // ensures that the selected order is indeed completed
        assertEquals(completedOrder.getStatus(), Status.COMPLETED);

        OrderBuilder orderBuilder = new OrderBuilder(completedOrder);
        Order editedOrder = orderBuilder.withQuantity(VALID_QUANTITY_ATLAS)
                .withDate(VALID_DATE_ATLAS).build();

        EditOrderDescriptor descriptor = new EditOrderDescriptorBuilder().withQuantity(VALID_QUANTITY_ATLAS)
                .withDate(VALID_DATE_ATLAS).build();
        EditOrderCommand editOrderCommand = new EditOrderCommand(INDEX_SECOND_ORDER, descriptor);

        String expectedMessage = String.format(EditOrderCommand.MESSAGE_EDIT_ORDER_SUCCESS,
                Messages.format(editedOrder));

        Model expectedModel = model.createCopy();
        expectedModel.setOrder(getOrderByIndex(expectedModel, INDEX_SECOND_ORDER), editedOrder);

        assertCommandSuccess(editOrderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        selectedPerson.updateFilteredOrderList(new StatusEqualsKeywordPredicate(Status.PENDING));

        Order orderInFilteredList = getOrderByIndex(model, INDEX_SECOND_ORDER);
        Order editedOrder = new OrderBuilder(orderInFilteredList).withItem(VALID_ITEM_ATLAS).build();
        EditOrderCommand editOrderCommand = new EditOrderCommand(INDEX_SECOND_ORDER,
                new EditOrderDescriptorBuilder().withItem(VALID_ITEM_ATLAS).build());

        String expectedMessage = String.format(EditOrderCommand.MESSAGE_EDIT_ORDER_SUCCESS,
                Messages.format(editedOrder));

        Model expectedModel = model.createCopy();
        //TODO: removed the line below after createCopy gets updated
        expectedModel.getSelectedPerson().updateFilteredOrderList(new StatusEqualsKeywordPredicate(Status.PENDING));
        expectedModel.setOrder(getOrderByIndex(expectedModel, INDEX_SECOND_ORDER), editedOrder);

        assertCommandSuccess(editOrderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateOrderUnfilteredList_warningGiven() {
        Order firstOrder = getOrderByIndex(model, INDEX_FIRST_ORDER);
        EditOrderDescriptor descriptor = new EditOrderDescriptorBuilder(firstOrder).build();
        EditOrderCommand editOrderCommand = new EditOrderCommand(INDEX_THIRD_PERSON, descriptor);

        String warningMessage = String.format(EditOrderCommand.MESSAGE_DUPLICATE_ORDER_WARNING,
                firstOrder.getStatus().getValue());
        String expectedMessage = warningMessage
                + String.format(EditOrderCommand.MESSAGE_EDIT_ORDER_SUCCESS, Messages.format(firstOrder));

        Model expectedModel = model.createCopy();
        expectedModel.setOrder(getOrderByIndex(expectedModel, INDEX_THIRD_PERSON),
                getOrderByIndex(expectedModel, INDEX_FIRST_ORDER));

        assertCommandSuccess(editOrderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateOrderFilteredList_warningGiven() {
        Order firstOrder = getOrderByIndex(model, INDEX_FIRST_ORDER);
        selectedPerson.updateFilteredOrderList(new StatusEqualsKeywordPredicate(Status.PENDING));
        EditOrderDescriptor descriptor = new EditOrderDescriptorBuilder(firstOrder).build();
        EditOrderCommand editOrderCommand = new EditOrderCommand(INDEX_SECOND_ORDER, descriptor);

        String warningMessage = String.format(EditOrderCommand.MESSAGE_DUPLICATE_ORDER_WARNING,
                firstOrder.getStatus().getValue());
        String expectedMessage = warningMessage
                + String.format(EditOrderCommand.MESSAGE_EDIT_ORDER_SUCCESS, Messages.format(firstOrder));

        Model expectedModel = model.createCopy();
        //TODO: removed the line below after createCopy get updated
        expectedModel.getSelectedPerson().updateFilteredOrderList(new StatusEqualsKeywordPredicate(Status.PENDING));
        expectedModel.setOrder(getOrderByIndex(expectedModel, INDEX_SECOND_ORDER),
                getOrderByIndex(expectedModel, INDEX_FIRST_ORDER));

        assertCommandSuccess(editOrderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_orderHasElapsed_warningGiven() {
        Index indexLastOrder = Index.fromOneBased(model.getFilteredOrderList().size());
        Order lastOrder = getOrderByIndex(model, indexLastOrder);

        OrderBuilder orderInList = new OrderBuilder(lastOrder);
        Order editedOrder = orderInList.withDate(VALID_DATE_OUTDATED).build();

        EditOrderDescriptor descriptor = new EditOrderDescriptorBuilder().withDate(VALID_DATE_OUTDATED).build();
        EditOrderCommand editOrderCommand = new EditOrderCommand(indexLastOrder, descriptor);

        String expectedMessage = String.format(MESSAGE_OUTDATED_WARNING
                + EditOrderCommand.MESSAGE_EDIT_ORDER_SUCCESS, Messages.format(editedOrder));

        Model expectedModel = model.createCopy();
        expectedModel.setOrder(getOrderByIndex(expectedModel, indexLastOrder), editedOrder);

        assertCommandSuccess(editOrderCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidOrderIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredOrderList().size() + 1);
        EditOrderDescriptor descriptor = new EditOrderDescriptorBuilder().withQuantity(VALID_QUANTITY_BOTTLE).build();
        EditOrderCommand editOrderCommand = new EditOrderCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editOrderCommand, model, Messages.MESSAGE_INVALID_ORDER_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of unfiltered order list
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        // get original size before filtering
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredOrderList().size());

        selectedPerson.updateFilteredOrderList(new StatusEqualsKeywordPredicate(Status.PENDING));

        // ensures that outOfBoundIndex is indeed out of bounds after filtering
        assertTrue(outOfBoundIndex.getZeroBased() >= model.getFilteredOrderList().size());

        EditOrderCommand editOrderCommand = new EditOrderCommand(outOfBoundIndex,
                new EditOrderDescriptorBuilder().withItem(VALID_ITEM_ATLAS).build());

        assertCommandFailure(editOrderCommand, model, Messages.MESSAGE_INVALID_ORDER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditOrderCommand standardCommand = new EditOrderCommand(INDEX_FIRST_ORDER, DESC_ATLAS);

        // same values -> returns true
        EditOrderDescriptor copyDescriptor = new EditOrderDescriptor(DESC_ATLAS);
        EditOrderCommand commandWithSameValues = new EditOrderCommand(INDEX_FIRST_ORDER, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditOrderCommand(INDEX_SECOND_ORDER, DESC_ATLAS)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditOrderCommand(INDEX_FIRST_ORDER, DESC_BOTTLE)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditOrderDescriptor editOrderDescriptor = new EditOrderDescriptor();
        EditOrderCommand editOrderCommand = new EditOrderCommand(index, editOrderDescriptor);
        String expected = EditOrderCommand.class.getCanonicalName() + "{index=" + index + ", editOrderDescriptor="
                + editOrderDescriptor + "}";
        assertEquals(expected, editOrderCommand.toString());
    }
}
