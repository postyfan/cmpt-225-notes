# Week 6 - Balanced Trees, Heaps, Priority Queues

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
```
**Worked Insert Example (AVL)** (keys: 8,10,9): Insert 8,10 (OK). Insert 9 → root right-heavy with left-heavy child ⇒ RL: rotateRight(child), then rotateLeft(root). *(clarified)*

---

### 3.2 2–3 Tree (high-level)
```java
// Sketch — implementations vary. WHY: widen nodes to keep height low and leaves aligned.
final class TwoThreeTree<K extends Comparable<K>> {
  static final class Node<K> {
    K a, b;               // one or two keys (b null if 2-node)
    Node<K> c1, c2, c3;   // 2 or 3 children; leaves have all null
    boolean isLeaf() { return c1 == null; }
  }
  // Insert: descend to leaf; place key; if {3 keys} -> split into two nodes; push median up recursively.
  // Delete: if internal, swap with predecessor in leaf; remove from leaf; resolve underflow by borrow/merge upward.
  // Invariants: in-order keys; all leaves equal depth.
}
```
### 3.3 B-Tree (order m) (high-level)
```java
// WHY — block-friendly: many keys per node, fewer levels, fewer I/Os.
final class BTree<K extends Comparable<K>> {
  static final class Node<K> {
    java.util.ArrayList<K> keys = new java.util.ArrayList<>();
    java.util.ArrayList<Node<K>> child = new java.util.ArrayList<>();
    boolean leaf;
  }
  // insert(k):
  //  if root is full -> split root: newRoot with median, two children; then insertNonFull.
  //  insertNonFull(x,k): if leaf, insert sorted; else find child i; if child[i] full -> split then proceed.
  // delete(k):
  //  Ensure child has >= t keys before descending; use borrow (from siblings) else merge; if internal, replace by pred/succ.
}
```
### 3.4 Min-Heap Priority Queue (array-backed)
```java
import java.util.ArrayList;

// WHY — complete tree + local swaps give O(log n) updates and O(1) min.
final class MinHeap {
  private final ArrayList<Integer> a = new ArrayList<>();

  private int parent(int i){ return (i-1)/2; }
  private int left(int i){ return 2*i+1; }
  private int right(int i){ return 2*i+2; }

  public int size(){ return a.size(); }
  public boolean isEmpty(){ return a.isEmpty(); }
  public Integer getMin(){ return a.isEmpty() ? null : a.get(0); } // O(1)

  public void add(int x){ // heapifyUp
    a.add(x);
    int i = a.size()-1;
    while (i > 0 && a.get(i) < a.get(parent(i))) {
      swap(i, parent(i)); i = parent(i);
    }
  }

  public Integer removeMin(){ // heapifyDown
    if (a.isEmpty()) return null;
    int min = a.get(0);
    int last = a.remove(a.size()-1);
    if (!a.isEmpty()) {
      a.set(0, last);
      heapifyDown(0);
    }
    return min;
  }

  private void heapifyDown(int i){
    while (true) {
      int l = left(i), r = right(i), s = i;
      if (l < a.size() && a.get(l) < a.get(s)) s = l;
      if (r < a.size() && a.get(r) < a.get(s)) s = r;
      if (s == i) break;
      swap(i, s); i = s;
    }
  }

  private void swap(int i, int j){
    int t = a.get(i); a.set(i, a.get(j)); a.set(j, t);
  }

