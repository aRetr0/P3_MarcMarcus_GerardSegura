import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Students students = new Students();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int option;
        do {
            System.out.println("Menú Principal:");
            System.out.println("\t1. Mostrar llistat d'estudiants");
            System.out.println("\t2. Mostrar família d'un estudiant");
            System.out.println("\t3. Afegir un estudiant");
            System.out.println("\t4. Modificar un estudiant");
            System.out.println("\t5. Mostrar el informe");
            System.out.println("\t6. Guardar i Sortir");
            System.out.print("Tria una opció: ");
            option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    displayAllStudentsNames();
                    break;
                case 2:
                    showStudentFamily();
                    break;
                case 3:
                    addNewStudent();
                    break;
                case 4:
                    modifyStudent();
                    break;
                case 5:
                    mostrarInforme();
                    break;
                case 6:
                    saveAllStudents();
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        } while (option != 6);
    }

    private static void readAllStudents(String folderPath) {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String name = br.readLine();
                    String placeOfOrigin = br.readLine();
                    int maritalStatus = Integer.parseInt(br.readLine());
                    Person person = new Person(name, placeOfOrigin, maritalStatus);
                    BinaryTree tree = new BinaryTree();
                    tree.addNode(person, "");
                    students.addStudent(tree);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void saveAllStudents() {
        for (String name : students.getAllStudentsName()) {
            BinaryTree studentTree = students.getStudent(name);
            studentTree.preorderSave();
        }
    }

    private static void displayAllStudentsNames() {
        if (students.getAllStudentsName().isEmpty()) {
            System.out.println("No hi ha estudiants.");
        } else {
            for (String name : students.getAllStudentsName()) {
                System.out.println(name);
            }
        }
    }

    private static void showStudentFamily() {
        System.out.print("Introdueix el nom de l'estudiant: ");
        String name = scanner.nextLine();
        BinaryTree studentTree = students.getStudent(name);
        if (studentTree != null) {
            studentTree.displayTree();
        } else {
            System.out.println("Estudiant no trobat.");
        }
    }

    private static void addNewStudent() {
        System.out.print("Introdueix el nom de l'estudiant: ");
        String name = scanner.nextLine();
        System.out.print("Introdueix el lloc d'origen: ");
        String placeOfOrigin = scanner.nextLine();
        System.out.print("Introdueix l'estat civil (0: solter, 1: casat, 2: divorciat): ");
        int maritalStatus = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Person person = new Person(name, placeOfOrigin, maritalStatus);
        BinaryTree tree = new BinaryTree();
        tree.addNode(person, "");
        students.addStudent(tree);
    }

    private static void modifyStudent() {
        System.out.print("Introdueix el nom de l'estudiant: ");
        String name = scanner.nextLine();
        BinaryTree studentTree = students.getStudent(name);
        if (studentTree != null) {
            System.out.print("Vols afegir o eliminar membres de la família? (afegir/eliminar): ");
            String action = scanner.nextLine();
            if (action.equalsIgnoreCase("afegir")) {
                System.out.print("Introdueix el nom del membre: ");
                String memberName = scanner.nextLine();
                System.out.print("Introdueix el lloc d'origen: ");
                String placeOfOrigin = scanner.nextLine();
                System.out.print("Introdueix l'estat civil (0: solter, 1: casat, 2: divorciat): ");
                int maritalStatus = scanner.nextInt();
                scanner.nextLine(); // consume newline
                System.out.print("Introdueix el nivell (L/R): ");
                String level = scanner.nextLine();

                Person person = new Person(memberName, placeOfOrigin, maritalStatus);
                studentTree.addNode(person, level);
            } else if (action.equalsIgnoreCase("eliminar")) {
                System.out.print("Introdueix el nom del membre a eliminar: ");
                String memberName = scanner.nextLine();
                studentTree.removePerson(memberName);
            } else {
                System.out.println("Acció no vàlida.");
            }
        } else {
            System.out.println("Estudiant no trobat.");
        }
    }

    private static void mostrarInforme() {
        System.out.print("Introdueix la ciutat on ha nascut l'estudiant: ");
        String birthCity = scanner.nextLine();
        System.out.print("Introdueix la ciutat de procedència de la família: ");
        String familyCity = scanner.nextLine();

        int countBirthCity = 0;
        int countFamilyCity = 0;

        for (String name : students.getAllStudentsName()) {
            BinaryTree studentTree = students.getStudent(name);
            if (studentTree.isFrom(birthCity)) {
                countBirthCity++;
            }
            if (studentTree.isDescentFrom(familyCity)) {
                countFamilyCity++;
            }
        }

        System.out.println("Informe:");
        System.out.println("Estudiants nascuts a " + birthCity + ": " + countBirthCity);
        System.out.println("Estudiants amb família de " + familyCity + ": " + countFamilyCity);
    }
}