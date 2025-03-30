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
            System.out.println("You have reached the maximum borrow limit (3 books).\n");
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

    public void addBook(int id, String title) {
        if (findBook(id) != null) {
            System.out.println("Book ID already exists!\n");
            return;
        }
        books.add(new Book(id, title));
    }

    public void removeBook(int id) {
        Book book = findBook(id);
        if (book == null) {
            System.out.println("Book not found!\n");
            return;
        }
        for (Member member : members) {
            if (member.hasBorrowedBook(id)) {
                System.out.println("Cannot remove book. It is currently borrowed.\n");
                return;
            }
        }
        books.remove(book);
        System.out.println("Book removed successfully.\n");
    }

    public void registerMember(int id, String name) {
        if (findMember(id) != null) {
            System.out.println("Member ID already exists!\n");
            return;
        }
        members.add(new Member(id, name));
    }

    public Member findMember(int memberId) {
        return members.stream().filter(m -> m.getMemberId() == memberId).findFirst().orElse(null);
    }
    
    public Book findBook(int bookId) {
        return books.stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
    }

    public void borrowBook(int memberId, int bookId) {
        Member member = findMember(memberId);
        if (member == null) { System.out.println("Member not found!\n"); return; }

        Book book = findBook(bookId);
        if (book == null) { System.out.println("Book not found!\n"); return; }

        if (!book.isAvailable()) { System.out.println("Sorry, the book is already borrowed.\n"); return; }

        if (member.borrowBook(book)) {
            book.borrowBook();
            System.out.println(member.getName() + " borrowed: " + book.getTitle());
        }
    }

    public void returnBook(int memberId, int bookId) {
        Member member = findMember(memberId);
        if (member == null) { System.out.println("Member not found!\n"); return; }

        Book book = findBook(bookId);
        if (book == null) { System.out.println("Book not found!\n"); return; }

        if (member.hasBorrowedBook(bookId)) {
            member.returnBook(book);
            book.returnBook();
            System.out.println(member.getName() + " returned: " + book.getTitle());
        } else {
            System.out.println("Book was not borrowed by this member.\n");
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
        try (Scanner scanner = new Scanner(System.in)) {
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
                System.out.println("1. Display Books\n2. Display Members\n3. Borrow Book\n4. Return Book\n5. Add Book\n6. Remove Book\n7. Register Member\n8. Exit");
                System.out.print("Enter choice: ");
                
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number.\n");
                    scanner.next(); 
                    continue;
                }
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1 -> library.displayBooks();
                    case 2 -> library.displayMembers();
                    case 3 -> {
                        System.out.print("Enter Member ID: ");
                        int memberId = scanner.nextInt();
                        System.out.print("Enter Book ID: ");
                        int bookId = scanner.nextInt();
                        library.borrowBook(memberId, bookId);
                    }
                    case 4 -> {
                        System.out.print("Enter Member ID: ");
                        int returnMemberId = scanner.nextInt();
                        System.out.print("Enter Book ID: ");
                        int returnBookId = scanner.nextInt();
                        library.returnBook(returnMemberId, returnBookId);
                    }
                    case 5 -> {
                        System.out.print("Enter Book ID: ");
                        int newBookId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Book Title: ");
                        String newBookTitle = scanner.nextLine();
                        library.addBook(newBookId, newBookTitle);
                    }
                    case 8 -> { System.out.println("Exiting..."); return; }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }
}
