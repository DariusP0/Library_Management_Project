package model;

public class AudioBook extends Book{
    private String length;

    public void setLength(String length) {
        this.length = length;
    }

    public String getLength() {
        return length;
    }

    @Override
    public String toString(){
        return super.toString()+ String.format(" length: %s", this.length);
    }
}
