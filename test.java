// import java.io.*;
// // import java.nio.file.Files;
// // import java.nio.file.Path;
// // import java.nio.file.Paths;
// import java.util.*;

import java.util.ArrayList;

class test { 
    public static void main(String[] args) {
        Table table = null;

        for (int i = 0; i < 5; ++i) {
            Field<Integer> id = new Field<Integer>("id", i);
            Field<String> name = new Field<String>("name", "Dummy" + i);
            Field<Double> salary = new Field<Double>("salary", (i + 1) * 1500.0);
            Record record = new Record(id, name, salary);
            
            if (table == null) {
                table = new Table(record);
            } else {
                table.insert(record);
            }
        }
      
        if (table != null)
            table.select();

       
    }
}