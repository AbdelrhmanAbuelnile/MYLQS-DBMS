import java.io.Serializable;
import java.util.ArrayList;


class Table implements Serializable {

    private ArrayList<Record> records;
   

    public Table(Record rec) {
        this.records = new ArrayList<Record> ();
        this.records.add(rec);
    } 


    public void insert(Record rec) {
        ArrayList<Field<?>> fields = rec.get_fields();
        if (fields.size() != records.get(0).get_fields().size()) {
            return;
        }
        for (int i = 0; i < fields.size(); i++) {
            if (!fields.get(i).name.equals(this.records.get(0).get_fields().get(i).name)) {
                System.err.println("Invalid input");
                return;
            }
        }

        records.add(rec);
    }


    public void select() {
        records.get(0).print_record_labels();
        int size = records.size();
        for (int i = 0; i < size; i++) {
            records.get(i).print_record_values();
        }
        System.out.println();
    }
}



    // If one value does not match the other. Print error
    // Map<Field<?>, Record> tableData;

 // public Table(Field<?> object, Record record) {
    //     //The object must be in the record.
    //     this.tableData = new HashMap<>();
    //     Boolean flg = false;
    //     for (Field<?> obj : record.get_fields()) {
    //         if(obj == object) {
    //             flg = true;
    //             break;
    //         }
    //     }
    //     if(flg == false) {
    //         System.err.println("The key attribute is not in the record");
    //     } else {
    //         tableData.put(object, record);
    //     }
    // }