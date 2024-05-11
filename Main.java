import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Record implements Serializable {
    private Map<String, Object> fields;
    private Map<String, String> columnTypes;

    public Record() {
        this.fields = new HashMap<>();
        this.columnTypes = new HashMap<>();
    }

    public void addField(String name, Object value, String type) {
        fields.put(name, value);
        columnTypes.put(name, type);
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public Map<String, String> getColumnTypes() {
        return columnTypes;
    }

    @Override
    public String toString() {
        return "Record{" +
                "fields=" + fields +
                ", columnTypes=" + columnTypes +
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

    public void deleteRecord(int index) {
        if (index >= 0 && index < records.size()) {
            records.remove(index);
        } else {
            throw new IllegalArgumentException("Invalid record index");
        }
    }

    public List<Record> getRecords() {
        return records;
    }

    public List<Integer> findRecords(Map<String, Object> constraints) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            boolean matches = true;
            for (Map.Entry<String, Object> entry : constraints.entrySet()) {
                String columnName = entry.getKey();
                Object value = entry.getValue();
                if (!record.getFields().containsKey(columnName) || !record.getFields().get(columnName).equals(value)) {
                    matches = false;
                    break;
                }
            }
            if (matches) {
                indices.add(i);
            }
        }
        return indices;
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

    public void deleteFromTable(String tableName, Map<String, Object> constraints) {
        Table table = tables.get(tableName);
        if (table == null) {
            throw new IllegalArgumentException("No table with name " + tableName + " exists.");
        }
        List<Integer> indicesToDelete = table.findRecords(constraints);
        for (Integer index : indicesToDelete) {
            table.deleteRecord(index);
        }
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
        Path filePath = outputPath.resolve(fileName + ".bin");
        if (!Files.exists(filePath)) {
            throw new IOException("Database file not found for: " + fileName);
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new FileInputStream(filePath.toString()))) {
            return (Database) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Error loading database: " + e.getMessage());
        }
    }
}

public class Main {

    public static void insertInto(String fileName, String tableName, Scanner scanner) {
        try {
            Database loadedDb = Database.loadFromFile(fileName);
            Table table = loadedDb.getTable(tableName);
            if (table == null) {
                System.out.println("No table with name " + tableName + " exists.");
                return;
            }
            Record record = new Record();
            Map<String, String> columnTypes = loadedDb.getTable(tableName).getRecords().get(0).getColumnTypes();
            for (Map.Entry<String, String> entry : columnTypes.entrySet()) {
                String columnName = entry.getKey();
                String columnType = entry.getValue();
                System.out.println("Enter value for " + columnName + " (" + columnType + "): ");
                String value = scanner.nextLine();
                Object typedValue;
                switch (columnType.trim().toLowerCase()) {
                    case "string":
                        typedValue = value;
                        break;
                    case "int":
                        typedValue = Integer.parseInt(value);
                        break;
                    case "float":
                        typedValue = Float.parseFloat(value);
                        break;
                    case "boolean":
                        typedValue = Boolean.parseBoolean(value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid column type: " + columnType);
                }
                record.addField(columnName, typedValue, columnType);
            }
            loadedDb.writeToDatabase(fileName, tableName, record);
            System.out.println("Record inserted successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void deleteRecord(String fileName, String tableName, Scanner scanner) {
        try {
            Database loadedDb = Database.loadFromFile(fileName);
            Table table = loadedDb.getTable(tableName);
            if (table == null) {
                System.out.println("No table with name " + tableName + " exists.");
                return;
            }

            Map<String, Object> constraints = new HashMap<>();
            Map<String, String> columnTypes = loadedDb.getTable(tableName).getRecords().get(0).getColumnTypes();
            for (Map.Entry<String, String> entry : columnTypes.entrySet()) {
                String columnName = entry.getKey();
                String columnType = entry.getValue();
                System.out.println("Enter value for " + columnName + " (" + columnType + ") to delete by: ");
                String value = scanner.nextLine();
                Object typedValue;
                switch (columnType.trim().toLowerCase()) {
                    case "string":
                        typedValue = value;
                        break;
                    case "int":
                        typedValue = Integer.parseInt(value);
                        break;
                    case "float":
                        typedValue = Float.parseFloat(value);
                        break;
                    case "boolean":
                        typedValue = Boolean.parseBoolean(value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid column type: " + columnType);
                }
                constraints.put(columnName, typedValue);
            }

            loadedDb.deleteFromTable(tableName, constraints);
            Path outputPath = Paths.get("output");
            Path filePath = outputPath.resolve(fileName + ".bin");
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath.toString()))) {
                outputStream.writeObject(loadedDb);
            }
            System.out.println("Record(s) deleted successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void readFrom(String fileName, Scanner scanner) {
        try {
            Database loadedDb = Database.loadFromFile(fileName);
            Set<String> tableNames = loadedDb.getTables();
            int totalRecords = 0;
            for (String tableName : tableNames) {
                Table table = loadedDb.getTable(tableName);
                List<Record> loadedRecords = table.getRecords();
                totalRecords += loadedRecords.size();
                System.out.println("Records for table " + tableName + ":");
                for (int i = 0; i < loadedRecords.size(); i++) {
                    System.out.println("Index: " + i + " " + loadedRecords.get(i));
                }
            }
            System.out.println("Total records: " + totalRecords);

            System.out.println("Do you want to insert, delete, or update a record? (insert/delete/update/no): ");
            String choice = scanner.nextLine();
            switch (choice.toLowerCase()) {
                case "insert":
                    System.out.println("Enter the table name to insert into: ");
                    String tableNameInsert = scanner.nextLine();
                    insertInto(fileName, tableNameInsert, scanner);
                    break;
                case "delete":
                    System.out.println("Enter the table name to delete from: ");
                    String tableNameDelete = scanner.nextLine();
                    deleteRecord(fileName, tableNameDelete, scanner);
                    break;
                case "update":
                    System.out.println("Update functionality not implemented yet.");
                    break;
                case "no":
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
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
            switch (userCommand[0]) {
                case "use":
                    readFrom(userCommand[1], scanner);
                    break;

                case "create":
                    Database db = new Database();
                    db.createDatabase(userCommand[1]);
                    System.out.println("Enter your Table name: ");
                    String tableName = scanner.nextLine();
                    db.createTable(tableName);
                    System.out.println("Enter your column names and types (separated by commas, e.g., name:string, age:int): ");
                    String columnInput = scanner.nextLine();
                    String[] columnsWithType = columnInput.split(",");
                    Record record = new Record();
                    for (String colWithType : columnsWithType) {
                        String[] parts = colWithType.split(":");
                        String columnName = parts[0];
                        String columnType = parts[1];
                        System.out.println("Enter value for " + columnName + ": ");
                        String value = scanner.nextLine();
                        Object typedValue;
                        switch (columnType.trim().toLowerCase()) {
                            case "string":
                                typedValue = value;
                                break;
                            case "int":
                                typedValue = Integer.parseInt(value);
                                break;
                            case "float":
                                typedValue = Float.parseFloat(value);
                                break;
                            case "boolean":
                                typedValue = Boolean.parseBoolean(value);
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid column type: " + columnType);
                        }
                        record.addField(columnName, typedValue, columnType);
                    }
                    db.writeToDatabase(userCommand[1], tableName, record);
                    readFrom(userCommand[1], scanner);
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
