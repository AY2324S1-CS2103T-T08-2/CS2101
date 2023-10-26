package networkbook.logic.parser;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import networkbook.logic.Messages;
import networkbook.logic.commands.ClearCommand;
import networkbook.logic.commands.CreateCommand;
import networkbook.logic.commands.DeleteCommand;
import networkbook.logic.commands.EditCommand;
import networkbook.logic.commands.ExitCommand;
import networkbook.logic.commands.FindCommand;
import networkbook.logic.commands.HelpCommand;
import networkbook.logic.commands.ListCommand;
import networkbook.logic.commands.RedoCommand;
import networkbook.logic.commands.SortCommand;
import networkbook.logic.commands.UndoCommand;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.NameContainsKeyTermsPredicate;
import networkbook.model.person.Person;
import networkbook.model.person.PersonSortComparator;
import networkbook.model.person.PersonSortComparator.SortField;
import networkbook.model.person.PersonSortComparator.SortOrder;
import networkbook.testutil.EditPersonDescriptorBuilder;
import networkbook.testutil.PersonBuilder;
import networkbook.testutil.PersonUtil;
import networkbook.testutil.TypicalIndexes;

public class NetworkBookParserTest {

    private final NetworkBookParser parser = new NetworkBookParser();

    @Test
    public void parseCommand_create() throws Exception {
        Person person = new PersonBuilder().build();
        CreateCommand command = (CreateCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new CreateCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + TypicalIndexes.INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(TypicalIndexes.INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_undo() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD + " 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_redo() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD + " 3") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeyTermsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_sort() throws Exception {
        SortCommand command = (SortCommand) parser.parseCommand(
                SortCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_SORT_FIELD + " NAMe "
                + CliSyntax.PREFIX_SORT_ORDER + " desCending");
        PersonSortComparator expectedCmp = new PersonSortComparator(SortField.NAME, SortOrder.DESCENDING);
        assertEquals(new SortCommand(expectedCmp), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
                        -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, Messages.MESSAGE_UNKNOWN_COMMAND, ()
                -> parser.parseCommand("unknownCommand"));
    }
}
