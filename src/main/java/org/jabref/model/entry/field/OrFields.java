package org.jabref.model.entry.field;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.StringJoiner;
import java.util.TreeSet;

public class OrFields extends TreeSet<Field> implements Comparable<OrFields> {

    private final String notSortedDisplayName;

    public OrFields(Field field) {
        super(Comparator.comparing(Field::getName));
        add(field);
        notSortedDisplayName = field.getDisplayName();
    }

    public OrFields(Field... fields) {
        super(Comparator.comparing(Field::getName));
        addAll(Arrays.asList(fields));
        notSortedDisplayName = concatNamesFromFields(Arrays.asList(fields));
    }

    public OrFields(Collection<Field> fields) {
        super(Comparator.comparing(Field::getName));
        addAll(fields);
        notSortedDisplayName = concatNamesFromFields(fields);
    }

    // Usual DisplayName (columns are ordered alphabetically because of TreeSet)
    public String getDisplayName() {
        return concatNamesFromFields(this);
    }

    // Not sorted DisplayName (columns order from constructor)
    public String getNotSortedDisplayName() { return notSortedDisplayName; }

    // Not sorted list of fields
    public LinkedHashSet<Field> getNotSortedOrFields() {
        LinkedHashSet<Field> result = new LinkedHashSet<>();
        String[] names = notSortedDisplayName.split("/");
        for (int i = 0; i < names.length; i++) {
            for (Field currentField : this) {
                if (currentField.getDisplayName().equals(names[i])) {
                    result.add(currentField);
                    break;
                }
            }
        }
        return result;
    }

    public Field getPrimary() {
        return this.iterator().next();
    }

    @Override
    public int compareTo(OrFields o) {
        return FieldFactory.serializeOrFields(this).compareTo(FieldFactory.serializeOrFields(o));
    }

    private String concatNamesFromFields(Collection<Field> fields) {
        StringJoiner joiner = new StringJoiner("/");
        for (Field field : fields) {
            joiner.add(field.getDisplayName());
        }
        return joiner.toString();
    }
}
