package networkbook.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import networkbook.model.person.Graduation;
import networkbook.model.person.Person;
import networkbook.model.person.Priority;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String PHONES_HEADER = "Phone: ";
    private static final String EMAILS_HEADER = "Emails: ";
    private static final String LINKS_HEADER = "Links: ";
    private static final String GRADUATION_HEADER = "Graduation: ";
    private static final String COURSE_HEADER = "Courses: ";
    private static final String SPECIALISATION_HEADER = "Specialisations: ";
    private static final String PRIORITY_HEADER = "Priority: ";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/networkbook-level4/issues/336">The issue on NetworkBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phones;
    @FXML
    private Label links;
    @FXML
    private Label graduation;
    @FXML
    private Label courses;
    @FXML
    private Label specialisations;
    @FXML
    private Label emails;
    @FXML
    private FlowPane tags;
    @FXML
    private Label priority;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        requireNonNull(person);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phones.setText(PHONES_HEADER + person.getPhones().toString());
        emails.setText(EMAILS_HEADER + person.getEmails().toString());
        links.setText(LINKS_HEADER + person.getLinks().toString());
        person.getGraduation().ifPresentOrElse((Graduation g) ->
                graduation.setText(GRADUATION_HEADER + g.getFullString()), () -> graduation.setVisible(false));
        courses.setText(COURSE_HEADER + person.getCourses().toString());
        specialisations.setText(SPECIALISATION_HEADER + person.getSpecialisations().toString());
        person.getTags().stream()
                .forEach(tag -> tags.getChildren().add(new Label(tag.getValue())));
        person.getPriority().ifPresentOrElse((Priority p) ->
                        priority.setText(PRIORITY_HEADER + p), () -> priority.setVisible(false));
    }

    public Label getPhones() {
        return phones;
    }
    public Label getGraduation() {
        return graduation;
    }
    public Label getCourse() {
        return courses;
    }
    public Label getSpecialisations() {
        return specialisations;
    }
    public Label getPriority() {
        return priority; // getter method for testing
    }
}
