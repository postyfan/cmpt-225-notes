# Week 6
## Binary Trees • Traversals (DFS/BFS) • BST (find/insert/remove) • Balancing • AVL (height bounds & rotations)

---

## Minimal Node Used in Examples
```java
// No external libraries
class BTNode<T> {
    T data;
    BTNode<T> left, right, parent;
    BTNode(T d){ data = d; }
}
```

---

## Binary Trees — Essentials (Theory + Intuition)
- **Binary tree**: each node has ≤ 2 children (**left**, **right**).
- **Max nodes at level k**: `2^k`. Depth **d** ⇒ total ≤ `2^(d+1) − 1`.
- **Depth with N nodes**: best ≈ **log₂N** (balanced), worst ≈ **N−1** (path-like).

**Visuals**
```
Balanced (depth ~ log N)         Path-like (depth ~ N-1)
        o                                   o
      /   \                                   \
     o     o                                   o
    / \   / \                                   \
   o  o  o  o                                   o
```

---

## Traversals — Big Picture
| Traversal | Type | Visit Order | Typical Use |
|---|---|---|---|
| **Pre-order** | DFS | Root → Left → Right | Serialize/clone; prefix expressions |
| **In-order** | DFS | Left → Root → Right | Sorted output for **BST** |
| **Post-order** | DFS | Left → Right → Root | Delete/free; evaluate expression trees |
| **Level-order** | BFS | By levels (top→down, left→right) | Layered views; shortest paths in trees |

**Reference Tree Used Below**
```
            (10)
           /    \
         (5)    (21)
        /  \    /  \
      (1)  (7) (16) (25)
```
- **Pre**: 10, 5, 1, 7, 21, 16, 25  
- **In**:  1, 5, 7, 10, 16, 21, 25  
- **Post**:1, 7, 5, 16, 25, 21, 10  
- **Level**:10, 5, 21, 1, 7, 16, 25

---

## DFS Traversals — Templates + Visual Walkthroughs (O(size))

> Same template—just move the “visit/print” line.

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

**Pre-order Visual (Root first)**
```
10
├─ 5
│  ├─ 1
│  └─ 7
└─ 21
   ├─ 16
   └─ 25
Output: 10, 5, 1, 7, 21, 16, 25
```

**In-order Visual (In between children)**
```
Left(10) → 10 → Right(10)
Left(10) = [1,5,7]    Right(10) = [16,21,25]
Output: 1, 5, 7, 10, 16, 21, 25  (BST ⇒ sorted)
```

**Post-order Visual (Root last)**
```
Process subtrees fully, then the node
Output: 1, 7, 5, 16, 25, 21, 10
```

**Iterative Patterns (when avoiding recursion)**

_Pre-order (stack): push right then left_
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
        if(n.right!=null) st.push(n.right);
        if(n.left!=null)  st.push(n.left);
    }
}
```

_In-order (stack + runner to the left spine)_
```java
import java.util.ArrayDeque;
import java.util.Deque;

void inIter(BTNode<Integer> root){
    Deque<BTNode<Integer>> st = new ArrayDeque<>();
    BTNode<Integer> cur = root;
    while(cur!=null || !st.isEmpty()){
        while(cur!=null){ st.push(cur); cur = cur.left; }
        cur = st.pop();
        System.out.println(cur.data);
        cur = cur.right;
    }
}
```

_Post-order (two stacks trick)_
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
    while(!s2.isEmpty()) System.out.println(s2.pop().data); // L R Root
}
```

---

## Level-order / BFS (O(size))

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

**Layered Visual**
```
Level 0: 10
Level 1: 5, 21
Level 2: 1, 7, 16, 25
```

---

## Binary Search Trees (BST) — Property & Core Ops
- **BST property**: Left subtree ≤ node ≤ Right subtree (by key).
- **In-order** on a BST prints keys in **sorted order**.
- **Cost**: find/insert/remove = **O(depth)**.

**Find / Insert / Remove (3 cases)**

```java
// T extends Comparable<T>
BTNode<T> find(BTNode<T> r, T x){
    if(r==null) return null;
    int c = x.compareTo(r.data);
    if(c==0) return r;
    return (c<0) ? find(r.left, x) : find(r.right, x);
}

BTNode<T> insert(BTNode<T> r, T x){
    if(r==null) return new BTNode<>(x);
    int c = x.compareTo(r.data);
    if(c<0) r.left = insert(r.left, x);
    else    r.right= insert(r.right, x); // convention: dups to right
    return r;
}

BTNode<T> min(BTNode<T> n){ while(n.left!=null) n=n.left; return n; }

BTNode<T> remove(BTNode<T> r, T x){
    if(r==null) return null;
    int c = x.compareTo(r.data);
    if(c<0) r.left  = remove(r.left, x);
    else if(c>0) r.right = remove(r.right, x);
    else{
        if(r.left==null)  return r.right;    // 0 or 1 child
        if(r.right==null) return r.left;
        BTNode<T> s = min(r.right);          // successor
        r.data = s.data;
        r.right = remove(r.right, s.data);   // delete successor
    }
    return r;
}
```

**Remove (two-children) Visual**
```
Before:                After copying successor:
      (X)                         (S)
     /   \                       /   \
    …   (Right)      =>         …   (Right with S removed)
        /
      (S=min in Right)
```

---

## Balancing — One-shot vs Self-balancing
- **Rebuild once** from sorted array (in-order → array → median-first build): balanced **now**, but **loses balance** with later updates.
- **Self-balancing trees** (AVL, 2–3, B-Trees) fix height **after every** update.

**Median-First Build (Visual)**
```
Array [1..8] → choose 4/5 as root
Left = build [1..3]       Right = build [6..8]
(recurse choosing middles)
```

---

## AVL Trees — Theory + Rotations
- **Balance rule**: for every node `v`, `|h(v.left) − h(v.right)| ≤ 1`.
- **Height bound**: `height = O(log N)` (via size lower bounds like Fibonacci).
- **Update path** after insert/remove: recompute heights; if a node is ±2, **rotate**.

**Node with height**
```java
class AVLNode<T extends Comparable<T>> {
    T data; AVLNode<T> left, right, parent; int height; // empty child = -1
}
```

**Single Rotations (sketch)**
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

**Rotation Cases (Insert) & Visual Mnemonics**
```
LL case (heavy Left-Left):        RR case (heavy Right-Right):
        z                                  z
       /                                  / \
      y             rotateRight(z)       a   y
     /      =>                         /     \
    x                                  z      x
                                      /        \
                                     b          c

LR case: rotateLeft(y) then rotateRight(z)
RL case: rotateRight(y) then rotateLeft(z)
```
**Quick rule:** “Follow the heavy side twice”  
- LL → Right rotate  
- RR → Left rotate  
- LR → Left then Right  
- RL → Right then Left


