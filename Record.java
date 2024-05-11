import java.util.*;
import java.io.*;

class Record implements Serializable {
    private List<Field<?>> fields;

    public List<Field<?>> get_fields() {
        return this.fields;
    }

    public Record() {
        this.fields = new ArrayList<>();
    }

    public void addField(Field<?> value) {
        fields.add(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Field<?> obj : fields) {
            sb.append(obj.toString()).append(", ");
        }
        if (!fields.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());  // Remove the last comma and space
        }
        sb.append("]");
        return sb.toString();
    }
}