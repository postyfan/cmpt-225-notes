# Week 06 
**Binary Trees • DFS/BFS Traversals (deep focus) • BST (find/insert/remove) • Balancing • AVL (theory + rotations) • 2-3 Trees • B-Trees**

---

## 1) Binary Trees — essentials
- **Binary tree**: each node has ≤ **2** children: **left**, **right**.
- **Max nodes at level k**: `2^k` → depth **d** has ≤ `2^(d+1) − 1` nodes.
- **Depth bounds (N nodes)**: `Ω(log N)` (balanced) … `N−1` (path-like chain).

**Java skeleton**
```java
// No external libs needed
class BTNode<T> { T data; BTNode<T> left, right, parent; BTNode(T d){data=d;} }
class BinaryTree<T> { BTNode<T> root; }
```

**Quick visual**
```
Balanced       Path-like
    o               o
  /   \               \
 o     o               o
/ \   / \               \
```

---

## 2) Tree Traversals — the big picture (what/when)
| Traversal | Type | Visit Order | Typical Use |
|---|---|---|---|
| **Pre-order** | DFS | **Root → Left → Right** | Serialize/clone tree; prefix expressions |
| **In-order** | DFS | **Left → Root → Right** | Sorted output for **BST** |
| **Post-order** | DFS | **Left → Right → Root** | Free/delete trees; evaluate expression trees |
| **Level-order** | BFS | **Level by level** | Shortest paths (unweighted); “layered” views |

**Sample tree (used below)**
```
        (10)
       /    \
     (5)    (21)
    /  \    /  \
  (1)  (7) (16) (25)
```
- **Pre**: 10, 5, 1, 7, 21, 16, 25  
- **In**:  1, 5, 7, 10, 16, 21, 25  *(sorted for BST)*  
- **Post**:1, 7, 5, 16, 25, 21, 10  
- **Level**:10, 5, 21, 1, 7, 16, 25

---

## 3) DFS Traversals — minimal recursive templates (O(size))
> Same template; only the **position** of “visit” line changes.

```java
// No external libs needed
void pre(BTNode<Integer> r){
    if(r==null) return;
    System.out.println(r.data);   // Root
    pre(r.left);                  // Left
    pre(r.right);                 // Right
}

void in(BTNode<Integer> r){
    if(r==null) return;
    in(r.left);                   // Left
    System.out.println(r.data);   // Root
    in(r.right);                  // Right
}

void post(BTNode<Integer> r){
    if(r==null) return;
    post(r.left);                 // Left
    post(r.right);                // Right
    System.out.println(r.data);   // Root
}
```

**Mnemonics**
- **Pre** = **Pre**fix → print **before** children.
- **In** = **In** between → print **between** left/right.
- **Post** = **Post** (after) → print **after** children.

---

## 4) Iterative Traversals (how to think without recursion)

### 4.1 Pre-order (stack)
```java
import java.util.ArrayDeque;
import java.util.Deque;

void preIter(BTNode<Integer> root){
    if(root==null) return;
    Deque<BTNode<Integer>> st = new ArrayDeque<>();
    st.push(root);
    while(!st.isEmpty()){
        BTNode<Integer> n = st.pop();
        System.out.println(n.data);
        if(n.right!=null) st.push(n.right); // push right first
        if(n.left!=null)  st.push(n.left);  // so left is processed next
    }
}
```

### 4.2 In-order (stack + runner)
```java
import java.util.ArrayDeque;
import java.util.Deque;

void inIter(BTNode<Integer> root){
    Deque<BTNode<Integer>> st = new ArrayDeque<>();
    BTNode<Integer> cur = root;
    while(cur!=null || !st.isEmpty()){
        while(cur!=null){ st.push(cur); cur = cur.left; } // go left
        cur = st.pop();                                   // visit
        System.out.println(cur.data);
        cur = cur.right;                                  // then right
    }
}
```

