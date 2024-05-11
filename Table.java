import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

class Table implements Serializable {
    // If one value does not match the other. Print error
    Map<Field<?>, Record> tableData;

    public Table(Field<?> object, Record record) {
        //The object must be in the record.
        this.tableData = new HashMap<>();
        Boolean flg = false;
        for (Field<?> obj : record.get_fields()) {
            if(obj == object) {
                flg = true;
                break;
            }
        }
        if(flg == false) {
            System.err.println("The key attribute is not in the record");
        } else {
            tableData.put(object, record);
        }
    }
}