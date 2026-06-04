class AVLNode {
    int score;
    int height;
    int size;
    AVLNode left, right;

    AVLNode(int score) {
        this.score = score;
        height = 1;
        size = 1;
    }
}

class AVLTree {

    int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    int size(AVLNode node) {
        return (node == null) ? 0 : node.size;
    }

    void update(AVLNode node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
            node.size = 1 + size(node.left) + size(node.right);
        }
    }

    int getBalance(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode t2 = x.right;

        x.right = y;
        y.left = t2;

        update(y);
        update(x);

        return x;
    }

    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode t2 = y.left;

        y.left = x;
        x.right = t2;

        update(x);
        update(y);

        return y;
    }

    AVLNode insert(AVLNode node, int score) {

        if (node == null)
            return new AVLNode(score);

        if (score < node.score)
            node.left = insert(node.left, score);
        else if (score > node.score)
            node.right = insert(node.right, score);
        else
            return node;

        update(node);

        int balance = getBalance(node);

        // LL
        if (balance > 1 && score < node.left.score)
            return rightRotate(node);

        // RR
        if (balance < -1 && score > node.right.score)
            return leftRotate(node);

        // LR
        if (balance > 1 && score > node.left.score) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && score < node.right.score) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;

        while (current.left != null)
            current = current.left;

        return current;
    }

    AVLNode delete(AVLNode root, int score) {

        if (root == null)
            return root;

        if (score < root.score)
            root.left = delete(root.left, score);
        else if (score > root.score)
            root.right = delete(root.right, score);
        else {

            if ((root.left == null) || (root.right == null)) {

                AVLNode temp;

                if (root.left != null)
                    temp = root.left;
                else
                    temp = root.right;

                if (temp == null) {
                    root = null;
                } else {
                    root = temp;
                }

            } else {

                AVLNode temp = minValueNode(root.right);

                root.score = temp.score;

                root.right = delete(root.right, temp.score);
            }
        }

        if (root == null)
            return root;

        update(root);

        int balance = getBalance(root);

        // LL
        if (balance > 1 && getBalance(root.left) >= 0)
            return rightRotate(root);

        // LR
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // RR
        if (balance < -1 && getBalance(root.right) <= 0)
            return leftRotate(root);

        // RL
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    int getRank(AVLNode root, int score) {

        if (root == null)
            return 0;

        if (score < root.score)
            return getRank(root.left, score);

        if (score > root.score)
            return size(root.left) + 1 + getRank(root.right, score);

        return size(root.left) + 1;
    }

    void leaderboard(AVLNode root) {
        if (root != null) {
            leaderboard(root.right);
            System.out.print(root.score + " ");
            leaderboard(root.left);
        }
    }
}

public class GamingLeaderboardAVL {

    public static void main(String[] args) {

        AVLTree tree = new AVLTree();
        AVLNode root = null;

        int[] scores = {
                8200, 5400, 9100, 7700, 8800,
                4600, 9900, 6000, 7300, 9500, 5100
        };

        System.out.println("Inserting Player Scores:");

        for (int score : scores) {
            root = tree.insert(root, score);
        }

        System.out.println("\nLeaderboard (Highest to Lowest):");
        tree.leaderboard(root);

        int playerScore = 8800;

        int rank = tree.getRank(root, playerScore);

        int totalPlayers = root.size;

        System.out.println("\n\nPlayer Score: " + playerScore);
        System.out.println("Leaderboard Rank: " + (totalPlayers - rank + 1));

        System.out.println("\nUpdating Score...");
        System.out.println("Player score changed from 8800 to 9200");

        root = tree.delete(root, 8800);
        root = tree.insert(root, 9200);

        System.out.println("\nUpdated Leaderboard:");
        tree.leaderboard(root);

        rank = tree.getRank(root, 9200);

        System.out.println("\n\nNew Rank: " + (root.size - rank + 1));

        System.out.println("\n\nTime Complexity:");
        System.out.println("Insert  : O(log n)");
        System.out.println("Delete  : O(log n)");
        System.out.println("Rank    : O(log n)");
        System.out.println("Rotations : O(log n)");
    }
}