package networkbook.logic.parser;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import networkbook.logic.Messages;
import networkbook.logic.commands.CreateCommand;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.model.person.Address;
import networkbook.model.person.Email;
import networkbook.model.person.Name;
import networkbook.model.person.Person;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.tag.Tag;
import networkbook.model.util.UniqueList;

/**
 * Parses input arguments and creates a new CreateCommand object
 */
public class CreateCommandParser implements Parser<CreateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CreateCommand
     * and returns an CreateCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CreateCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        CliSyntax.PREFIX_NAME,
                        CliSyntax.PREFIX_PHONE,
                        CliSyntax.PREFIX_EMAIL,
                        CliSyntax.PREFIX_ADDRESS,
                        CliSyntax.PREFIX_TAG,
                        CliSyntax.PREFIX_PRIORITY
                );

        if (!arePrefixesPresent(
                argMultimap,
                CliSyntax.PREFIX_NAME,
                CliSyntax.PREFIX_ADDRESS,
                CliSyntax.PREFIX_PHONE,
                CliSyntax.PREFIX_EMAIL
        ) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                                                    CreateCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(
                CliSyntax.PREFIX_NAME,
                CliSyntax.PREFIX_PHONE,
                CliSyntax.PREFIX_EMAIL,
                CliSyntax.PREFIX_ADDRESS,
                CliSyntax.PREFIX_PRIORITY
        );
        Name name = ParserUtil.parseName(argMultimap.getValue(CliSyntax.PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(CliSyntax.PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(CliSyntax.PREFIX_EMAIL).get());
        UniqueList<Email> emails = new UniqueList<Email>().setItems(List.of(email));
        Address address = ParserUtil.parseAddress(argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(CliSyntax.PREFIX_TAG));
        Priority priority = ParserUtil.parsePriority(argMultimap.getValue(CliSyntax.PREFIX_PRIORITY).orElse(null));

        Person person = new Person(name, phone, emails, address, tagList, priority);

        return new CreateCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
