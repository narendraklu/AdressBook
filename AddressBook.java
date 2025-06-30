import java.util.*;

public class AddressBook {
    static Scanner scanner = new Scanner(System.in);
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
        while (true) {
            System.out.println("1. Add Contact");
            System.out.println("2. List Contacts");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            if (choice == 1) {
                addContact();
            } else if (choice == 2) {
                listContacts();
            } else if (choice == 3) {
                break;
            } else {
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
    }

    static void listContacts() {
        for (Contact c : contacts) {
            System.out.println(c.name + " | " + c.phone + " | " + c.email);
        }
    }
}
