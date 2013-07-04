/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.
*/

import pdf2xml.*;
import java.io.*;
import java.lang.*;


public class pdf2table {
	
	public static void main(String args[]) {
	
	try {
	  if (args.length == 0) {
	   userinterface uid = new userinterface();
       uid.show();
      }
	  else if (args.length == 2) {
	
	     File source = new File(args[0]);
	     File target = new File(args[1]);
	  
	     if (source.isFile() && target.isDirectory()) {
		   String file_name = source.getName();
	       if (file_name.endsWith(".pdf")) {   
             int i = file_name.indexOf(".pdf");
			 file_name = file_name.substring(0,i);			 

			 execute_converter ec = new execute_converter(file_name,source.toString(),target+"\\"+file_name.toString(),"","",false);			 
		   }
		   else {
		     throw new Exception("The source file must be a PDF file (example.pdf)!");
		   }
	     }
	     else {
	       throw new Exception();
	     }
	  }
	  else if (args.length == 4) {
	     File source = new File(args[0]);
         File target = new File(args[1]);  
	     int f = Integer.parseInt(args[2]);
	     int l = Integer.parseInt(args[3]);
         if (source.isFile() && target.isDirectory()) {
           String file_name = source.getName();
	       if (file_name.endsWith(".pdf")) {   
             int i = file_name.indexOf(".pdf");
			 file_name = file_name.substring(0,i);
			 

             execute_converter ec = new execute_converter(file_name,source.toString(),target.toString()+"\\"+file_name,args[2],args[3],false);
		   }
		   else {
             throw new Exception("The source file must be a PDF file (example.pdf)!");
		   }
	     }
         else {
	      throw new Exception();
	     }
	   }
	   else {
	     throw new Exception();
	   }
	   
	 }
	 catch (Exception e) {
	  System.out.println("Usage: pdf2table [<PDF-file> <target>] [<f> <l>]");
	  System.out.println("    PDF-file <String> : PDF file with an '.pdf' ending");
      System.out.println("    target <String>   : folder for the returned documents");
	  System.out.println("    f <int>           : first page to extract");
	  System.out.println("    l <int>           : last page to extract");
	 }
    }      
}