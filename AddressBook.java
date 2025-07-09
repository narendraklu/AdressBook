import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
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
            System.out.println("9. Backup Contacts");
            System.out.println("10. Restore Contacts from Backup");
            System.out.println("11. Mark/Unmark Favorite");
            System.out.println("12. List Only Favorite Contacts");
            System.out.println("13. Show Upcoming Birthdays");
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
            } else if (choice == 8) {
                importContacts();
            } else if (choice == 9) {
                backupContacts();
            } else if (choice == 10) {
                restoreContacts();
            } else if (choice == 11) {
                toggleFavorite();
            } else if (choice == 12) {
                listFavorites();
            } else if (choice == 13) {
                listUpcomingBirthdays();
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
        System.out.print("Birthday (yyyy-mm-dd): ");
        String birthdayStr = scanner.nextLine();
        LocalDate birthday = LocalDate.parse(birthdayStr);

        contacts.add(new Contact(name, phone, email, birthday));
        System.out.println("Contact added!");
        saveAllContacts();
    }

    static void listContacts() {
        for (Contact c : contacts) {
            String favMark = c.isFavorite ? "⭐" : "";
            System.out.println(c.name + " | " + c.phone + " | " + c.email + " | Birthday: " + c.birthday
                    + " | Added on: " + c.createdAt + " " + favMark);
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
                if (parts.length == 6) {
                    Contact c = new Contact(parts[0], parts[1], parts[2], LocalDate.parse(parts[5]));
                    c.createdAt = LocalDateTime.parse(parts[3]);
                    c.isFavorite = Boolean.parseBoolean(parts[4]);
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
            FileWriter fw = new FileWriter(FILE_NAME, false);
            for (Contact c : contacts) {
                fw.write(c.name + "," + c.phone + "," + c.email + "," + c.createdAt + "," + c.isFavorite + ","
                        + c.birthday + "\n");
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
                String favMark = c.isFavorite ? "⭐" : "";
                System.out.println(c.name + " | " + c.phone + " | " + c.email + " | Birthday: " + c.birthday
                        + " | Added on: " + c.createdAt + " " + favMark);
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

                System.out.print("New birthday (yyyy-mm-dd, enter to skip): ");
                String newBirthdayStr = scanner.nextLine();
                if (!newBirthdayStr.isEmpty()) {
                    c.birthday = LocalDate.parse(newBirthdayStr);
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
            FileWriter fw = new FileWriter("exported_contacts.csv", false);
            fw.write("Name,Phone,Email,CreatedAt,IsFavorite,Birthday\n");
            for (Contact c : contacts) {
                fw.write(c.name + "," + c.phone + "," + c.email + "," + c.createdAt + "," + c.isFavorite + ","
                        + c.birthday + "\n");
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
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine(); // skip header
            }

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Contact c = new Contact(parts[0], parts[1], parts[2], LocalDate.parse(parts[5]));
                    c.createdAt = LocalDateTime.parse(parts[3]);
                    c.isFavorite = Boolean.parseBoolean(parts[4]);
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

    static void backupContacts() {
        try {
            File original = new File(FILE_NAME);
            File backup = new File("contacts_backup.txt");

            Scanner scannerFile = new Scanner(original);
            FileWriter fw = new FileWriter(backup, false);

            while (scannerFile.hasNextLine()) {
                String line = scannerFile.nextLine();
                fw.write(line + "\n");
            }

            scannerFile.close();
            fw.close();
            System.out.println("Backup created successfully as contacts_backup.txt!");
        } catch (IOException e) {
            System.out.println("Error backing up contacts: " + e.getMessage());
        }
    }

    static void restoreContacts() {
        try {
            File backup = new File("contacts_backup.txt");
            File original = new File(FILE_NAME);

            if (!backup.exists()) {
                System.out.println("Backup file does not exist!");
                return;
            }

            Scanner scannerFile = new Scanner(backup);
            FileWriter fw = new FileWriter(original, false);

            while (scannerFile.hasNextLine()) {
                String line = scannerFile.nextLine();
                fw.write(line + "\n");
            }

            scannerFile.close();
            fw.close();

            contacts.clear();
            loadContacts();
            System.out.println("Contacts restored from backup successfully!");
        } catch (IOException e) {
            System.out.println("Error restoring contacts: " + e.getMessage());
        }
    }

    static void toggleFavorite() {
        System.out.print("Enter name to mark/unmark favorite: ");
        String nameToToggle = scanner.nextLine();
        boolean found = false;

        for (Contact c : contacts) {
            if (c.name.equalsIgnoreCase(nameToToggle)) {
                c.isFavorite = !c.isFavorite;
                String status = c.isFavorite ? "marked as favorite ⭐" : "removed from favorites";
                System.out.println("Contact " + status + "!");
                found = true;
                saveAllContacts();
                break;
            }
        }

        if (!found) {
            System.out.println("Contact not found.");
        }
    }

    static void listFavorites() {
        boolean anyFavorite = false;
        for (Contact c : contacts) {
            if (c.isFavorite) {
                System.out.println(c.name + " | " + c.phone + " | " + c.email + " | Birthday: " + c.birthday
                        + " | Added on: " + c.createdAt + " | ⭐");
                anyFavorite = true;
            }
        }
        if (!anyFavorite) {
            System.out.println("No favorite contacts yet!");
        }
    }

    static void listUpcomingBirthdays() {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);
        boolean found = false;

        for (Contact c : contacts) {
            if (c.birthday != null) {
                LocalDate thisYearBirthday = c.birthday.withYear(today.getYear());
                if (!thisYearBirthday.isBefore(today) && !thisYearBirthday.isAfter(thirtyDaysLater)) {
                    System.out.println(c.name + " | Birthday: " + c.birthday);
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No upcoming birthdays in the next 30 days!");
        }
    }
}

class Contact {
    String name;
    String phone;
    String email;
    LocalDateTime createdAt;
    boolean isFavorite;
    LocalDate birthday;

    Contact(String name, String phone, String email, LocalDate birthday) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.isFavorite = false;
        this.birthday = birthday;
    }
}
