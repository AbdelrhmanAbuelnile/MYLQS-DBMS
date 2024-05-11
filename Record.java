import java.util.*;
import java.io.*;

class Record implements Serializable {
    private ArrayList<Field<?>> fields;

    public ArrayList<Field<?>> get_fields() {
        return this.fields;
    }

    public Record(Field<?>... fields) {
        this.fields = new ArrayList<>();
        for (Field<?> field : fields) {
            this.fields.add(field);
        }
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

    public void print_record_labels() {
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            System.out.print(fields.get(i).name);
            if (i < size - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();  // Move to the next line after printing all labels
    }

    public void print_record_values() {
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            System.out.print(fields.get(i).value);
            if (i < size - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();  // Move to the next line after printing all labels
    }
}