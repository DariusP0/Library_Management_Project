package model;

import java.time.LocalDate;

public class EBook extends Book{
    private  String format;

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String toString(){
        return super.toString()+ String.format(" Format: %s", this.format);
    }
}
