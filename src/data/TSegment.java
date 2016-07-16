package data;

public class TSegment {

    private boolean reverseFrag;
    private int[] elements;
    private final int ID;

    public TSegment(int id, int[] elements) {
	this.setElements(elements);
	this.ID = id;
    }

    public boolean getReverseFrag() {
	return reverseFrag;
    }

    public void setReverseFrag(boolean reverseFrag) {
	this.reverseFrag = reverseFrag;
    }

    public int getElement(int index) {
	return elements[index];
    }

    public void setElements(int[] elements) {
	this.elements = elements;
    }

    public void conbine(int[] elements, boolean isLeft) {

    }

}
