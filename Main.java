import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Record implements Serializable {
    private Map<String, Object> fields;

    public Record() {
        this.fields = new HashMap<>();
    }

    public void addField(String name, Object value) {
        fields.put(name, value);
    }

    @Override
    public String toString() {
        return "Record{" +
                "fields=" + fields +
                '}';
    }
}

class Table implements Serializable {
    private List<Record> records;

    public Table() {
        this.records = new ArrayList<>();
    }

    public void addRecord(Record record) {
        records.add(record);
    }

    public List<Record> getRecords() {
        return records;
    }
}

class Database implements Serializable {
    private Map<String, Table> tables;

    public Database() {
        this.tables = new HashMap<>();
    }

    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    public Set<String> getTables() {
        return tables.keySet();
    }

    public void createTable(String tableName) {
        if (tables.containsKey(tableName)) {
            throw new IllegalArgumentException("Table with name " + tableName + " already exists.");
        }
        tables.put(tableName, new Table());
    }

    public void writeToTable(String tableName, Record record) {
        Table table = tables.get(tableName);
        if (table == null) {
            throw new IllegalArgumentException("No table with name " + tableName + " exists.");
        }
        table.addRecord(record);
    }

    public void createDatabase(String fileName) throws IOException {
        Path outputPath = Paths.get("output");
        if (!Files.exists(outputPath)) {
            Files.createDirectories(outputPath);
        }
        Path filePath = outputPath.resolve(fileName + ".bin");
        if (Files.exists(filePath)) {
            throw new IOException("Database with name " + fileName + " already exists.");
        }
    }

    public void writeToDatabase(String fileName, String tableName, Record record) throws IOException {
        Table table = tables.get(tableName);
        if (table == null) {
            throw new IllegalArgumentException("No table with name " + tableName + " exists.");
        }
        table.addRecord(record);
        Path outputPath = Paths.get("output");
        Path filePath = outputPath.resolve(fileName + ".bin");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath.toString()))) {
            outputStream.writeObject(this);
        }
    }

    public static Database loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        Path outputPath = Paths.get("output");
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream(outputPath.resolve(fileName + ".bin").toString()))) {
            return (Database) inputStream.readObject();
        }
    }
}

public class Main {

    public static void readFrom(String fileName) {
        try {
            Database loadedDb = Database.loadFromFile(fileName);
            Set<String> tableNames = loadedDb.getTables();
            int totalRecords = 0;
            for (String tableName : tableNames) {
                Table table = loadedDb.getTable(tableName);
                List<Record> loadedRecords = table.getRecords();
                totalRecords += loadedRecords.size();
                System.out.println("Records for table " + tableName + ":");
                for (Record record : loadedRecords) {
                    System.out.println(record);
                }
            }
            System.out.println("Total records: " + totalRecords);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No database with name " + fileName + " exists.");
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Welcome to the Database Management System!");
        System.out.println("Enter your Database name: ");
        String userInput = scanner.nextLine();
        String[] userCommand = userInput.toLowerCase().split(" ", 2);
        if (!userCommand[0].equals("use") && !userCommand[0].equals("create")) {
            System.out.println(userCommand[0] + " is not a valid command");
            scanner.close();
            return;
        }
        try {
            Database db = new Database();
            switch (userCommand[0]) {
                case "use":
                    readFrom(userCommand[1]);
                    break;

                case "create":
                    db.createDatabase(userCommand[1]);
                    System.out.println("Enter your Table name: ");
                    String tableName = scanner.nextLine();
                    // db.createTable(tableName);
                    System.out.println("Enter your column names (separated by commas): ");
                    String columnInput = scanner.nextLine();
                    String[] columns = columnInput.split(",");
                    System.out.println("Enter your Record data (format: value1,value2,...): ");
                    String recordInput = scanner.nextLine();
                    String[] values = recordInput.split(",");
                    if (columns.length != values.length) {
                        System.out.println("Number of values does not match number of columns.");
                        return;
                    }
                    Record record = new Record();
                    for (int i = 0; i < columns.length; i++) {
                        record.addField(columns[i], values[i]);
                    }
                    db.createTable(tableName);
                    db.writeToDatabase(userCommand[1], tableName, record);
                    readFrom(userCommand[1]);
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        } finally {
            scanner.close();
        }
        System.out.println();
    }
}