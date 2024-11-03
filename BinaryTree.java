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
            e.printStackTrace();
            root = null;
        }
    }

    public String getName() {
        return root.info.getName();
    }

    private NodeA preorderLoad(BufferedReader bur) {
        try {
            String line = bur.readLine();
            if (line == null) {
                return null;
            }
            Person person = new Person(line);
            NodeA node = new NodeA(person);
            node.left = preorderLoad(bur);
            node.right = preorderLoad(bur);
            return node;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addNode(Person unaPersona, String level) {
        if (root == null) {
            root = new NodeA(unaPersona);
            return true;
        }
        return root.addNodeRecursive(unaPersona, level);
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
        String filename = root.info.getName() + ".txt";
        try (BufferedWriter buw = new BufferedWriter(new FileWriter(filename))) {
            root.preorderSaveRecursive(buw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removePerson(String name) {
        if (root != null && !root.info.getName().equals(name)) {
            if (root.info.getName().compareTo(name) > 0) {
                root.left.removePersonRecursive(name);
            } else {
                root.right.removePersonRecursive(name);
            }
        }
    }

    public boolean isFrom(String place) {
        return root != null && root.isDescentFromRecursive(place);
    }

    public boolean isDescentFrom(String name) {
        return root != null && root.isDescentFromRecursive(name);
    }

    public int howManyParents() {
        return root != null ? root.countNodesRecursive() - 1 : 0;
    }

    public int howManyGrandparents() {
        return root != null ? root.countNodesRecursive() - 2 : 0;
    }

    public boolean marriedParents() {
        return root != null &&
                root.left != null &&
                root.right != null &&
                root.left.info.getMaritalStatus() == 2 &&
                root.right.info.getMaritalStatus() == 2;
    }

    private class NodeA {
        NodeA right;
        NodeA left;
        Person info;

        NodeA(Person info) {
            this.info = info;
        }

        NodeA(Person info, NodeA left, NodeA right) {
            this.info = info;
            this.left = left;
            this.right = right;
        }

        private void preorderSaveRecursive(BufferedWriter buw) {
            try {
                buw.write(info.toString());
                buw.newLine();
                if (left != null) {
                    left.preorderSaveRecursive(buw);
                }
                if (right != null) {
                    right.preorderSaveRecursive(buw);
                }
            } catch (IOException e) {
                e.printStackTrace();
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
            if (right != null) {
                right.displayTreeRecursive(level + 1);
            }
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }
            System.out.println(info);
            if (left != null) {
                left.displayTreeRecursive(level + 1);
            }
        }

        private void removePersonRecursive(String name) {
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
            } else if (left != null && left.info.getName().compareTo(name) > 0) {
                left.removePersonRecursive(name);
            } else if (right != null && right.info.getName().compareTo(name) < 0) {
                right.removePersonRecursive(name);
            }
        }

        private boolean isDescentFromRecursive(String place) {
            if (info.getPlaceOfOrigin().equals(place)) {
                return true;
            }
            if (left != null && left.isDescentFromRecursive(place)) {
                return true;
            }
            return right != null && right.isDescentFromRecursive(place);
        }

        private int countNodesRecursive() {
            int count = 1;
            if (left != null) {
                count += left.countNodesRecursive();
            }
            if (right != null) {
                count += right.countNodesRecursive();
            }
            return count;
        }
    }
}
