import java.util.*;

class Book {
    private int id;
    private String title;
    private boolean isAvailable;

    public Book(int id, String title) {
        this.id = id;
        this.title = title;
        this.isAvailable = true;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return isAvailable; }
    public void borrowBook() { isAvailable = false; }
    public void returnBook() { isAvailable = true; }
    
    @Override
    public String toString() {
        return "Book ID: " + id + ", Title: " + title + ", Available: " + (isAvailable ? "Yes" : "No");
    }
}

class Member {
    private int memberId;
    private String name;
    private List<Book> borrowedBooks = new ArrayList<>();

    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<Book> getBorrowedBooks() { return borrowedBooks; }

    public boolean borrowBook(Book book) {
        if (borrowedBooks.size() >= 3) {
            System.out.println("You have reached the maximum borrow limit (3 books).");
            return false;
        }
        borrowedBooks.add(book);
        return true;
    }

    public void returnBook(Book book) { borrowedBooks.remove(book); }
    public boolean hasBorrowedBook(int bookId) {
        return borrowedBooks.stream().anyMatch(book -> book.getId() == bookId);
    }
}

class Library {
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();

    public void addBook(int id, String title) { books.add(new Book(id, title)); }
    public void removeBook(int id) { books.removeIf(book -> book.getId() == id); }
    public void registerMember(int id, String name) { members.add(new Member(id, name)); }
    public Member findMember(int memberId) {
        return members.stream().filter(m -> m.getMemberId() == memberId).findFirst().orElse(null);
    }
    public Book findBook(int bookId) {
        return books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
    }

    public void borrowBook(int memberId, int bookId) {
        Member member = findMember(memberId);
        if (member == null) { System.out.println("Member not found!"); return; }

        Book book = findBook(bookId);
        if (book == null) { System.out.println("Book not found!"); return; }

        if (!book.isAvailable()) { System.out.println("Sorry, the book is already borrowed."); return; }

        if (member.borrowBook(book)) {
            book.borrowBook();
            System.out.println(member.getName() + " borrowed: " + book.getTitle());
        }
    }

    public void returnBook(int memberId, int bookId) {
        Member member = findMember(memberId);
        if (member == null) { System.out.println("Member not found!"); return; }

        Book book = findBook(bookId);
        if (book == null) { System.out.println("Book not found!"); return; }

        if (member.hasBorrowedBook(bookId)) {
            member.returnBook(book);
            book.returnBook();
            System.out.println(member.getName() + " returned: " + book.getTitle());
        } else {
            System.out.println("Book was not borrowed by this member.");
        }
    }

    public void displayBooks() {
        System.out.println("\nLibrary Books:");
        books.forEach(System.out::println);
    }
    
    public void displayMembers() {
        System.out.println("\nLibrary Members:");
        members.forEach(m -> System.out.println("Member ID: " + m.getMemberId() + ", Name: " + m.getName()));
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        library.addBook(1, "Java Programming");
        library.addBook(2, "Full Stack Development");
        library.addBook(3, "Operating Systems");
        library.addBook(4, "Data Structures and Algorithms");
        library.addBook(5, "Database Management Systems");

        library.registerMember(1, "Abhimanyu");
        library.registerMember(2, "Sahil");
        library.registerMember(3, "Ashish");
        library.registerMember(4, "Rohit");
        library.registerMember(5, "Gaurav");

        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Display Books");
            System.out.println("2. Display Members");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Add Book (Admin)");
            System.out.println("6. Remove Book (Admin)");
            System.out.println("7. Register Member");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    library.displayBooks();
                    break;
                case 2:
                    library.displayMembers();
                    break;
                case 3:
                    System.out.print("Enter Member ID: ");
                    int memberId = scanner.nextInt();
                    System.out.print("Enter Book ID to borrow: ");
                    int bookId = scanner.nextInt();
                    library.borrowBook(memberId, bookId);
                    break;
                case 4:
                    System.out.print("Enter Member ID: ");
                    int returnMemberId = scanner.nextInt();
                    System.out.print("Enter Book ID to return: ");
                    int returnBookId = scanner.nextInt();
                    library.returnBook(returnMemberId, returnBookId);
                    break;
                case 5:
                    System.out.print("Enter Book ID: ");
                    int newBookId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Book Title: ");
                    String newBookTitle = scanner.nextLine();
                    library.addBook(newBookId, newBookTitle);
                    System.out.println("Book added successfully.");
                    break;
                case 6:
                    System.out.print("Enter Book ID to remove: ");
                    int removeBookId = scanner.nextInt();
                    library.removeBook(removeBookId);
                    System.out.println("Book removed successfully.");
                    break;
                case 7:
                    System.out.print("Enter Member ID: ");
                    int newMemberId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Member Name: ");
                    String newMemberName = scanner.nextLine();
                    library.registerMember(newMemberId, newMemberName);
                    System.out.println("Member registered successfully.");
                    break;
                case 8:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
