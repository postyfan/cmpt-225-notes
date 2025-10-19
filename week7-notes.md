# Week 7 — Balanced Trees, Heaps, Priority Queues

---

## 1) Key Ideas & Definitions
- **Balanced BST**: BST with height Θ(log n); guarantees O(log n) search/insert/delete.
- **AVL Tree**: BST maintaining `|height(left) − height(right)| ≤ 1` at every node; height < ~1.5·log₂ n.
- **2–3 Tree**: Multi-way search tree; nodes hold 1–2 keys with 2–3 children; all leaves at same depth.
- **B-Tree (order m)**: Nodes hold many keys (block-sized); root 2…m children; internal nodes m/2…m; leaves same depth.
- **Priority Queue (PQ)**: ADT with `add`, `getTop`, `removeTop`, size checks; orders removals by priority.
- **Heap (min/max)**: Complete binary tree with heap-order (min-heap: parent ≤ children); typical PQ implementation.
- **Heapify-Up/Down**: Local repairs after insert/delete to restore heap property.

**Intuition**: AVL enforces tight height bounds → predictable O(log n). 2–3/B-trees widen nodes to keep trees shallow (great for memory/disk). Heaps weaken ordering (only top guaranteed) to make `getTop` O(1) and updates O(log n).

---

## 2) Core Theory & Rules
- **AVL Height Bounds**
  - Minimal AVL size satisfies `S(h) ≥ S(h−1)+S(h−2)+1` ⇒ `S(h) ≥ Fib(h+1)` ⇒ `h = O(log n)`. *(clarified)*
  - Geometric bound gives `h < 1.5·log₂ n` (approx).
- **AVL Rebalancing Rule**
  - After BST insert/delete, update heights up to root; if a node `z` has balance ±2, rotate around the **deepest grandchild**; on ties, rotate in the **same direction** as the child (LL/LR/RR/RL).
- **2–3 Trees Depth Bounds**
  - With height `h`, `2^h ≤ N ≤ 3^{h+1}` ⇒ `log₃ N − 1 ≤ h ≤ log₂ N`. *(clarified)*
- **B-Tree Operations**
  - **Insert** at leaf; if node overflows, **split** at median and **push up** median recursively.
  - **Delete**: if key internal, swap with predecessor/successor in leaf; handle **underflow** via borrow-then-merge; fix parent recursively.
- **Heap Array Indexing (0-based)**: `left(i)=2i+1`, `right(i)=2i+2`, `parent(j)=(j−1)/2`.
- **Build-Heap in O(n)**: Bottom-up `heapifyDown` from last internal node to root.
- **Heapsort**: In-place max-heap; build O(n), then swap root with last and `heapifyDown` over shrinking window → O(n log n).

---

## 3) Algorithms & Patterns (with Java)

### 3.1 AVL Tree (insert/delete + rotations)
```java
// Minimal AVL (integers for brevity). Use generics T extends Comparable<T> in practice.
final class AVL {
  static final class Node {
    int key, height = 1;
    Node left, right;
    Node(int k) { key = k; }
  }

  private static int h(Node x) { return x == null ? 0 : x.height; }
  private static int bal(Node x) { return x == null ? 0 : h(x.left) - h(x.right); }
  private static void pull(Node x) { x.height = 1 + Math.max(h(x.left), h(x.right)); }

  // Rotations: WHY — restore |balance| ≤ 1 by moving tall side up and short side down.
  private static Node rotateRight(Node y) { // LL case
    Node x = y.left, T2 = x.right;
    x.right = y; y.left = T2;
    pull(y); pull(x);
    return x;
  }
  private static Node rotateLeft(Node x) { // RR case
    Node y = x.right, T2 = y.left;
    y.left = x; x.right = T2;
    pull(x); pull(y);
    return y;
  }

  // Rebalance using deepest-grandchild rule (covers LL/LR/RR/RL).
  private static Node rebalance(Node z) {
    int b = bal(z);
    if (b > 1) { // left heavy
      if (bal(z.left) < 0) z.left = rotateLeft(z.left);   // LR
      return rotateRight(z);                               // LL
    }
    if (b < -1) { // right heavy
      if (bal(z.right) > 0) z.right = rotateRight(z.right); // RL
      return rotateLeft(z);                                  // RR
    }
    return z;
  }

  public static Node insert(Node root, int key) {
    if (root == null) return new Node(key);
    if (key < root.key) root.left = insert(root.left, key);
    else if (key > root.key) root.right = insert(root.right, key);
    else return root; // no duplicates (policy)
    pull(root);
    return rebalance(root); // O(1) rotations on insertion path, O(log n) height updates
  }

  public static Node delete(Node root, int key) {
    if (root == null) return null;
    if (key < root.key) root.left = delete(root.left, key);
    else if (key > root.key) root.right = delete(root.right, key);
    else {
      if (root.left == null || root.right == null) {
        root = (root.left != null) ? root.left : root.right;
      } else {
        // Replace with predecessor (max in left)
        Node t = root.left;
        while (t.right != null) t = t.right;
        root.key = t.key;
        root.left = delete(root.left, t.key);
      }
    }
    if (root == null) return null;
    pull(root);
    return rebalance(root); // May cascade on deletion
  }
}
