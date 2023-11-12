package model.builder;

import model.AudioBook;
import model.Book;

import java.time.LocalDate;
import java.util.Date;

public class AudioBookBuilder {

    private AudioBook audiobook;


    public AudioBookBuilder(){
        audiobook = new AudioBook();
    }

    public AudioBookBuilder setId(Long id){
        audiobook.setId(id);
        return this;
    }
    public AudioBookBuilder setLength(String length){
        audiobook.setLength(length);
        return this;
    }
    public AudioBookBuilder setAuthor(String author){
        audiobook.setAuthor(author);
        return this;
    }

    public AudioBookBuilder setTitle(String title){
        audiobook.setTitle(title);
        return this;
    }

    public AudioBookBuilder setPublishedDate(LocalDate publishedDate){
        audiobook.setPublishedDate(publishedDate);
        return this;
    }

    public AudioBook build()
    {
        return audiobook;
    }


}
