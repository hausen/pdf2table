/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.
*/

package pdf2xml;

public class Font {

int page;
int id;
int size;	 
String family;
String color;

  public Font(int p,int i,int s,String f,String c) {
  	 this.page = p;
  	 this.id = i;
  	 this.size = s;
  	 this.family = f;
  	 this.color = c;
  }	
}