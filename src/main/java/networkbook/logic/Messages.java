package networkbook.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import networkbook.logic.parser.Prefix;
import networkbook.model.person.Graduation;
import networkbook.model.person.Person;
import networkbook.model.person.Priority;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_CONTACT_NAME = "Invalid name provided for contact to create!";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_SORTED_OVERVIEW = "Here is your sorted list of contacts:"
            + "\n(%1$d persons listed)";
    public static final String MESSAGE_DUPLICATE_FIELDS =
            "Multiple values specified for the following single-valued field(s): ";
    private static final String MESSAGE_FIELDS_MUST_BE_PRESENT =
            "The total number of fields of %s, which is %d, must match the number of fields of %s";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    public static String getErrorMessageForPrefixMustBePresent(
            Prefix[] prefixesIfPresent, Prefix prefixThenPresent, int totalCount) {
        String prefixesString = Stream.of(prefixesIfPresent).map(Prefix::toString).reduce("", (u, v) -> u + " " + v);
        return String.format(MESSAGE_FIELDS_MUST_BE_PRESENT,
                String.join(" ", prefixesString), totalCount, prefixThenPresent.toString());
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phones: ").append(person.getPhones())
                .append("; Emails: ").append(person.getEmails())
                .append("; Links: ").append(person.getLinks());
        person.getGraduation().ifPresent((Graduation g) -> {
            builder.append("; Graduation: ");
            builder.append(g.getFullString());
        });
        builder.append("; Courses: ").append(person.getCourses());
        builder.append("; Specialisations ").append(person.getSpecialisations());
        builder.append("; Tags: ");
        person.getTags().forEach(builder::append);
        person.getPriority().ifPresent((Priority p) -> {
            builder.append("; Priority: ");
            builder.append(p);
        });
        return builder.toString();
    }

}
