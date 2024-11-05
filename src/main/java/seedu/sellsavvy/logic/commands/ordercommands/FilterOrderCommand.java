package seedu.sellsavvy.logic.commands.ordercommands;

import static java.util.Objects.requireNonNull;
import static seedu.sellsavvy.logic.Messages.MESSAGE_ORDERLIST_DOES_NOT_EXIST;

import seedu.sellsavvy.commons.util.ToStringBuilder;
import seedu.sellsavvy.logic.commands.Command;
import seedu.sellsavvy.logic.commands.CommandResult;
import seedu.sellsavvy.logic.commands.exceptions.CommandException;
import seedu.sellsavvy.model.Model;
import seedu.sellsavvy.model.order.Status;
import seedu.sellsavvy.model.order.StatusEqualsKeywordPredicate;
import seedu.sellsavvy.model.person.Person;

/**
 * Filters and lists all orders under a specified person from the displayed order list,
 * with an order status that matches the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FilterOrderCommand extends Command {

    public static final String COMMAND_WORD = "filterorder";
    public static final String COMMAND_ALIAS = "filtero";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters the displayed order list by"
            + " order status (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: ORDER_STATUS (" + Status.MESSAGE_CONSTRAINTS + ")\n"
            + "Example: " + COMMAND_WORD + " Completed";
    public static final String MESSAGE_FILTER_ORDERS_SUCCESS = "%1$d %2$s orders listed!";
    public static final String MESSAGE_FILTER_ORDERS_SUCCESS_SINGULAR = "1 %1$s order listed!";

    private final StatusEqualsKeywordPredicate predicate;

    public FilterOrderCommand(StatusEqualsKeywordPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person selectedPerson = model.getSelectedPerson();
        if (selectedPerson == null) {
            throw new CommandException(MESSAGE_ORDERLIST_DOES_NOT_EXIST);
        }

        selectedPerson.updateFilteredOrderList(predicate);
        return new CommandResult(generateSuccessMessage(selectedPerson.getFilteredOrderList().size(), predicate));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterOrderCommand)) {
            return false;
        }

        FilterOrderCommand otherFilterOrderCommand = (FilterOrderCommand) other;
        return predicate.equals(otherFilterOrderCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }

    /**
     * Generates the response upon successful execution of the command.
     */
    private String generateSuccessMessage(int numberOfOrders, StatusEqualsKeywordPredicate predicate) {
        if (numberOfOrders == 1) {
            return String.format(MESSAGE_FILTER_ORDERS_SUCCESS_SINGULAR, predicate.getValue());
        } else {
            return String.format(MESSAGE_FILTER_ORDERS_SUCCESS, numberOfOrders, predicate.getValue());
        }
    }
}
