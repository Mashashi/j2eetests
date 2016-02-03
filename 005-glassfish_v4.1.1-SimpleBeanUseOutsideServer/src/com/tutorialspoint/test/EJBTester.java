package com.tutorialspoint.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl;
import com.sun.enterprise.naming.impl.SerialInitContextFactory;
import com.tutorialspoint.stateless.LibrarySessionBeanRemote;
 
/**
 * Add the glassfish-client.jar to the class path rather than coping it!
 * 
 * @author Rafael
 *
 */
public class EJBTester {
   
   private static String beanPath = "java:global/SimpleBean/LibrarySessionBean";
   
   BufferedReader brConsoleReader = null; 
   Properties props;
   InitialContext ctx;
   {
      props = new Properties();
      
      /*try {
    	  props.load(new FileInputStream("jndi.properties"));
      } catch (IOException ex) {
    	  ex.printStackTrace();
      }*/
      
      
      props.put(Context.PROVIDER_URL, "tcp://localhost:3700");
      props.put(Context.INITIAL_CONTEXT_FACTORY, SerialInitContextFactory.class.getName());
      props.put(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
      props.setProperty(Context.STATE_FACTORIES, JNDIStateFactoryImpl.class.getName());
      // optional.  Defaults to localhost.  Only needed if web server is running 
      // on a different host than the appserver 
      props.setProperty("org.omg.CORBA.ORBInitialHost", "localhost");
      // optional.  Defaults to 3700.  Only needed if target orb port is not 3700.
      props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
      
      try {
    	  ctx = new InitialContext(props);            
      } catch (NamingException ex) {
    	  ex.printStackTrace();
      }
      
      brConsoleReader = 
      new BufferedReader(new InputStreamReader(System.in));
   }
   public static void main(String[] args) {
 
      EJBTester ejbTester = new EJBTester();
 
      ejbTester.testStatelessEjb();
   }
   private void showGUI(){
      System.out.println("**********************");
      System.out.println("Welcome to Book Store");
      System.out.println("**********************");
      System.out.print("Options \n1. Add Book\n2. Exit \nEnter Choice: ");
   }
   private void testStatelessEjb(){
      try {
         int choice = 1; 
         LibrarySessionBeanRemote libraryBean = 
        		 (LibrarySessionBeanRemote) ctx.lookup(beanPath);
         while (choice != 2) {
            String bookName;
            showGUI();
            String strChoice = brConsoleReader.readLine();
            choice = Integer.parseInt(strChoice);
            if (choice == 1) {
               System.out.print("Enter book name: ");
               bookName = brConsoleReader.readLine();                    
               libraryBean.addBook(bookName);          
            }else if (choice == 2) {
               break;
            }
         }
         List<String> booksList = libraryBean.getBooks();
         System.out.println("Book(s) entered so far: " + booksList.size());
         for (int i = 0; i < booksList.size(); ++i) {
         System.out.println((i+1)+". " + booksList.get(i));
         }
         LibrarySessionBeanRemote libraryBean1 = 
         (LibrarySessionBeanRemote)ctx.lookup(beanPath);
         List<String> booksList1 = libraryBean1.getBooks();
         System.out.println("***Using second lookup to get library stateless object***");
         System.out.println(
         "Book(s) entered so far: " + booksList1.size());
         for (int i = 0; i < booksList1.size(); ++i) {
            System.out.println((i+1)+". " + booksList1.get(i));
         }
      } catch (Exception e) {
         System.out.println(e.getMessage());
         e.printStackTrace();
      }finally {
         try {
            if(brConsoleReader !=null){
               brConsoleReader.close();
            }
         } catch (IOException ex) {
            System.out.println(ex.getMessage());
         }
      }
   }  
}