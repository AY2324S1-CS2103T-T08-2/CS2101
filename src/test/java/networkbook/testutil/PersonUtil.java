package networkbook.testutil;

import java.util.Set;

import networkbook.logic.commands.CreateCommand;
import networkbook.logic.commands.EditCommand;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.person.Email;
import networkbook.model.person.Link;
import networkbook.model.person.Person;
import networkbook.model.tag.Tag;
import networkbook.model.util.UniqueList;

/**
 * A utility class for Person.
 */
public class PersonUtil {
    //TODO: Add priority
    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return CreateCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(CliSyntax.PREFIX_NAME + " " + person.getName().fullName + " ");
        person.getPhone().ifPresent(phone -> sb.append(CliSyntax.PREFIX_PHONE).append(" ")
                .append(phone.value).append(" "));
        person.getEmails().stream().forEach(
                e -> sb.append(CliSyntax.PREFIX_EMAIL + " " + e.toString() + " ")
        );
        person.getLinks().stream().forEach(
                e -> sb.append(CliSyntax.PREFIX_LINK + " " + e.toString() + " ")
        );
        person.getGraduatingYear().ifPresent(graduatingYear -> sb.append(CliSyntax.PREFIX_GRADUATING_YEAR)
                .append(" ").append(graduatingYear.value).append(" "));
        person.getCourse().ifPresent(course -> sb.append(CliSyntax.PREFIX_COURSE).append(" ")
                .append(course.value).append(" "));
        person.getSpecialisation().ifPresent(specialisation -> sb.append(CliSyntax.PREFIX_SPECIALISATION)
                .append(" ").append(specialisation.getSpecialisation()).append(" "));
        person.getTags().stream().forEach(
            s -> sb.append(CliSyntax.PREFIX_TAG + " " + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditCommand.EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(CliSyntax.PREFIX_NAME).append(" ")
                                                    .append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(CliSyntax.PREFIX_PHONE).append(" ")
                                                    .append(phone.value).append(" "));
        if (descriptor.getEmails().isPresent()) {
            UniqueList<Email> emails = descriptor.getEmails().get();
            if (emails.isEmpty()) {
                sb.append(CliSyntax.PREFIX_EMAIL).append(" ");
            } else {
                emails.forEach(e -> sb.append(CliSyntax.PREFIX_EMAIL).append(" ")
                                                    .append(e.toString()).append(" "));
            }
        }
        if (descriptor.getLinks().isPresent()) {
            UniqueList<Link> links = descriptor.getLinks().get();
            if (links.isEmpty()) {
                sb.append(CliSyntax.PREFIX_LINK).append(" ");
            } else {
                links.forEach(e -> sb.append(CliSyntax.PREFIX_LINK).append(" ").append(e.toString()).append(" "));
            }
        }
        descriptor.getGraduatingYear().ifPresent(graduatingYear -> sb.append(CliSyntax.PREFIX_GRADUATING_YEAR)
                .append(" ").append(graduatingYear.value).append(" "));
        descriptor.getCourse().ifPresent(course -> sb.append(CliSyntax.PREFIX_COURSE).append(" ")
                .append(course.value).append(" "));
        descriptor.getSpecialisation().ifPresent(specialisation -> sb.append(CliSyntax.PREFIX_SPECIALISATION)
                .append(" ").append(specialisation.getSpecialisation()).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(CliSyntax.PREFIX_TAG).append(" ");
            } else {
                tags.forEach(s -> sb.append(CliSyntax.PREFIX_TAG).append(" ")
                                                    .append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
