import java.io.*;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
import java.util.*;

class Object<T> {
    T value;
    String name;

    public Object(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    @SuppressWarnings("unchecked")
    public void plus(Object<T> other) {
        if (this.value instanceof String && other.value instanceof String) {
            this.value = (T) ((String) this.value + (String) other.value);
        } else if (this.value instanceof Number && other.value instanceof Number) {
            Number num1 = (Number) this.value;
            Number num2 = (Number) other.value;
            if (this.value instanceof Double || other.value instanceof Double) { // For promoting.
                this.value = (T) Double.valueOf(num1.doubleValue() + num2.doubleValue());
            } else if (this.value instanceof Float || other.value instanceof Float) {
                this.value = (T) Float.valueOf(num1.floatValue() + num2.floatValue());
            } else if (this.value instanceof Long || other.value instanceof Long) {
                this.value = (T) Long.valueOf(num1.longValue() + num2.longValue());
            } else {
                this.value = (T) Integer.valueOf(num1.intValue() + num2.intValue());
            }
        } else {
            throw new IllegalArgumentException("Unsupported type for addition: " + this.value.getClass().getSimpleName());
        }

        
    }

    @SuppressWarnings("unchecked")
    public void plus(T val) {
        if (this.value instanceof String && val instanceof String) {
            this.value = (T) ((String) this.value + (String) val);
        } else if (this.value instanceof Number && val instanceof Number) {
            Number num1 = (Number) this.value;
            Number num2 = (Number) val;
            if (this.value instanceof Double || val instanceof Double) { // For promoting.
                this.value = (T) Double.valueOf(num1.doubleValue() + num2.doubleValue());
            } else if (this.value instanceof Float || val instanceof Float) {
                this.value = (T) Float.valueOf(num1.floatValue() + num2.floatValue());
            } else if (this.value instanceof Long || val instanceof Long) {
                this.value = (T) Long.valueOf(num1.longValue() + num2.longValue());
            } else {
                this.value = (T) Integer.valueOf(num1.intValue() + num2.intValue());
            }
        } else {
            throw new IllegalArgumentException("Unsupported type for addition: " + this.value.getClass().getSimpleName());
        }
    }
    @Override
    public String toString() {
        return name + " = " + value.toString();
    }
}



class Record implements Serializable {
    private List<Object<?>> fields;

    public List<Object<?>> get_fields() {
        return this.fields();
    }

    public Record() {
        this.fields = new ArrayList<>();
    }

    public void addField(Object<?> value) {
        fields.add(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object<?> obj : fields) {
            sb.append(obj.toString()).append(", ");
        }
        if (!fields.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length());  // Remove the last comma and space
        }
        sb.append("]");
        return sb.toString();
    }
}
class Table implements Serializable {
    // If one value does not match the other. Print error
    Map<Object<?>, Record> tableData;

    public Table(Object<?> object, Record record) {
        //The object must be in the record.
        this.tableData = new HashMap<>();
        bool flg = 0;
        for (Object<?> obj : record.get_fields()) {
            if(obj == object) {
                flg = 1;
                break;
            }
        }

        if(flg == 0) {
            System.err.println("The key attribute is not in the record");
        } else {
            tableData.put(object, record);
        }
    }
}


class test { 
    public static void main(String[] args) {
        Record record = new Record();

        Object<String> intObject = new Object<>("Name", "Abdullah");
        Object<Integer> stringObject = new Object<>("Id", 123214124);
        Object<Double> doubleObject = new Object<>("Salary", 15000.0);

        record.addField(intObject);
        record.addField(stringObject);
        record.addField(doubleObject);
    
        System.out.println(record);

    }
}