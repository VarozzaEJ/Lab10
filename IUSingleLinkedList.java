import java.util.*;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * 
 * @param <E> type to store
 */
public class IUSingleLinkedList<E> implements IndexedUnsortedList<E> {
	private LinearNode<E> front, rear;
	private int count;
	private int modCount;
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		front = rear = null;
		count = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(E element) {
		LinearNode<E> newNode = new LinearNode<E>(element);
		newNode.setNext(front);
		front = newNode;
		if (rear == null) {
			rear = newNode;
		}
		count++;
		modCount++;
	}

	@Override
	public void addToRear(E element) {
		LinearNode<E> newNode = new LinearNode<E>(element);
		if (rear != null) {
			rear.setNext(newNode);
		} else {
			front = newNode;
		}
		rear = newNode;
		count++;
		modCount++;
		
	}

	@Override
	public void add(E element) {
		LinearNode<E> newNode = new LinearNode<E>(element);
		if (rear != null) {
			rear.setNext(newNode);
		} else {
			front = newNode;
		}
		modCount++;
		count++;
		rear = newNode;
	}

	@Override
	public void addAfter(E element, E target) {
		LinearNode<E> current = front;
		while (current != null && !current.getElement().equals(target)) {
			current = current.getNext();
		}
		// Target not found
		if (current == null) {
			throw new NoSuchElementException();
		}
		LinearNode<E> newNode = new LinearNode<E>(element);
		newNode.setNext(current.getNext());
		current.setNext(newNode);
		if (newNode.getNext() == null) {
			rear = newNode;
		}
		count++;
		modCount++;
		
	}

	@Override
	public void add(int index, E element) {
		LinearNode<E> newNode = new LinearNode<E>(element);
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}
		if (index == 0) {
			newNode.setNext(front);
			front = newNode;
			if (rear == null) {
				rear = newNode;
			}
		} else {
			LinearNode<E> current = front, previous = null;
			for (int i = 0; i < index; i++) {
				previous = current;
				current = current.getNext();
			}
			previous.setNext(newNode);
			newNode.setNext(current);
			if (newNode.getNext() == null) {
				rear = newNode;
			}
		}
		count++;
		modCount++;
		
	}

	@Override
	public E removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return removeElement(null, front);
	}

	@Override
	public E removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		LinearNode<E> current = front, previous = null;
		while (current.getNext() != null) {
			previous = current;
			current = current.getNext();
		}
		return removeElement(previous, current);
	}

	@Override
	public E remove(E element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		LinearNode<E> current = front, previous = null;
		while (current != null && !current.getElement().equals(element)) {
			previous = current;
			current = current.getNext();
		}
		// Matching element not found
		if (current == null) {
			throw new NoSuchElementException();
		}
		return removeElement(previous, current);		
	}

	@Override
	public E remove(int index) {
		LinearNode<E> current = front, previous = null;
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		for (int i = 0; i < index; i++) {
			previous = current;
			current = current.getNext();
		}
		return removeElement(previous, current);
	}

	@Override
	public void set(int index, E element) {
		LinearNode<E> current = front;
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		System.out.println();
		current.setElement(element);
		modCount++;
	}

	@Override
	public E get(int index) {
		LinearNode<E> current = front;
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		return current.getElement();
	}

	@Override
	public int indexOf(E element) {
		LinearNode<E> current = front;
		int index = 0;
		while (current != null) {
			if (current.getElement().equals(element)) {
				return index;
			}
			current = current.getNext();
			index++;
		}
		return -1;
	}

	@Override
	public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return front.getElement();
    }

	@Override
	public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return rear.getElement();
    }	

	@Override
	public boolean contains(E target) {
		LinearNode<E> current = front;
		while (current != null) {
			if (current.getElement().equals(target)) {
				return true;
			}
			current = current.getNext();
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return count;
	}

	@Override
	public String toString() {
		String result = "[";
		LinearNode<E> current = front;
		while (current != null) {
			result += current.getElement().toString();
			if (current.getNext() != null) {
				result += ", ";
			}
			current = current.getNext();
		}
		result += "]";
		return result;
	}

	private E removeElement(LinearNode<E> previous, LinearNode<E> current) {
		// Grab element
		E result = current.getElement();
		// If not the first element in the list
		if (previous != null) {
			previous.setNext(current.getNext());
		} else { // If the first element in the list
			front = current.getNext();
		}
		// If the last element in the list
		if (current.getNext() == null) {
			rear = previous;
		}
		count--;
		modCount++;

		return result;
	}

	@Override
	public Iterator<E> iterator() {
		return new SLLIterator();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<E> {
		private LinearNode<E> previous;
		private LinearNode<E> current;
		private LinearNode<E> next;
		private int iterModCount;
		private boolean removeAble;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return next != null;
        }

		@Override
		public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (current != null) {
                previous = current;
            }
            current = next;
            next = next.getNext();
            removeAble = true;
            return current.getElement();
        }
		
		@Override
		public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
			if (!removeAble) {
				throw new IllegalStateException();
			}
            if (current == null) {
                throw new IllegalStateException();
            }
            if (previous == null) {
                front = next;
            } else {
                previous.setNext(next);
            }
            if (current == rear) {
                rear = previous;
            }
            current = null;
            removeAble = false;
            count--;
            modCount++;
            iterModCount++;
        }
	}

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}
}
