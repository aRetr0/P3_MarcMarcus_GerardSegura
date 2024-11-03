package src;

import java.io.*;

public class BinaryTree {
    private NodeA root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(String filename) {
        try (BufferedReader bur = new BufferedReader(new FileReader(filename))) {
            root = preorderLoad(bur);
        } catch (IOException e) {
            System.err.println("Error loading binary tree from file: " + e.getMessage());
            root = null;
        }
    }

    public String getName() {
        return root != null ? root.info.getName() : null;
    }

    private NodeA preorderLoad(BufferedReader bur) {
        try {
            String line = bur.readLine();
            if (line == null) {
                return null;
            }
            if (line.trim().isEmpty()) {
                NodeA deadNode = new NodeA(null);
                deadNode.left = preorderLoad(bur);
                deadNode.right = preorderLoad(bur);
                return deadNode;
            }

            Person person = new Person(line);
            NodeA node = new NodeA(person);

            if (line.contains("; ;")) {
                node.left = null;
                node.right = null;
            } else if (line.contains(";")) {
                node.left = preorderLoad(bur);
                node.right = null;
            } else {
                node.left = preorderLoad(bur);
                node.right = preorderLoad(bur);
            }

            return node;
        } catch (IOException e) {
            System.err.println("Error reading line: " + e.getMessage());
            return null;
        }
    }

    public void addNode(Person unaPersona, String level) {
        if (root == null) {
            root = new NodeA(unaPersona);
            return;
        }
        root.addNodeRecursive(unaPersona, level);
    }

    public void displayTree() {
        if (root != null) {
            root.displayTreeRecursive(0);
        }
    }

    public void preorderSave() {
        if (root == null) {
            throw new IllegalStateException("The tree is empty.");
        }
        String filename = "estudiants/" + root.info.getName() + ".txt";
        try (BufferedWriter buw = new BufferedWriter(new FileWriter(filename))) {
            root.preorderSaveRecursive(buw);
        } catch (IOException e) {
            System.err.println("Error saving binary tree to file: " + e.getMessage());
        }
    }

    public void removePerson(String name) {
        if (root != null && !root.info.getName().equals(name)) {
            root.removePersonRecursive(name);
        }
    }

    public boolean isFrom(String place) {
        return root != null && root.info.getPlaceOfOrigin().equals(place);
    }

    public boolean isDescentFrom(String place) {
        return root != null && root.isDescentFromRecursive(place);
    }

    public int howManyParents() {
        int count = 0;
        if (root != null) {
            if (root.left != null && root.left.info != null) count++;
            if (root.right != null && root.right.info != null) count++;
        }
        return count;
    }

    public int howManyGrandParents() {
        int count = 0;
        if (root != null) {
            if (root.left != null) {
                if (root.left.left != null) count++;
                if (root.left.right != null) count++;
            }
            if (root.right != null) {
                if (root.right.left != null) count++;
                if (root.right.right != null) count++;
            }
        }
        return count;
    }

    public boolean marriedParents() {
        return root != null && root.left != null && root.right != null && root.left.info.getMaritalStatus() == Person.MARRIED && root.right.info.getMaritalStatus() == Person.MARRIED;
    }

    private static class NodeA {
        NodeA right;
        NodeA left;
        Person info;

        NodeA(Person info) {
            this.info = info;
        }

        private void preorderSaveRecursive(BufferedWriter buw) throws IOException {
            if (info != null) {
                buw.write(info.toString());
                if (left == null && right == null) {
                    buw.write("; ;");
                } else if (right == null) {
                    buw.write(";");
                }
                buw.newLine();
            } else {
                buw.newLine();
            }

            if (left != null) {
                left.preorderSaveRecursive(buw);
            }

            if (right != null) {
                right.preorderSaveRecursive(buw);
            }
        }

        private boolean addNodeRecursive(Person unaPersona, String level) {
            if (level.isEmpty()) {
                return false;
            }
            char direction = level.charAt(0);
            String remainingLevel = level.substring(1);

            if (direction == 'L') {
                if (left == null) {
                    left = new NodeA(unaPersona);
                    return true;
                } else {
                    return left.addNodeRecursive(unaPersona, remainingLevel);
                }
            } else if (direction == 'R') {
                if (right == null) {
                    right = new NodeA(unaPersona);
                    return true;
                } else {
                    return right.addNodeRecursive(unaPersona, remainingLevel);
                }
            } else {
                throw new IllegalArgumentException("Invalid direction: " + direction);
            }
        }

        private void displayTreeRecursive(int level) {
            printIndent(level);
            if (info != null) System.out.println(info.getName());
            else System.out.println("*dead");
            if (left != null) {
                left.displayTreeRecursive(level + 1);
            }
            if (right != null) {
                right.displayTreeRecursive(level + 1);
            }
        }

        private void printIndent(int level) {
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }
        }

        private boolean removePersonRecursive(String name) {
            if (left != null && left.info.getName().equals(name)) {
                if (left.left == null && left.right == null) {
                    left = null;
                } else if (left.left == null) {
                    left = left.right;
                } else if (left.right == null) {
                    left = left.left;
                } else {
                    NodeA temp = left.right;
                    while (temp.left != null) {
                        temp = temp.left;
                    }
                    left.info = temp.info;
                    left.right.removePersonRecursive(temp.info.getName());
                }
                return true;
            } else if (right != null && right.info.getName().equals(name)) {
                if (right.left == null && right.right == null) {
                    right = null;
                } else if (right.left == null) {
                    right = right.right;
                } else if (right.right == null) {
                    right = right.left;
                } else {
                    NodeA temp = right.right;
                    while (temp.left != null) {
                        temp = temp.left;
                    }
                    right.info = temp.info;
                    right.right.removePersonRecursive(temp.info.getName());
                }
                return true;
            } else if (left != null && left.info.getName().compareTo(name) > 0) {
                return left.removePersonRecursive(name);
            } else if (right != null && right.info.getName().compareTo(name) < 0) {
                return right.removePersonRecursive(name);
            }
            return false;
        }

        private boolean isDescentFromRecursive(String place) {
            if (info != null && info.getPlaceOfOrigin().equals(place)) {
                return true;
            }
            if (left != null && left.isDescentFromRecursive(place)) {
                return true;
            }
            return right != null && right.isDescentFromRecursive(place);
        }
    }
}