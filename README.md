# Contact List Manager

A console-based Java application that manages a contact list using a **singly linked list**.
DSA project (PS No. 30) by **Khushi Kumari** (ERP ID: RU-25-10677), B.Tech CSE (AIML).

## Features

- **Add contact**: name + phone; IDs are generated automatically (C101, C102, ...)
- **Display all** contacts as a numbered list
- **Search by name**: linear search, partial text, letter case ignored
- **Delete contact by ID**
- **Validation**: name cannot be empty, phone must have exactly 10 digits, no duplicate phone numbers
- **File handling**: contacts are loaded from `contacts.txt` at start and saved on exit

## How it works

| Class | Role |
|---|---|
| `Contact` | Holds the ID, name and phone of one contact |
| `Node` | One node of the linked list: `Contact data` + `Node next` |
| `ContactListManager` | Holds `head`, runs the menu and implements all operations as methods |

The linked list, the substring search and the validation are written by hand (no built-in sort or search methods).
`ArrayList` is used only to collect search results and to read the lines of the file.

## Run

Requires JDK 8 or later (JDK 17 recommended).

```bash
javac ContactListManager.java
java ContactListManager
```

Menu: `1` Add Contact, `2` Display All, `3` Search by Name, `4` Delete Contact by ID, `0` Exit (saves).

## Sample session

```
Enter choice: 1
Name: Karan Mehta
Phone: 9123456780
Contact added. ID: C102 | Karan Mehta | 9123456780

Enter choice: 3
Enter name to search: Mehta
Found: C102 | Karan Mehta | 9123456780

Enter choice: 2
1. C101 | Anita Roy | 9876543210
2. C102 | Karan Mehta | 9123456780
```

## Data file

`contacts.txt` has one line per contact in the format `ID|Name|Phone`:

```
C101|Anita Roy|9876543210
C102|Karan Mehta|9123456780
```

## Time complexity (n = number of contacts)

| Operation | Time |
|---|---|
| Add contact | O(n) |
| Display all | O(n) |
| Search by name | O(n x L x m), about O(n) for short names |
| Delete by ID | O(n) |
| Load / Save file | O(n^2) / O(n) |

## Test cases

All four official test cases pass: normal add (C101), delete the only contact (remaining: 0),
invalid phone (`12345` is rejected), and add + search + display (C102 Karan Mehta).

## Files

- `ContactListManager.java`: source code
- `Contact_List_Manager_Interim_Report.docx`: project report
- `Contact_List_Manager_Presentation.pptx`: presentation
