package com.tutorialspoint.stateless;
 
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
 

/*
 @Stateless(
 	//mappedName = "officefacade" // This is vendor specific
	name = "LibrarySessionBean/remote" // This is standard
)
*/
@Stateless(name="LibrarySessionBean")
//@EJB(name="LibrarySessionBean", beanInterface = LibrarySessionBeanRemote.class)
public class LibrarySessionBean implements LibrarySessionBeanRemote {
    
    List<String> bookShelf;    
    
    public LibrarySessionBean(){
       bookShelf = new ArrayList<String>();
    }
    
    public void addBook(String bookName) {
        bookShelf.add(bookName);
    }    
 
    public List<String> getBooks() {
        return bookShelf;
    }
    
}