  // Build-heap O(n): bottom-up heapifyDown
  public static MinHeap build(int[] arr){
    MinHeap h = new MinHeap();
    for (int x : arr) h.a.add(x);
    for (int i = (h.size()/2)-1; i >= 0; --i) h.heapifyDown(i);
    return h;
  }
}
```
**Heapsort (in-place, max-heap)**: Build max-heap array; repeat `swap(a[0], a[n-1-i]); heapifyDown(0)` over shrinking heap window.

---

## 4) Complexity & Trade-offs

### 4.1 Operations
| Structure / Op | Find | Insert | Delete | Extra Space | Notes |
|---|---:|---:|---:|---:|---|
| AVL Tree | O(log n) | O(log n) (≤2 rotations) | O(log n) (may cascade) | O(1) per node (height) | Strict balance; more code. |
| 2–3 Tree | O(log n) | O(log n) | O(log n) | Node keys | All leaves same depth. |
| B-Tree (order m) | O(logₘ n) I/Os | O(logₘ n) | O(logₘ n) | Block-sized nodes | Fewer levels; disk-friendly. |
| Min-Heap `getMin` | O(1) | — | — | — | Root stores min. |
| Min-Heap insert/remove | — | O(log n) | O(log n) | O(n) array | Complete tree shape. |
| Build-Heap | — | O(n) | — | O(1) extra | Bottom-up heapify. |
| Heapsort (max-heap) | — | — | — | O(1) extra | O(n log n), not stable. |

### 4.2 Trade-offs
- **AVL vs plain BST**: AVL guarantees O(log n); BST can degrade to O(n). AVL has higher constant factors and code complexity.
- **AVL vs B-Tree**: AVL best in-memory; B-tree best on disk/SSD due to block alignment and fewer node touches.
- **Heap vs Sorted Structure**: Heap gives fast top but no global order; not for fast arbitrary search.

---

## 5) Diagrams / Visuals (ASCII)

**AVL Balance (LL)**
     z                         y
    / \      rotateRight      / \
   y   T3   -------------->   x  z
  / \                        / \ / \
 x  T2                      T0 T1T2 T3

**AVL (LR → L then R)**
     z                          z                          y
    / \      (LR)             / \      rotateLeft(y)     / \
   y  T3   --------------->  y  T3   --------------->   x   z
  / \                       / \                        / \ / \
 T0  x                     T0 T1                      T0 T1T2 T3
     / \                       / \
    T1 T2                     x  T2

**Heap (array)**
Array: [1,4,2,9,6,3,10,8,10,7]
Tree (level order = array):
            1
       4         2
     9   6     3  10
   8 10 7
Indices: i -> left=2i+1, right=2i+2, parent=(i-1)/2

**2–3 Node**
[ A | B ]     children:  T1  T2  T3
Order:  T1 < A < T2 < B < T3

**B-Tree Node (order m=4)**
keys: [A | B | C] ; children: T1 T2 T3 T4
T1 < A < T2 < B < T3 < C < T4

---

## 6) Edge Cases & Pitfalls
- **AVL**
  - Base cases: `null → height=0`.
  - Mis-detecting LR/RL; always check child balance.
  - Deletion can cascade balances; keep rebalancing up to root.
  - Always `pull` (recompute height) before `rebalance`.
- **2–3/B-Tree**
  - After insert, propagate **split** upward if needed.
  - On delete underflow: try **borrow** before **merge**.
  - Maintain strict key ordering and child range invariants.
- **Heap**
  - Wrong index math; verify `(n/2)-1` as last internal node.
  - `heapifyDown`: swap with the **smaller** child in a min-heap.
  - Heapsort: remember to shrink heap boundary each iteration.

---

## 7) Testing & Debugging Hints
- **AVL invariants**: for each node `|h(L)-h(R)| ≤ 1` and `h = 1+max(hL,hR)`; assert after updates.
- **Rotation sanity**: in-order traversal must remain unchanged.
- **2–3/B-Tree**: all leaves same depth; keys strictly increasing inside nodes; separators match child ranges.
- **Heap checks**: for all `i>0`, `a[parent(i)] ≤ a[i]`; test duplicates and worst-case arrays.
- **Build-heap**: compare result vs repeated `add`; heaps should match.

---

## 8) Quick Reference Tables

**Priority Queue (min-heap)**
| Method | Purpose | Cost |
|---|---|---:|
| `add(x)` | Insert element with priority `x` | O(log n) |
| `getMin()` | Peek smallest | O(1) |
| `removeMin()` | Pop smallest | O(log n) |
| `size()/isEmpty()` | capacity checks | O(1) |

**AVL Rotations**
| Case | Condition | Fix |
|---|---|---|
| LL | balance(z)=+2, balance(z.left) ≥ 0 | rotateRight(z) |
| LR | +2, left child −1 | rotateLeft(z.left), then rotateRight(z) |
| RR | −2, right child ≤ 0 | rotateLeft(z) |
| RL | −2, right child +1 | rotateRight(z.right), then rotateLeft(z) |

---

## 9) Summary
1. **Why is AVL height O(log n)?** Minimal AVL nodes grow ≥ Fib(h+1); invert to get h = O(log n).
2. **After a single insert, how many rotations?** At most 2 (one double or two singles) at the first offending node.
3. **Can deletions cause multiple rebalances?** Yes; under-balance can propagate upward.
4. **Why prefer B-trees on disk?** Fewer, fatter nodes align with blocks → fewer I/Os.
5. **`parent(j)` in heap (0-based)?** `(j−1)/2`.
6. **Complexity of build-heap?** O(n) via bottom-up heapify.
7. **Heapsort stable? space?** Not stable; O(1) extra space.
8. **2–3 tree leaf property?** All leaves at the same depth.
9. **Min-heap `heapifyDown` choice?** Swap with the smaller child.
10. **Tie on AVL deletion grandchildren?** Rotate in the same direction as the taller child.