### 4.3 Post-order (two stacks trick)
```java
import java.util.ArrayDeque;
import java.util.Deque;

void postIter(BTNode<Integer> root){
    if(root==null) return;
    Deque<BTNode<Integer>> s1 = new ArrayDeque<>();
    Deque<BTNode<Integer>> s2 = new ArrayDeque<>();
    s1.push(root);
    while(!s1.isEmpty()){
        BTNode<Integer> n = s1.pop();
        s2.push(n);
        if(n.left!=null)  s1.push(n.left);
        if(n.right!=null) s1.push(n.right);
    }
    while(!s2.isEmpty()) System.out.println(s2.pop().data); // Left, Right, Root
}
```

---

## 5) Level-order / BFS (O(size))
```java
import java.util.ArrayDeque;
import java.util.Queue;

void levelOrder(BTNode<Integer> root){
    if(root==null) return;
    Queue<BTNode<Integer>> q = new ArrayDeque<>();
    q.add(root);
    while(!q.isEmpty()){
        BTNode<Integer> n = q.remove();
        System.out.println(n.data);
        if(n.left!=null)  q.add(n.left);
        if(n.right!=null) q.add(n.right);
    }
}
```

**Per-level processing (width, etc.)**
```java
int maxWidth = 0;
Queue<BTNode<Integer>> q = new ArrayDeque<>();
q.add(root);
while(!q.isEmpty()){
    int levelSize = q.size();
    maxWidth = Math.max(maxWidth, levelSize);
    for(int i=0;i<levelSize;i++){
        BTNode<Integer> n = q.remove();
        if(n.left!=null)  q.add(n.left);
        if(n.right!=null) q.add(n.right);
    }
}
```

**Pattern:** queue = frontier-by-layers.

---

## 6) Binary Search Trees (BST) — property & ops
- **Property**: Left subtree ≤ node ≤ Right subtree (by key).
- **In-order** on a BST prints **sorted** values.
- All ops cost **O(depth)** (path length).

**Find / Insert / Remove (3 cases)**
```java
// T extends Comparable<T>
BTNode<T> find(BTNode<T> r, T x){
    if(r==null) return null;
    int c = x.compareTo(r.data);
    if(c==0) return r;
    return (c<0) ? find(r.left,x) : find(r.right,x);
}

BTNode<T> insert(BTNode<T> r, T x){
    if(r==null){ BTNode<T> n=new BTNode<>(x); return n; }
    int c = x.compareTo(r.data);
    if(c<0) r.left = insert(r.left,x);
    else    r.right= insert(r.right,x); // put dups to right (chosen convention)
    return r;
}

BTNode<T> min(BTNode<T> n){ while(n.left!=null) n=n.left; return n; }

BTNode<T> remove(BTNode<T> r, T x){
    if(r==null) return null;
    int c = x.compareTo(r.data);
    if(c<0) r.left  = remove(r.left,x);
    else if(c>0) r.right = remove(r.right,x);
    else{
        if(r.left==null)  return r.right;          // 0 or 1 child
        if(r.right==null) return r.left;
        BTNode<T> s = min(r.right);                 // 2 children: successor
        r.data = s.data;
        r.right = remove(r.right, s.data);
    }
    return r;
}
```

**Why balance matters**
```
Balanced: height ~ log N → ops ~ O(log N)
Chain:    height ~ N     → ops ~ O(N)
```

---

## 7) Balancing — one-shot vs self-balancing
- Rebuilding from a **sorted array** (in-order → array → median-first build) gives a **balanced** tree **now**, but not after future updates.
- Prefer **self-balancing** trees that fix height **after every insert/remove** (e.g., **AVL**, **2-3**, **B-Trees**).

**Median-first build (concept)**
```
mid = (lo+hi)/2 → root
recurse left on [lo..mid-1]
recurse right on [mid+1..hi]
```

---

