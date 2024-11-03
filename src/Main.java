package src;

import java.io.File;
import java.util.Scanner;

public class Main {
    private static final Students students = new Students();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        readAllStudents();

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

        System.out.println("Sortint del programa.");
    }

    private static void readAllStudents() {
        File folder = new File("estudiants");
        File[] listOfFiles = folder.listFiles((_, name) -> name.endsWith(".txt"));

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                BinaryTree tree = new BinaryTree(file.getPath());
                if (tree.getName() != null) {
                    students.addStudent(tree);
                    System.out.println("Alumne carregat des del fitxer: " + file.getName());
                    System.out.println("Arbre binari estructurat:");
                    tree.displayTree();
                }
            }
        }
    }

    private static void saveAllStudents() {
        for (String name : students.getAllStudentsName()) {
            BinaryTree studentTree = students.getStudent(name);
            studentTree.preorderSave();
            System.out.println("Alumne guardat al fitxer: " + name);
        }
    }

    private static void displayAllStudentsNames() {
        if (students.getAllStudentsName().isEmpty()) {
            System.out.println("No hi ha estudiants.");
        } else {
            System.out.println("Mostrem els noms dels estudiants:");
            for (String name : students.getAllStudentsName()) {
                System.out.println("\t" + name);
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
        System.out.print("Introdueix l'estat civil (0: vidu, 1: divorciat, 2: casat, 3: solter): ");
        int maritalStatus = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (maritalStatus < 0 || maritalStatus > 3) {
            System.out.println("Estat civil no vàlid.");
            return;
        }

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
                System.out.print("Introdueix l'estat civil (0: vidu, 1: divorciat, 2: casat, 3: solter): ");
                int maritalStatus = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (maritalStatus < 0 || maritalStatus > 3) {
                    System.out.println("Estat civil no vàlid.");
                    return;
                }

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
        System.out.print("Indica la ciutat de naixement a buscar: ");
        String birthCity = scanner.nextLine();
        System.out.print("Indica la ciutat de procedència a buscar: ");
        String familyCity = scanner.nextLine();

        int countBirthCity = 0;
        int countFamilyCity = 0;
        int countSingleParent = 0;
        int countDivorcedParents = 0;
        int countGrandParents = 0;

        for (String name : students.getAllStudentsName()) {
            BinaryTree studentTree = students.getStudent(name);
            if (studentTree.isFrom(birthCity)) {
                countBirthCity++;
            }
            if (studentTree.isDescentFrom(familyCity)) {
                countFamilyCity++;
            }
            if (studentTree.howManyParents() == 1) {
                countSingleParent++;
            }
            if (!studentTree.marriedParents()) {
                countDivorcedParents++;
            }
            if (studentTree.howManyGrandParents() >= 2) {
                countGrandParents++;
            }
        }

        System.out.println("Nombre d'alumnes totals: " + students.getAllStudentsName().size());
        System.out.println("Hi ha " + countBirthCity + " alumnes de " + birthCity);
        System.out.println("Hi ha " + countFamilyCity + " alumnes descendents de " + familyCity);
        System.out.println("Hi ha " + countSingleParent + " alumnes amb un únic progenitor.");
        System.out.println("Hi ha " + countDivorcedParents + " alumnes amb progenitors no casats.");
        System.out.println("Hi ha " + countGrandParents + " alumnes amb dos o més avis o àvies.");
    }
}