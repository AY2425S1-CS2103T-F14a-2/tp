---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# SellSavvy Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

- **Project Origin**: This project builds on the AddressBook-Level3 project, originally created by the [SE-EDU initiative](https://se-education.org).

- **Libraries Utilized**:
    - [JavaFX](https://openjfx.io/): Used for building a responsive graphical user interface.
    - [Jackson](https://github.com/FasterXML/jackson): Used for JSON data processing.
    - [JUnit5](https://github.com/junit-team/junit5): Used for testing to ensure code reliability. <br>

- **AI Assistance**: The *SellSavvy* logo was generated with ChatGPT 4.0.

- **References to Other Team Projects (TPs)**:
    - For our User Guide, we referred to the [AY2425S1-CS2103T-F14a-1 User Guide](https://github.com/AY2425S1-CS2103T-F14a-1/tp/blob/master/docs/UserGuide.md) and adapted their Markbind layouts for constraint and tips boxes.


--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2425S1-CS2103T-F14a-2/tp/blob/master/src/main/java/seedu/sellsavvy/Main.java) and [`MainApp`](https://github.com/AY2425S1-CS2103T-F14a-2/tp/blob/master/src/main/java/seedu/sellsavvy/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `deletecustomer 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S1-CS2103T-F14a-2/tp/blob/master/src/main/java/seedu/sellsavvy/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `CustomerListPanel`, `OrderListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S1-CS2103T-F14a-2/tp/blob/master/src/main/java/seedu/sellsavvy/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S1-CS2103T-F14a-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Customer` and `Order` objects residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S1-CS2103T-F14a-2/tp/blob/master/src/main/java/seedu/sellsavvy/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("deletecustomer 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `deletecustomer 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCustomerCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCustomerCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCustomerCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a customer).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCustomerCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCustomerCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddOrderCommandParser`, `DeleteCustomerCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S1-CS2103T-F14a-2/tp/blob/master/src/main/java/seedu/sellsavvy/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Customer` objects (which are contained in a `UniqueCustomerList` object).
* stores the currently 'selected' `Customer` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Customer>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a particular `Customer` whose orders will be displayed in a `ReadOnlyObjectWrapper<Customer>` which is exposed to outsiders as an unmodifiable `ReadOnlyObjectProperty<Customer>` that can be 'observed' e.g. the UI can be bound to this object property so that the UI automatically updates when selected customer change.
  * Each `Customer` stores the currently 'selected' `Order` objects (e.g., results of a filter query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Order>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Customer` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Customer` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S1-CS2103T-F14a-2/tp/blob/master/src/main/java/seedu/sellsavvy/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.sellsavvy.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `deletecustomer 5` command to delete the 5th customer in the address book. The `deletecustomer` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `deletecustomer 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `addcustomer n/David …​` to add a new customer. The `addcustomer` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the customer was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `listcustomer`. Commands that do not modify the address book, such as `listcustomer`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `addcustomer n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `deletecustomer`, just save the customer being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* independent sellers/drop-shipping business owners selling on platforms like Carousell
* lack a central platform for drop-shipping and delivery order management
* has a need to manage a significant number of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: For small independent sellers, organizing customer lists can be challenging. SellSavvy offers a centralized platform to store orders and track deliveries, streamlining drop-shipping management. SellSavvy is optimized for tech-savvy fast-typing users through command-line interface and efficient functionalities.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                  | I want to …​                                              | So that I can…​                                            |
|----------|--------------------------|-----------------------------------------------------------|------------------------------------------------------------|
| `* * *`  | user                     | add new customers with details such as name and address   | remember details of customers for order deliveries         |
| `* * *`  | user                     | add orders made by a customer                             | keep track of orders made by each customer                 |
| `* * *`  | user                     | add details to orders, such as delivery date and quantity | remember details of orders when making deliveries          |
| `* * *`  | user                     | mark orders as completed                                  | keep track of orders that have been delivered              |
| `* * *`  | user with many customers | delete a customer from my address book                    | remove clients who I no longer need to be in contact with  |
| `* * *`  | user with many customers | view all my customer contacts                             | see an overview of all my customers' details               |
| `* * *`  | user with many orders    | view all orders under a specific customer                 | see an overview of all orders made by a customer           |
| `* * *`  | user with many orders    | delete an order under a customer                          | remove orders that I no longer need to track               |
| `* * *`  | user with many orders    | revert an order's completed status                        | keep track of erroneous or failed order deliveries         |
| `* *`    | tech-savvy user          | save data to local storage                                | keep my data even after exiting SellSavvy                  |
| `* *`    | tech-savvy user          | load data from local storage                              | access my local data using SellSavvy                       |
| `* *`    | experienced user         | edit a customer's details                                 | keep the customers' information up-to-date                 |
| `* *`    | experienced user         | edit an order's details                                   | keep the orders' information up-to-date                    |
| `*`      | experienced user         | find a customer by name                                   | search for a specific customer's details                   |
| `*`      | experienced user         | filter orders by their status                             | see which orders are completed or have yet to be delivered |
| `*`      | inexperienced user       | be informed a customer already made an identical order    | take note of duplicate orders made by the same customer    |
| `*`      | inexperienced user       | be informed if a new order's delivery date has passed     | take note of erroneous creation of historical orders       |

*{More to be added}*

### Use cases

**Use case 1: View List of Customers**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC01 - View List of Customers

**MSS**

1.  User chooses to view the list of customers.
2.  SellSavvy retrieves all customers from the database.
3.  SellSavvy displays all customers along with their details.

Use case ends.

**Use case 2: Add a Customer**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC02 - Add Customer
* **Guarantees**:
  * Customer will be added to customer list if input parameters are valid.

**MSS**

1.  User chooses to add a new customer and specifies the customer details.
2.  SellSavvy adds the customer into the list.
3.  SellSavvy confirms the addition by displaying the newly added customer's details.

Use case ends.

**Extensions**

* 1a. SellSavvy detects required details missing.
  * 1a1. SellSavvy displays an error message “Invalid command format!” and states the command format.

  Use case ends.


* 1b. SellSavvy detects that there is a parameter not satisfying its constraint.
  * 1b1. SellSavvy states the constraint of the invalid parameter.

  Use case ends


* 1c. SellSavvy detects that a customer with identical name already exists.
  * 1c1. SellSavvy displays an error that the customer already exists.
  
  Use case ends.


* 2a. SellSavvy detects that there is an existing customer with a similar name.
  * 2a1. SellSavvy gives a warning that a customer with a similar name already exists.
  
  Use case resumes from step 3.


* 2b. SellSavvy detects that the new customer has tags with similar names.
  * 2b1. SellSavvy gives a warning that there are similar tags in the new customer.
  
  Use case resumes from step 3.

**Use case 3: Delete Customer and All Orders Related to The Customer**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC03 - Delete Customer and All Orders related to the customer
* **Preconditions**: There are customers displayed in the customer list.
* **Guarantees**:
    * Customer and all their orders will be deleted if input parameters are valid.

**MSS**

1. User wants to delete a customer.
2. User finds the customer index from the list.
3. User deletes the customer by their index.
4. SellSavvy updates the displayed list of customers and indicates that delete is successful.

Use case ends.

**Extensions**

* 3a. SellSavvy detects that there are no customers with the specified index.
  * 3a1. SellSavvy displays an error that the customer index is invalid. 
  
  Use case ends.


* 3b. SellSavvy detects that there are no customers with the specified index.
    * 3a1. SellSavvy displays an error that the customer index is invalid.

  Use case ends.

**Use case 4: Find the Customer by their Name**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC04 - Find the Customer by their Name
* **Guarantees**:
  * All customers displayed will have at least one of the specified keywords in their name.

**MSS**

1. User wants to search for a specific customer.
2. User specifies keyword(s) of the name of the customer
3. SellSavvy displays all customers whose names have at least one of the keywords.

Use case ends.

**Use case 5: Add an Order under a Customer**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC06 - Add an Order under a Customer
* **Preconditions**: There are customers displayed in the customer list.
* **Guarantees**:
    * A new pending order will be added under the specified customer, if input parameters are valid.

**MSS**

1. User wants to add an order under a specific customer.
2. User finds the customer index from the list.
3. User adds the customer using the index, specifying the details of the order.
4. SellSavvy adds the order under the customer.
5. SellSavvy confirms the addition by displaying the newly added order's details and customer's list of orders.

Use case ends.

**Extensions**

* 3a. SellSavvy detects required details missing.
    * 3a1. SellSavvy displays an error message “Invalid command format!” and states the command format.

  Use case ends.


* 3b. SellSavvy detects that there is a parameter not satisfying its constraint.
    * 3b1. SellSavvy states the constraint of the invalid parameter.

  Use case ends


* 3c. SellSavvy detects that there are no customers with the specified index.
    * 3c1. SellSavvy displays an error that the customer index is invalid.

  Use case ends.


* 4a. SellSavvy detects that there is an existing pending order under the customer with a similar details.
    * 4a1. SellSavvy gives a warning that an order with a similar details already exists.

  Use case resumes from step 5.

**Use case 6: List a Customer's Orders**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC06 - List a Customer's Orders
* **Preconditions**: There are customers displayed in the customer list.
* **Guarantees**:
    * Orders made by specific customer will be displayed as a list, if input parameters are valid.

**MSS**

1. User wants to view all orders made by a specific customer.
2. User finds the customer index from the customer list.
3. User inputs command to list all orders, by the index of customer in customer list.
4. SellSavvy retrieves a list of all orders made by specified customer.
5. SellSavvy displays the orders in a list in GUI.

Use case ends.

**Extensions**

* 3a. SellSavvy detects that there are no customers with the specified index.
    * 3a1. SellSavvy displays an error that the customer index is invalid.

    Use case ends.

**Use case 7: Mark Order as Completed**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC07 - Mark Order as Completed
* **Preconditions**: A customer's list of orders is being displayed.
* **Guarantees**:
    * Specified order will be marked as “Completed” if the input parameters are valid.

**MSS**

1. User wants to mark an order by a customer as completed.
2. User finds the order they want to mark as completed.
3. User specifies the index of the order.
4. SellSavvy updates the status of the order and indicates that the action is successful.

Use case ends.

**Extensions**

* 3a.  The specified order is already marked as "Completed".
    * 3a1. SellSavvy displays a message stating that the order is already marked as completed.

    Use case ends.


* 3b. There are no orders with the specified index.
  * 3b1. SellSavvy displays an error that the order index is invalid.

  Use case ends.

**Use case 8: Remove "Completed" Marking from Order**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC08 - Remove "Completed" Marking from Order
* **Preconditions**: A customer's list of orders is being displayed.
* **Guarantees**:
    * Specified order will be reverted to "Pending" status if the input parameters are valid.

**MSS**

1. User wants to remove "Completed" Marking from Order due to mistake.
2. User finds the order they want to mark as completed.
3. User specifies the index of the order.
4. SellSavvy updates the status of the order and indicates that the action is successful.

Use case ends.

**Extensions**

* 3a.  The specified order is not marked as “Completed” in the first place.
    * 3a1. SellSavvy displays a message stating that the order is not marked as completed in the first place.

  Use case ends.


* 3b. There are no orders with the specified index.
    * 3b1. SellSavvy displays an error that the order index is invalid.

  Use case ends.

**Use case 9: Delete an order**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC09 - Delete an Order
* **Preconditions**: A customer's list of orders is being displayed.
* **Guarantees**:
    * An order made by the customer will be deleted if input parameters are valid.

**MSS**

1. User wants to delete an order.
2. User finds the order they want to delete.
3. User deletes the order by their index.
4. SellSavvy updates the displayed list of orders that the action is successful.

Use case ends.


* 3a. There are no orders with the specified index.
    * 3a1. SellSavvy displays an error that the order index is invalid.

  Use case ends.

**Use case 10: Filter order list by order status**

* **System**: SellSavvy
* **Actor**: User
* **Use Case**: UC10 - Filter an Order List by Order Status
* **Preconditions**: A customer's list of orders is being displayed.
* **Guarantees**:
    * Orders with specified status under the customer will be displayed as a list, if input parameters are valid.

**MSS**

1. User wants to filter an order list.
2. User filters the order list of the customer by the status keyword.
3. SellSavvy displays the orders with specified status in a list in GUI.

Use case ends.

**Extensions**

* 2a. There is no such status with specified status keyword.
  * 2a1. SellSavvy displays an error message "Invalid command format!" and provides the available status keywords.

  Use case ends.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 100 customers and/or 1000 orders without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should be able to be used offline (i.e. without internet connection)
5. Should log user inputs and errors for analysis and debugging.
6. The system should respond within 2 seconds from any user input.

### Glossary

* **CLI**: Command Line Interface
* **Customer**: People who request for delivery order of product from user
* **GUI**: Graphical User Interface
* **JSON**: JavaScript Object Notation
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Order**: Agreement made by customers with user on delivery of product
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Status**: The current fulfilment condition of the delivery of an order, namely completed or pending.
<a id="similar"></a>
* **Similar names**: Names which are identical if whitespaces and case sensitivity are ignored.
* **Similar details (orders)**: Orders with identical date, quantity and status along with similar item names.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimal.

1. Saving window preferences

   1. Resize the window to an optimal size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. Saving changes in data

   1. Prerequisites: Has at least 1 customer listed using `listcustomer` in the GUI
    
   1.  Add an order under a customer using the `addorder` command.<br>
      Example: `addorder 1 i/Lamp d/20-11-2024 q/3`
      Expected: Order added under the first customer in the customer list and all his orders will be displayed. 

   1.  Re-launch the app by double-clicking the jar file.<br>
       Expected: The newest order added is retained.


### Adding a customer

**Note:** Some of the test cases may depend on previous test cases, especially those on testing customers with dupliacte/similar names. You are advised to follow the test cases in order. <br> 

**Tips:** All the prerequisites below will be fulfilled if you start off with the default sample data and follow the test cases in sequence.

1. Adding a unique customer with all parameters specified.

    1. Prerequisites: Customer with name `John Doe` or other similar name does not already exist in the addressbook.

    2. Test case: `addcustomer n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 t/friends t/owesMoney`<br>
       Expected: The customer is successfully added. Details of the added customer shown in the status message.

2. Adding a unique customer with all parameters specified using command aliases.

    1. Prerequisites: Customer with name `Betsy Crowe` or other similar name does not already exist in the addressbook.
   
    2. Test case: `addc n/Betsy Crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567 t/criminal` <br>
       Expected: The customer is successfully added. Details of the added customer shown in the status message.
   
3. Adding a customer with exact same name.

   1. Prerequisites: Customer with name `Betsy Crowe` already exist in the addressbook.

   2. Test case: `addcustomer n/Betsy Crowe t/friend e/betsycrowe@duplicate.com a/Newgate Prison p/12345678 t/criminal` <br>
      Expected: No customer is added. Error details shown in the status message. Status bar remains the same.

4. Adding a customer with a [similar name](#similar) and without the optional `tag` field.

    1. Prerequisites: Customer with name `Betsy Crowe` but not `Betsy crowe` already exist in the addressbook.

    2. Test case: `addcustomer n/Betsy crowe t/friend e/betsycrowe@example.com a/Newgate Prison p/1234567` <br>
       Expected: The customer is successfully added. A warning and details of the added customer shown in the status message.

5. Adding a customer with duplicate tags.

    1. Prerequisites: Customer with name `Yu Sutong` or other similar name does not already exist in the addressbook.

    2. Test case: `addcustomer n/Yu Sutong t/vvip t/vvip e/su@example.com a/Newgate Prison p/12345678` <br>
       Expected: The customer is successfully added with one of the duplicated tag ignored. Details of the added customer shown in the status message.

6. Adding a customer with [similar tags](#similar).

    1. Prerequisites: Customer with name `Foo Chao` or other similar name does not already exist in the addressbook.

    2. Test case: `addcustomer n/Foo Chao t/VVIP t/vvip e/su@example.com a/69, Sembawang Road. #01-01  p/12345678` <br>
       Expected: The customer is successfully added with both similar tags.A warning and details of the added customer shown in the status message.

7. Adding a customer with missing compulsory field.

   1. Test case: `addcustomer n/Lim Kai Xuan e/su@example.com a/69, Sembawang Road. #01-01` <br>
   
   2. Expected: No customer is added. Error details shown in the status message. Status bar remains the same.

   3. Test case: `addcustomer n/Lim Kai Xuan e/su@example.com p/12345678` <br>

   4. Expected: No customer is added. Error details shown in the status message. Status bar remains the same.

### Editing an existing customer

1. Editing a customer while all customers are being shown.

    1. Prerequisites: All customers are listed using the `listcustomer` command with at least 1 customer listed.

    2. Test case: `editcustomer 1 p/91234567 e/johndoe@example.com` <br>
       Expected: The customer is successfully edited. Details of the edited customer shown in the status message.

2. Editing a customer in a filtered list using command aliases.

    1. Prerequisites: Customers filtered using `findcustomer` command with at least 1 customer listed.
       Example: `findcustomer john`

    2. Test case: `editc 1 n/Betsy Crower t/` <br>
       Expected: The customer is successfully edited with all tags removed. Details of the edited customer shown in the status message. The displayed customer list got unfiltered.

3. Editing a customer to an exact same name as an existing customer.

    1. Prerequisites:
       - Customer with name `Betsy Crowe` already exist in the address book.
       - At least 1 customer is listed.
       - The customer to be edited is not `Betsy Crowe`.

    2. Test case: `editcustomer 1 n/Betsy Crowe` <br>
       Expected: No customer is edited. Error details shown in the status message. Status bar remains the same.

4. Editing a customer to a [similar name](#similar) as an existing customer.

    1. 1. Prerequisites:
        - Customer with name `Betsy Crowe` but not `betsy crowe` already exist in the address book.
        - At least 1 customer is listed.
        - The customer to be edited is not `Betsy Crowe`.
    2. Test case: `editcustomer 1 n/betsy crowe` <br>
       Expected: The customer is successfully edit. A warning and details of the edited customer shown in the status message.

5. Editing a customer to have duplicate tags.

    1. Prerequisites: At least one customer is listed.

    2. Test case: `editcustomer 1 t/friends t/friends` <br>
       Expected: The customer's tag is successfully edited with one of the duplicated tag ignored. Details of the edited customer shown in the status message.

6. Editing a customer to have [similar tags](#similar).

    1. Prerequisites: At least one customer is listed.

    2. Test case: `editcustomer 1 t/Friends t/friends` <br>
       Expected: The customer is successfully edited with both similar tags.A warning and details of the added customer shown in the status message.

7. Editing a customer with invalid inputs.
    1. Prerequisites: At least one customer is listed.

    2. Test case: `editcustomer 1 n/@#$%` <br>

    3. Expected: No customer is edited. Error details shown in the status message. Status bar remains the same.
   
### Deleting a customer

1. Deleting a customer while all customers are being shown.

   1. Prerequisites: List all customers using the `listcustomer` command with at least 1 customer listed.

   2. Test case: `deletecustomer 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.

   3. Test case: `deletecustomer 0`<br>
      Expected: No customer is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect `deletecustomer` commands to try: `deletecustomer`, `deletecustomer x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

2. Deleting a customer from a filtered list using command aliases.

    1. Prerequisites: Prerequisites: Customers filtered using `findcustomer` command with at least 1 customer listed.
       Example: `findcustomer john`

    2. Test case: `deletec 1`<br>
       Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message.

    3. Test case: `deletec 0`<br>
       Expected: No customer is deleted. Error details shown in the status message. Status bar remains the same.

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
