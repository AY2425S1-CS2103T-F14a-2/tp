package seedu.sellsavvy.model.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sellsavvy.logic.commands.ordercommands.OrderCommandTestUtil.VALID_DATE_BOTTLE;
import static seedu.sellsavvy.testutil.Assert.assertThrows;
import static seedu.sellsavvy.testutil.TypicalOrders.ATLAS;

import org.junit.jupiter.api.Test;

import seedu.sellsavvy.commons.core.index.Index;
import seedu.sellsavvy.model.order.exceptions.OrderNotFoundException;
import seedu.sellsavvy.testutil.OrderBuilder;

public class OrderListTest {
    private final OrderList orderList = new OrderList();

    @Test
    public void contains_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> orderList.contains(null));
    }

    @Test
    public void contains_orderNotInList_returnsFalse() {
        assertFalse(orderList.contains(ATLAS));
    }

    @Test
    public void contains_orderInList_returnsTrue() {
        orderList.add(ATLAS);
        assertTrue(orderList.contains(ATLAS));
    }

    @Test
    public void contains_orderWithSameItemInList_returnsTrue() {
        orderList.add(ATLAS);
        Order editedAtlas = new OrderBuilder(ATLAS).withDate(VALID_DATE_BOTTLE).build();
        assertTrue(orderList.contains(editedAtlas));
    }

    @Test
    public void add_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> orderList.add(null));
    }

    @Test
    public void remove_nullOrder_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> orderList.remove(null));
    }

    @Test
    public void remove_orderDoesNotExist_throwsOrderNotFoundException() {
        assertThrows(OrderNotFoundException.class, () -> orderList.remove(Index.fromZeroBased(0)));
    }

    @Test
    public void remove_existingOrder_removesOrder() {
        orderList.add(ATLAS);
        orderList.remove(Index.fromZeroBased(0));
        OrderList expectedOrderList = new OrderList();
        assertEquals(expectedOrderList, orderList);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> orderList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(orderList.asUnmodifiableObservableList().toString(), orderList.toString());
    }
}
