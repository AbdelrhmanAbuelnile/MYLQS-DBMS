// import java.io.*;
// // import java.nio.file.Files;
// // import java.nio.file.Path;
// // import java.nio.file.Paths;
// import java.util.*;







class test { 
    public static void main(String[] args) {
        Record record = new Record();

        Field<String> intObject = new Field<>("Abdullah", "Abdullah");
        Field<Integer> stringObject = new Field<>("Id", 123214124);
        Field<Double> doubleObject = new Field<>("Salary", 15000.0);
        Field<Integer> Object = new Field<>("Id", 123214124);
        record.addField(intObject);
        record.addField(stringObject);
        record.addField(doubleObject);

        System.out.println(stringObject == Object);

        System.out.println(record);

    }
}