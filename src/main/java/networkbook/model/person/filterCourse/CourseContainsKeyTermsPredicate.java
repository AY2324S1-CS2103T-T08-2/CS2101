package networkbook.model.person.filterCourse;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import networkbook.commons.util.StringUtil;
import networkbook.commons.util.ToStringBuilder;
import networkbook.model.person.Course;
import networkbook.model.person.Person;

/**
 * Tests that at least one of a Person's courses contains one of the given key terms.
 */
public class CourseContainsKeyTermsPredicate implements Predicate<Person> {
    private final List<String> keyTerms;

    public CourseContainsKeyTermsPredicate(List<String> keyTerms) {
        this.keyTerms = keyTerms;
    }

    @Override
    public boolean test(Person person) {
        return keyTerms.stream()
                .anyMatch(keyTerm -> person.getCourses().stream()
                        .anyMatch(course -> StringUtil.containsTermIgnoreCase(course.getCourse(), keyTerm)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CourseContainsKeyTermsPredicate)) {
            return false;
        }

        CourseContainsKeyTermsPredicate otherCourseContainsKeyTermsPredicate = (CourseContainsKeyTermsPredicate) other;
        return keyTerms.equals(otherCourseContainsKeyTermsPredicate.keyTerms);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("key terms", keyTerms).toString();
    }

    public List<String> getKeyTerms() {
        return keyTerms;
    }

    /**
     * Gets all courses that match any of the key terms.
     */
    public List<Course> getCourses(Person person) {
        return person.getCourses()
                .stream()
                .filter(course ->
                        keyTerms.stream()
                                .anyMatch(keyTerm -> StringUtil.containsTermIgnoreCase(course.getCourse(), keyTerm)))
                .collect(Collectors.toList());
    }
}
