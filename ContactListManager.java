import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Contact List Manager (PS No. 30)
 * Singly linked list based console application.
 * Features: add contact, display all, search by name, delete by ID.
 * Data is loaded from contacts.txt at start and saved to it on exit.
 */

// One contact record
class Contact {
    String id;
    String name;
    String phone;

    Contact(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String toString() {
        return id + " | " + name + " | " + phone;
    }
}

// Node of the singly linked list: data + next
class Node {
    Contact data;
    Node next;

    Node(Contact data) {
        this.data = data;
        this.next = null;
    }
}

public class ContactListManager {

    static final String FILE_NAME = "contacts.txt";

    static Node head = null;      // first node of the list
    static int size = 0;          // number of contacts
    static int nextIdNumber = 101; // next ID will be C101, C102, ...
    static Scanner sc;

    // ---------------------------------------------------------------- main
    public static void main(String[] args) {
        sc = new Scanner(System.in);
        loadFromFile();

        boolean running = true;
        while (running) {
            printMenu();
            if (!sc.hasNextLine()) {      // input finished: save and leave
                break;
            }
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {
                addContact();
            } else if (choice.equals("2")) {
                displayAll();
            } else if (choice.equals("3")) {
                searchByName();
            } else if (choice.equals("4")) {
                deleteById();
            } else if (choice.equals("0")) {
                running = false;
            } else {
                System.out.println("Invalid choice. Please enter 0-4.");
            }
        }

        saveToFile();
        System.out.println("Contacts saved to " + FILE_NAME + ". Goodbye!");
    }

    static void printMenu() {
        System.out.println();
        System.out.println("===== Contact List Manager =====");
        System.out.println("1. Add Contact");
        System.out.println("2. Display All");
        System.out.println("3. Search by Name");
        System.out.println("4. Delete Contact by ID");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    // ----------------------------------------------------------------- add
    static void addContact() {
        System.out.print("Name: ");
        String name = readLine().trim();
        System.out.print("Phone: ");
        String phone = readLine().trim();

        if (name.length() == 0) {
            System.out.println("Name cannot be empty.");
            return;
        }
        if (!isValidPhone(phone)) {
            System.out.println("Invalid phone number. It must have exactly 10 digits.");
            return;
        }
        if (phoneExists(phone)) {
            System.out.println("Duplicate phone number. This number is already saved.");
            return;
        }

        name = name.replace('|', ' ');   // '|' is the separator used in the file
        Contact c = new Contact("C" + nextIdNumber, name, phone);
        nextIdNumber++;
        appendNode(c);
        System.out.println("Contact added. ID: " + c.toString());
    }

    // Adds a node at the end of the linked list
    static void appendNode(Contact c) {
        Node node = new Node(c);
        if (head == null) {
            head = node;
        } else {
            Node cur = head;
            while (cur.next != null) {
                cur = cur.next;
            }
            cur.next = node;
        }
        size++;
    }

    // ------------------------------------------------------------- display
    static void displayAll() {
        if (head == null) {
            System.out.println("No contacts found.");
            return;
        }
        int number = 1;
        Node cur = head;
        while (cur != null) {
            System.out.println(number + ". " + cur.data.toString());
            number++;
            cur = cur.next;
        }
    }

    // -------------------------------------------------------------- search
    // Linear search through the list; partial, case-insensitive match.
    static void searchByName() {
        System.out.print("Enter name to search: ");
        String key = readLine().trim();

        if (key.length() == 0) {
            System.out.println("Search text cannot be empty.");
            return;
        }

        ArrayList<Contact> matches = new ArrayList<Contact>();
        Node cur = head;
        while (cur != null) {
            if (containsText(cur.data.name, key)) {
                matches.add(cur.data);
            }
            cur = cur.next;
        }

        if (matches.size() == 0) {
            System.out.println("Contact not found.");
            return;
        }
        for (int i = 0; i < matches.size(); i++) {
            System.out.println("Found: " + matches.get(i).toString());
        }
    }

    // Own substring search (no built-in contains/indexOf), ignores letter case
    static boolean containsText(String text, String key) {
        int n = text.length();
        int m = key.length();
        if (m > n) {
            return false;
        }
        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m && toLower(text.charAt(i + j)) == toLower(key.charAt(j))) {
                j++;
            }
            if (j == m) {
                return true;
            }
        }
        return false;
    }

    static char toLower(char ch) {
        if (ch >= 'A' && ch <= 'Z') {
            return (char) (ch + 32);
        }
        return ch;
    }

    // -------------------------------------------------------------- delete
    static void deleteById() {
        System.out.print("Enter contact ID to delete: ");
        String id = readLine().trim();

        Node prev = null;
        Node cur = head;
        while (cur != null) {
            if (cur.data.id.equalsIgnoreCase(id)) {
                if (prev == null) {
                    head = cur.next;       // deleting the first node
                } else {
                    prev.next = cur.next;  // bypass the node
                }
                size--;
                System.out.println("Contact " + cur.data.id + " deleted. Contacts remaining: " + size);
                return;
            }
            prev = cur;
            cur = cur.next;
        }
        System.out.println("Contact ID not found.");
    }

    // ---------------------------------------------------------- validation
    // Exactly 10 characters and every character is a digit 0-9
    static boolean isValidPhone(String phone) {
        if (phone.length() != 10) {
            return false;
        }
        for (int i = 0; i < phone.length(); i++) {
            char ch = phone.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    static boolean phoneExists(String phone) {
        Node cur = head;
        while (cur != null) {
            if (cur.data.phone.equals(phone)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    // --------------------------------------------------------- file handling
    // Each line of contacts.txt:  ID|Name|Phone
    static void loadFromFile() {
        ArrayList<String> lines = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            return;   // file does not exist yet: start with an empty list
        }

        int maxId = 100;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|");
            if (parts.length != 3) {
                continue;   // skip damaged lines
            }
            Contact c = new Contact(parts[0].trim(), parts[1].trim(), parts[2].trim());
            appendNode(c);
            try {
                int number = Integer.parseInt(c.id.substring(1));
                if (number > maxId) {
                    maxId = number;
                }
            } catch (NumberFormatException e) {
                // ID not in C<number> form: ignore for numbering
            }
        }
        nextIdNumber = maxId + 1;
    }

    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            Node cur = head;
            while (cur != null) {
                pw.println(cur.data.id + "|" + cur.data.name + "|" + cur.data.phone);
                cur = cur.next;
            }
        } catch (IOException e) {
            System.out.println("Error: could not save contacts to " + FILE_NAME);
        }
    }

    // ------------------------------------------------------------- helpers
    static String readLine() {
        if (sc.hasNextLine()) {
            return sc.nextLine();
        }
        return "";
    }
}
