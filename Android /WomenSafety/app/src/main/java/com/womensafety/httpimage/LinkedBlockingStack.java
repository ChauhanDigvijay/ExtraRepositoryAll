package com.womensafety.httpimage;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedBlockingStack<E> extends AbstractQueue<E> implements BlockingQueue<E> {
    private final Condition cond = this.lock.newCondition();
    private int count;
    private Node<E> head;
    private final ReentrantLock lock = new ReentrantLock();

    class Itr implements Iterator<E> {
        Node<E> current;
        Node<E> last;
        Node<E> next = LinkedBlockingStack.this.getHead();

        Itr() {
        }

        public boolean hasNext() {
            if (this.current != null) {
                return true;
            }
            Node node = this.next;
            this.current = node;
            if (node == null) {
                return false;
            }
            this.next = LinkedBlockingStack.this.getNext(this.next);
            return true;
        }

        public E next() {
            if (this.current != null || hasNext()) {
                this.last = this.current;
                this.current = null;
                return this.last.item;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (this.last == null) {
                throw new IllegalStateException();
            }
            LinkedBlockingStack.this.removeNode(this.last);
        }
    }

    static class Node<E> {
        E item;
        Node<E> next;

        Node(E x, Node<E> n) {
            this.item = x;
            this.next = n;
        }
    }

    public LinkedBlockingStack(Collection<? extends E> c) {
        addAll(c);
    }

    private void insert(E o) {
        this.head = new Node(o, this.head);
        this.count++;
        this.cond.signal();
    }

    private E extract() {
        E x = this.head.item;
        this.head = this.head.next;
        this.count--;
        return x;
    }

    public int size() {
        this.lock.lock();
        try {
            int i = this.count;
            return i;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean offer(E o) {
        if (o == null) {
            throw new NullPointerException();
        }
        this.lock.lock();
        try {
            insert(o);
            return true;
        } finally {
            this.lock.unlock();
        }
    }

    public void put(E o) {
        offer(o);
    }

    public boolean offer(E o, long t, TimeUnit unit) {
        return offer(o);
    }

    public E peek() {
        this.lock.lock();
        try {
            if (this.count == 0) {
                return null;
            }
            E e = this.head.item;
            this.lock.unlock();
            return e;
        } finally {
            this.lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        this.lock.lock();
        while (this.count == 0) {
            try {
                this.cond.await();
            } finally {
                this.lock.unlock();
            }
        }
        E extract = extract();
        return extract;
    }

    public E poll() {
        this.lock.lock();
        try {
            if (this.count == 0) {
                return null;
            }
            E extract = extract();
            this.lock.unlock();
            return extract;
        } finally {
            this.lock.unlock();
        }
    }

    public E poll(long t, TimeUnit unit) throws InterruptedException {
        long ns = unit.toNanos(t);
        this.lock.lock();
        while (this.count == 0) {
            if (ns <= 0) {
                return null;
            }
            try {
                ns = this.cond.awaitNanos(ns);
            } finally {
                this.lock.unlock();
            }
        }
        E extract = extract();
        this.lock.unlock();
        return extract;
    }

    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    public boolean contains(Object o) {
        this.lock.lock();
        try {
            for (Node<E> p = this.head; p != null; p = p.next) {
                if (o.equals(p.item)) {
                    return true;
                }
            }
            this.lock.unlock();
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    public boolean remove(Object o) {
        this.lock.lock();
        Node<E> trail = null;
        try {
            Node<E> p = this.head;
            while (p != null) {
                Node<E> next = p.next;
                if (o.equals(p.item)) {
                    if (trail == null) {
                        this.head = next;
                    } else {
                        trail.next = next;
                    }
                    this.count--;
                    return true;
                }
                trail = p;
                p = next;
            }
            this.lock.unlock();
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    public void clear() {
        this.lock.lock();
        try {
            this.head = null;
            this.count = 0;
        } finally {
            this.lock.unlock();
        }
    }

    public int drainTo(Collection<? super E> c) {
        if (c == null) {
            throw new NullPointerException();
        } else if (c == this) {
            throw new IllegalArgumentException();
        } else {
            this.lock.lock();
            try {
                Node<E> p = this.head;
                this.head = null;
                this.count = 0;
                int n = 0;
                while (p != null) {
                    c.add(p.item);
                    n++;
                    p = p.next;
                }
                return n;
            } finally {
                this.lock.unlock();
            }
        }
    }

    public int drainTo(Collection<? super E> c, int max) {
        if (c == null) {
            throw new NullPointerException();
        } else if (c == this) {
            throw new IllegalArgumentException();
        } else {
            int n = 0;
            while (n < max) {
                E x = poll();
                if (x == null) {
                    break;
                }
                c.add(x);
                n++;
            }
            return n;
        }
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    Node<E> getNext(Node<E> p) {
        this.lock.lock();
        try {
            Node<E> node = p.next;
            return node;
        } finally {
            this.lock.unlock();
        }
    }

    Node<E> getHead() {
        this.lock.lock();
        try {
            Node<E> node = this.head;
            return node;
        } finally {
            this.lock.unlock();
        }
    }

    boolean removeNode(Node<E> x) {
        this.lock.lock();
        Node<E> trail = null;
        try {
            Node<E> p = this.head;
            while (p != null) {
                Node<E> next = p.next;
                if (p == x) {
                    if (trail == null) {
                        this.head = next;
                    } else {
                        trail.next = next;
                    }
                    this.count--;
                    return true;
                }
                trail = p;
                p = next;
            }
            this.lock.unlock();
            return false;
        } finally {
            this.lock.unlock();
        }
    }
}
