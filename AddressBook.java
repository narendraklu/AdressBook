import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class AddressBook {
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_NAME = "contacts.txt";
    static List<Contact> contacts = new ArrayList<>();

    public static void main(String[] args) {
        if (!checkPassword()) {
            System.out.println("🔐 Exiting program.");
            return;
        }

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
            System.out.println("10. Restore Contacts");
            System.out.println("11. Mark/Unmark Favorite");
            System.out.println("12. List Favorite Contacts");
            System.out.println("13. Upcoming Birthdays");
            System.out.println("14. Change Password");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) addContact();
            else if (choice == 2) listContacts();
            else if (choice == 3) break;
            else if (choice == 4) deleteContact();
            else if (choice == 5) searchContact();
            else if (choice == 6) editContact();
            else if (choice == 7) exportContacts();
            else if (choice == 8) importContacts();
            else if (choice == 9) backupContacts();
            else if (choice == 10) restoreContacts();
            else if (choice == 11) toggleFavorite();
            else if (choice == 12) listFavorites();
            else if (choice == 13) listUpcomingBirthdays();
            else if (choice == 14) changePassword();
            else System.out.println("❌ Invalid option.");
        }
    }

    static boolean checkPassword() {
        File file = new File("password.txt");
        try {
            if (!file.exists()) {
                System.out.print("🔒 No password set. Create one: ");
                String newPassword = scanner.nextLine();
                String hash = hashPassword(newPassword);
                FileWriter fw = new FileWriter(file);
                fw.write(hash);
                fw.close();
                System.out.println("✅ Password set!");
                return true;
            }

            Scanner reader = new Scanner(file);
            String storedHash = reader.nextLine();
            reader.close();

            System.out.print("🔐 Enter password: ");
            String entered = scanner.nextLine();
            String enteredHash = hashPassword(entered);

            if (storedHash.equals(enteredHash)) {
                System.out.println("✅ Access granted.");
                return true;
            } else {
                System.out.println("❌ Incorrect password!");
                return false;
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error: " + e.getMessage());
            return false;
        }
    }

    static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static void changePassword() {
        try {
            File file = new File("password.txt");
            if (!file.exists()) {
                System.out.println("⚠️ Password file missing. Cannot change password.");
                return;
            }

            Scanner fileScanner = new Scanner(file);
            String storedHash = fileScanner.nextLine();
            fileScanner.close();

            System.out.print("Enter current password: ");
            String oldPass = scanner.nextLine();
            String oldHash = hashPassword(oldPass);

            if (!storedHash.equals(oldHash)) {
                System.out.println("❌ Incorrect current password. Password not changed.");
                return;
            }

            System.out.print("Enter new password: ");
            String newPass = scanner.nextLine();
            System.out.print("Confirm new password: ");
            String confirmPass = scanner.nextLine();

            if (!newPass.equals(confirmPass)) {
                System.out.println("❌ Passwords do not match. Try again.");
                return;
            }

            String newHash = hashPassword(newPass);
            FileWriter fw = new FileWriter(file, false);
            fw.write(newHash);
            fw.close();
            System.out.println("✅ Password changed successfully!");
        } catch (IOException e) {
            System.out.println("⚠️ Error changing password: " + e.getMessage());
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
        LocalDate birthday = LocalDate.parse(scanner.nextLine());

        contacts.add(new Contact(name, phone, email, birthday));
        saveAllContacts();
        System.out.println("✅ Contact added.");
    }

    static void listContacts() {
        for (Contact c : contacts) {
            String fav = c.isFavorite ? "⭐" : "";
            System.out.println(c.name + " | " + c.phone + " | " + c.email + " | " + c.birthday + " | " + c.createdAt + " " + fav);
        }
    }

    static void deleteContact() {
        System.out.print("Enter name to delete: ");
        String name = scanner.nextLine();
        boolean removed = contacts.removeIf(c -> c.name.equalsIgnoreCase(name));
        if (removed) {
            saveAllContacts();
            System.out.println("✅ Contact deleted.");
        } else System.out.println("❌ Contact not found.");
    }

    static void searchContact() {
        System.out.print("Search name: ");
        String name = scanner.nextLine();
        boolean found = false;
        for (Contact c : contacts) {
            if (c.name.equalsIgnoreCase(name)) {
                System.out.println(c.name + " | " + c.phone + " | " + c.email + " | " + c.birthday + " | " + c.createdAt);
                found = true;
            }
        }
        if (!found) System.out.println("❌ Not found.");
    }

    static void editContact() {
        System.out.print("Enter name to edit: ");
        String name = scanner.nextLine();
        for (Contact c : contacts) {
            if (c.name.equalsIgnoreCase(name)) {
                System.out.print("New phone (enter to skip): ");
                String phone = scanner.nextLine();
                if (!phone.isEmpty()) c.phone = phone;

                System.out.print("New email (enter to skip): ");
                String email = scanner.nextLine();
                if (!email.isEmpty()) c.email = email;

                System.out.print("New birthday (yyyy-mm-dd, skip if blank): ");
                String bday = scanner.nextLine();
                if (!bday.isEmpty()) c.birthday = LocalDate.parse(bday);

                saveAllContacts();
                System.out.println("✅ Contact updated.");
                return;
            }
        }
        System.out.println("❌ Contact not found.");
    }

    static void toggleFavorite() {
        System.out.print("Enter name to favorite/unfavorite: ");
        String name = scanner.nextLine();
        for (Contact c : contacts) {
            if (c.name.equalsIgnoreCase(name)) {
                c.isFavorite = !c.isFavorite;
                saveAllContacts();
                System.out.println("✅ Favorite status toggled.");
                return;
            }
        }
        System.out.println("❌ Contact not found.");
    }

    static void listFavorites() {
        boolean found = false;
        for (Contact c : contacts) {
            if (c.isFavorite) {
                System.out.println(c.name + " | " + c.phone + " | " + c.email + " | " + c.birthday + " ⭐");
                found = true;
            }
        }
        if (!found) System.out.println("😢 No favorites yet.");
    }

    static void listUpcomingBirthdays() {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusDays(30);
        boolean found = false;
        for (Contact c : contacts) {
            if (c.birthday != null) {
                LocalDate bdayThisYear = c.birthday.withYear(today.getYear());
                if (!bdayThisYear.isBefore(today) && !bdayThisYear.isAfter(nextMonth)) {
                    System.out.println(c.name + " | Birthday: " + c.birthday);
                    found = true;
                }
            }
        }
        if (!found) System.out.println("🎉 No upcoming birthdays!");
    }

    static void importContacts() {
        File file = new File("import_contacts.csv");
        if (!file.exists()) {
            System.out.println("❌ import_contacts.csv missing.");
            return;
        }

        try {
            Scanner sc = new Scanner(file);
            if (sc.hasNextLine()) sc.nextLine();

            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length == 6) {
                    Contact c = new Contact(parts[0], parts[1], parts[2], LocalDate.parse(parts[5]));
                    c.createdAt = LocalDateTime.parse(parts[3]);
                    c.isFavorite = Boolean.parseBoolean(parts[4]);
                    contacts.add(c);
                }
            }
            sc.close();
            saveAllContacts();
            System.out.println("✅ Contacts imported.");
        } catch (Exception e) {
            System.out.println("⚠️ Import failed: " + e.getMessage());
        }
    }

    static void exportContacts() {
        try {
            FileWriter fw = new FileWriter("exported_contacts.csv");
            fw.write("Name,Phone,Email,CreatedAt,IsFavorite,Birthday\n");
            for (Contact c : contacts) {
                fw.write(c.name + "," + c.phone + "," + c.email + "," + c.createdAt + "," + c.isFavorite + "," + c.birthday + "\n");
            }
            fw.close();
            System.out.println("✅ Exported to exported_contacts.csv.");
        } catch (IOException e) {
            System.out.println("❌ Export error: " + e.getMessage());
        }
    }

    static void backupContacts() {
        try {
            File in = new File(FILE_NAME);
            File out = new File("contacts_backup.txt");
            Scanner sc = new Scanner(in);
            FileWriter fw = new FileWriter(out);

            while (sc.hasNextLine()) fw.write(sc.nextLine() + "\n");

            sc.close();
            fw.close();
            System.out.println("✅ Backup created.");
        } catch (IOException e) {
            System.out.println("⚠️ Backup failed: " + e.getMessage());
        }
    }

    static void restoreContacts() {
        try {
            File in = new File("contacts_backup.txt");
            File out = new File(FILE_NAME);
            if (!in.exists()) {
                System.out.println("❌ Backup not found.");
                return;
            }

            Scanner sc = new Scanner(in);
            FileWriter fw = new FileWriter(out);

            while (sc.hasNextLine()) fw.write(sc.nextLine() + "\n");

            sc.close();
            fw.close();
            contacts.clear();
            loadContacts();
            System.out.println("✅ Restored from backup.");
        } catch (IOException e) {
            System.out.println("⚠️ Restore failed: " + e.getMessage());
        }
    }

    static void loadContacts() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return;

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(",");
                if (parts.length == 6) {
                    Contact c = new Contact(parts[0], parts[1], parts[2], LocalDate.parse(parts[5]));
                    c.createdAt = LocalDateTime.parse(parts[3]);
                    c.isFavorite = Boolean.parseBoolean(parts[4]);
                    contacts.add(c);
                }
            }
            sc.close();
        } catch (IOException e) {
            System.out.println("⚠️ Load failed: " + e.getMessage());
        }
    }

    static void saveAllContacts() {
        try {
            FileWriter fw = new FileWriter(FILE_NAME);
            for (Contact c : contacts) {
                fw.write(c.name + "," + c.phone + "," + c.email + "," + c.createdAt + "," + c.isFavorite + "," + c.birthday + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println("⚠️ Save failed: " + e.getMessage());
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
