package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.EditPersonDescriptor.createEditedPersonByDeletingAllAppointments;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.person.Person.MAXIMUM_NUM_OF_APPOINTMENTS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
<<<<<<< HEAD
=======
import seedu.address.logic.parser.EditPersonDescriptor;
import seedu.address.logic.util.MaximumSortedList;
>>>>>>> musab_max_appointmnet
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes all appointments of an existing person in the address book.
 */
public class DeleteAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "da";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Delete all appointments of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: [INDEX] \n"
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person's Appointment: %1$s";

    public static final String MESSAGE_NO_APPOINTMENT_TO_DELETE = "This person does not have an appointment to delete";
    private final Index index;

    /**
     * @param index of the person in the filtered person list
     */
    public DeleteAppointmentCommand(Index index) {
        requireNonNull(index);

        this.index = index;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

<<<<<<< HEAD
=======
        MaximumSortedList<Appointment> emptyAppointments = new MaximumSortedList<>(MAXIMUM_NUM_OF_APPOINTMENTS);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        editPersonDescriptor.setAppointments(emptyAppointments);

>>>>>>> musab_max_appointmnet
        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPersonByDeletingAllAppointments(personToEdit);

        if (personToEdit.getAppointments().size() == 0) {
            throw new CommandException(MESSAGE_NO_APPOINTMENT_TO_DELETE);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, editedPerson));
    }



    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteAppointmentCommand)) {
            return false;
        }

        // state check
        DeleteAppointmentCommand e = (DeleteAppointmentCommand) other;
        return index.equals(e.index);
    }
}