## 8) AVL Trees — theory & rotations
- **Balance rule**: for every node `v`, `|h(v.left) − h(v.right)| ≤ 1`.
- **Height bounds**: from size lower bounds (e.g., Fibonacci), `height = O(log N)`.
- **Update path** after insert/remove: recompute **heights**; if some node has balance ±2 → **rotate**.

**Node with height**
```java
class AVLNode<T extends Comparable<T>> {
    T data; AVLNode<T> left,right,parent; int height; // empty child = -1
}
```

**Rotations (sketch)**
```java
AVLNode<T> rotateRight(AVLNode<T> y){
    AVLNode<T> x = y.left,  T2 = x.right;
    x.right = y; y.left = T2;
    // update heights (y then x)
    return x;
}
AVLNode<T> rotateLeft(AVLNode<T> x){
    AVLNode<T> y = x.right, T2 = y.left;
    y.left = x; x.right = T2;
    // update heights (x then y)
    return y;
}
```

**Cases (insert)**
- **LL** → Right rotate  
- **RR** → Left rotate  
- **LR** → Left rotate (on left child) + Right rotate  
- **RL** → Right rotate (on right child) + Left rotate

**Mnemonic**
```
Follow the heavy side twice:
LL→R, RR→L, LR→L then R, RL→R then L
```

---

## 9) 2-3 Trees — perfectly balanced
- Internal nodes have **2 or 3 children**; store **1 or 2 keys**.
- **All leaves same depth** → height `h = Θ(log N)` with `log₃ N − 1 ≤ h ≤ log₂ N`.
- **Insert** at leaf; if overfull, **split** (median up), may cascade.
- **Delete** at leaf (or swap with pred); on underflow, **borrow** else **merge**, may cascade.

**Insert split visual**
```
Leaf [A,B,C] → median B goes up
Left [A]      Right [C]
Parent inserts B between children
```

---

## 10) B-Trees (order-m) — disk-friendly
- Balanced multi-way search trees; **leaves at equal depth**.
- Node (except root) has children in **[ceil(m/2)..m]**; keys separate child ranges.
- **Insertion**: insert at leaf → if node overfull, **split** and push median up (can cascade).
- **Deletion**: delete/replace with predecessor; on **underflow**, **borrow/merge**; may cascade.
- **Why**: fewer, larger nodes → fewer I/Os; rebalancing **amortized O(1)**; height `Θ(log_m N)`.

---

## 11) Operations & Costs (at a glance)
| Structure / Operation | Time (typical worst) | Notes |
|---|---|---|
| **DFS/BFS traversals** | **O(size)** | Touch each node once |
| **BST find/insert/remove** | **O(depth)** | Path length = height |
| **AVL find/insert/remove** | **O(log N)** | Height bounded; few rotations |
| **2-3 Tree ops** | **Θ(log N)** | Perfectly balanced |
| **B-Tree ops** | **Θ(log N)** | Split/merge amortized O(1) |

---

## 12) Quick “choose-the-traversal” guide
| Task | Traversal | Why |
|---|---|---|
| Print BST in sorted order | **In-order** | Left ≤ Root ≤ Right |
| Save / clone tree shape | **Pre-order** | Root-first captures structure |
| Delete/free nodes safely | **Post-order** | Children before parent |
| Get nodes by distance from root | **Level-order (BFS)** | Visits by layers |

---

## 13) Common traversal pitfalls (fix fast)
- **Mixing L/R**: In-order must be **Left → Root → Right**.
- **Pre-order iterative**: push **right first**, then **left**.
- **In-order iterative**: after visiting, always `cur = cur.right`.
- **Post-order iterative**: two-stack method is simplest to get **Left,Right,Root**.
- **Null checks**: every traversal should guard `if (root == null) return;`.

---

## 14) Minimal node class used above
```java
class BTNode<T> { T data; BTNode<T> left, right; BTNode(T d){data=d;} }
```

End_of_Notes
