import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.time.LocalDateTime;

public class AddressBook {
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_NAME = "contacts.txt";
    static List<Contact> contacts = new ArrayList<>();

    public static void main(String[] args) {
        loadContacts();
        while (true) {
            System.out.println("\n===== Address Book Menu =====");
            System.out.println("1. Add Contact");
            System.out.println("2. List Contacts");
            System.out.println("3. Exit");
            System.out.println("4. Delete Contact");
            System.out.println("5. Search Contact");
            System.out.println("6. Edit Contact");
            System.out.println("7. Export Contacts");
            System.out.println("8. Import Contacts");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            if (choice == 1) {
                addContact();
            } else if (choice == 2) {
                listContacts();
            } else if (choice == 3) {
                break;
            } else if (choice == 4) {
                deleteContact();
            } else if (choice == 5) {
                searchContact();
            } else if (choice == 6) {
                editContact();
            } else if (choice == 7) {
                exportContacts();
            }else if (choice == 8) {
              importContacts();
            }

            
            else {
                System.out.println("Invalid option!");
            }
        }
    }

    static void addContact() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        contacts.add(new Contact(name, phone, email));
        System.out.println("Contact added!");

        saveAllContacts();
    }

    static void listContacts() {
        for (Contact c : contacts) {
            System.out.println(c.name + " | " + c.phone + " | " + c.email + " | Added on: " + c.createdAt);
        }
    }

    static void loadContacts() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Contact c = new Contact(parts[0], parts[1], parts[2]);
                    c.createdAt = LocalDateTime.parse(parts[3]);
                    contacts.add(c);
                }
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }
    }

    static void saveAllContacts() {
        try {
            FileWriter fw = new FileWriter(FILE_NAME, false); // overwrite
            for (Contact c : contacts) {
                fw.write(c.name + "," + c.phone + "," + c.email + "," + c.createdAt + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }

    static void deleteContact() {
        System.out.print("Enter name to delete: ");
        String nameToDelete = scanner.nextLine();
        boolean removed = contacts.removeIf(c -> c.name.equalsIgnoreCase(nameToDelete));

        if (removed) {
            System.out.println("Contact deleted!");
            saveAllContacts();
        } else {
            System.out.println("Contact not found.");
        }
    }

    static void searchContact() {
        System.out.print("Enter name to search: ");
        String nameToSearch = scanner.nextLine();
        boolean found = false;
        for (Contact c : contacts) {
            if (c.name.equalsIgnoreCase(nameToSearch)) {
                System.out.println(c.name + " | " + c.phone + " | " + c.email + " | Added on: " + c.createdAt);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Contact not found.");
        }
    }

    static void editContact() {
        System.out.print("Enter name to edit: ");
        String nameToEdit = scanner.nextLine();
        boolean found = false;

        for (Contact c : contacts) {
            if (c.name.equalsIgnoreCase(nameToEdit)) {
                System.out.print("New phone (enter to skip): ");
                String newPhone = scanner.nextLine();
                if (!newPhone.isEmpty()) {
                    c.phone = newPhone;
                }

                System.out.print("New email (enter to skip): ");
                String newEmail = scanner.nextLine();
                if (!newEmail.isEmpty()) {
                    c.email = newEmail;
                }

                found = true;
                saveAllContacts();
                System.out.println("Contact updated!");
                break;
            }
        }

        if (!found) {
            System.out.println("Contact not found.");
        }
    }

    static void exportContacts() {
        try {
            FileWriter fw = new FileWriter("exported_contacts.csv", false); // overwrite
            fw.write("Name,Phone,Email,CreatedAt\n"); // header
            for (Contact c : contacts) {
                fw.write(c.name + "," + c.phone + "," + c.email + "," + c.createdAt + "\n");
            }
            fw.close();
            System.out.println("Contacts exported successfully to exported_contacts.csv!");
        } catch (IOException e) {
            System.out.println("Error exporting contacts: " + e.getMessage());
        }
    }
    static void importContacts() {
    try {
        File file = new File("import_contacts.csv");
        if (!file.exists()) {
            System.out.println("import_contacts.csv not found!");
            return;
        }

        Scanner fileScanner = new Scanner(file);
        // Skip header if present
        if (fileScanner.hasNextLine()) {
            fileScanner.nextLine();
        }

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length == 4) {
                Contact c = new Contact(parts[0], parts[1], parts[2]);
                c.createdAt = LocalDateTime.parse(parts[3]);
                contacts.add(c);
            }
        }
        fileScanner.close();
        saveAllContacts();
        System.out.println("Contacts imported successfully!");
    } catch (IOException e) {
        System.out.println("Error importing contacts: " + e.getMessage());
    }
}

}

class Contact {
    String name;
    String phone;
    String email;
    LocalDateTime createdAt;

    Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.createdAt = LocalDateTime.now();
    }
}
