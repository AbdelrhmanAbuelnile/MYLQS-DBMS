import java.util.*;


class Field<T> {
    T value;
    String name;

    public Field(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Field<?> other = (Field<?>) obj;
        return Objects.equals(value, other.value) && Objects.equals(name, other.name);
    }



    @SuppressWarnings("unchecked")
    public void plus(Field<T> other) {
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