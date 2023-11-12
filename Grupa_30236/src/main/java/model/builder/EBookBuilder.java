package model.builder;

import model.Book;
import model.EBook;

import java.time.LocalDate;
import java.util.Date;

public class EBookBuilder {

    private EBook ebook;


    public EBookBuilder(){
        ebook = new EBook();
    }

    public EBookBuilder setId(Long id){
        ebook.setId(id);
        return this;
    }
    public EBookBuilder setFormat(String format){
        ebook.setFormat(format);
        return this;
    }
    public EBookBuilder setAuthor(String author){
        ebook.setAuthor(author);
        return this;
    }

    public EBookBuilder setTitle(String title){
        ebook.setTitle(title);
        return this;
    }

    public EBookBuilder setPublishedDate(LocalDate publishedDate){
        ebook.setPublishedDate(publishedDate);
        return this;
    }

    public EBook build()
    {
        return ebook;
    }


}
