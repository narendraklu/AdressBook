import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AddressBook {
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_NAME = "contacts.txt";
    static List<Contact> contacts = new ArrayList<>();

    static class Contact {
        String name;
        String phone;
        String email;

        Contact(String name, String phone, String email) {
            this.name = name;
            this.phone = phone;
            this.email = email;
        }
    }

    public static void main(String[] args) {
        loadContacts();
            while (true) {
            System.out.println("1. Add Contact");
            System.out.println("2. List Contacts");
            System.out.println("3. Exit");
            System.out.println("4. Delete Contact");
            System.out.println("5. Search Contact");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            if (choice == 1) {
                addContact();
            } else if (choice == 2) {
                listContacts();
            } else if (choice == 3) {
                break;
            }
              else if (choice == 4) {
              deleteContact();
            } else if (choice == 5) {
            searchContact();
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

        try {
        FileWriter fw = new FileWriter(FILE_NAME, true); // true means append mode
        fw.write(name + "," + phone + "," + email + "\n");
        fw.close();
    } catch (IOException e) {
        System.out.println("Error writing to file: " + e.getMessage());
    }

        

    }

    static void listContacts() {
        for (Contact c : contacts) {
            System.out.println(c.name + " | " + c.phone + " | " + c.email);
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
            if (parts.length == 3) {
                contacts.add(new Contact(parts[0], parts[1], parts[2]));
            }
        }
        fileScanner.close();
    } catch (IOException e) {
        System.out.println("Error loading contacts: " + e.getMessage());
    }
}
static void deleteContact() {
    System.out.print("Enter name to delete: ");
    String nameToDelete = scanner.nextLine();
    boolean removed = contacts.removeIf(c -> c.name.equalsIgnoreCase(nameToDelete));

    if (removed) {
        System.out.println("Contact deleted!");
        saveAllContacts(); // Rewrite entire file
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
            System.out.println(c.name + " | " + c.phone + " | " + c.email);
            found = true;
        }
    }
    if (!found) {
        System.out.println("Contact not found.");
    }
}
static void saveAllContacts() {
    try {
        FileWriter fw = new FileWriter(FILE_NAME, false); // false = overwrite
        for (Contact c : contacts) {
            fw.write(c.name + "," + c.phone + "," + c.email + "\n");
        }
        fw.close();
    } catch (IOException e) {
        System.out.println("Error saving contacts: " + e.getMessage());
    }
}







}



