/*
    Copyright 2005, 2005 Burcu Yildiz
    Contact: burcu.yildiz@gmail.com
    
    This file is part of pdf2table.    pdf2table is free software: you can redistribute it and/or modify    it under the terms of the GNU General Public License as published by    the Free Software Foundation, either version 3 of the License, or    (at your option) any later version.    pdf2table is distributed in the hope that it will be useful,    but WITHOUT ANY WARRANTY; without even the implied warranty of    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the    GNU General Public License for more details.    You should have received a copy of the GNU General Public License    along with pdf2table.  If not, see <http://www.gnu.org/licenses/>.
*/

package pdf2xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class PDF2XML {

    public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in))) return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                (current == 0xA) ||
                (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }
    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
    
    public static void writeToFile(String content, String filename) throws IOException{
        PrintWriter out = new PrintWriter(filename);
        out.print(content);
        out.close();
    }
    
    public static void cleanFile(String filename){
        try{
            String content = readFile(filename);
            content = stripNonValidXMLCharacters(content);
            writeToFile(content,filename);
        }catch(Exception e){}
    }
    
    
    public static void convert(String f, String s, String t, String from,
            String to, boolean interactive_extraction) {
        try {
            System.out.println(t);

            File my_file = new File(t);
            my_file.mkdirs();

            File my_file2 = new File(t, "pdf2xml.dtd");
            FileOutputStream fos = new FileOutputStream(my_file2);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            try {
                build_dtd(osw);
            } finally {
                osw.close();
                fos.close();
            }

            String cmd = "";

            try {
                Runtime rt = Runtime.getRuntime();

                if (from.equals("") || to.equals("")) {

                    cmd = "pdftohtml -xml " + s + " " + t + File.separator + f;
                    System.out.println(cmd);
                    Process p = rt.exec(cmd);
                    p.waitFor();
                } else {
                    try {
                        int a = Integer.parseInt(from);
                        int b = Integer.parseInt(to);
                        cmd = "pdftohtml -f " + a + " -l " + b + " -xml " + s
                                + " " + t + File.separator + f;
                        System.out.println(cmd);
                        Process p = rt.exec(cmd);
                        p.waitFor();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                String filename = t + File.separator + f + ".xml"; 
                cleanFile(filename);
                FirstClassification fc = new FirstClassification(
                        interactive_extraction, t);
                fc.run(filename);

            } catch (IOException ie) {
                System.out.println("Error: " + ie);
            } catch (InterruptedException ie2) {
                System.out.println("The program pdftohtml was interrupted.");
            }
        } catch (Exception e) {
            System.out
                    .println("Exception in class: PDF2XML and method: constructor. "
                            + e);
        }
    }
	
    public static void build_dtd(OutputStreamWriter osw) throws IOException {
	
        String dtd = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n" +
                    "<!ELEMENT pdf2xml (page+,line*,fontspec*)>\n" +
                    "<!ELEMENT page (fontspec*, text*)>\n" +
                    "<!ATTLIST page\n" +
                    	"number CDATA #REQUIRED\n" +
                    	"position CDATA #REQUIRED\n" +
                    	"top CDATA #REQUIRED\n" +
                    	"left CDATA #REQUIRED\n" +
                    	"height CDATA #REQUIRED\n" +
                    	"width CDATA #REQUIRED\n" +
                    ">\n" +
                    "<!ELEMENT fontspec EMPTY>\n" +
                    "<!ATTLIST fontspec\n" +
                    	"id CDATA #REQUIRED\n" +
                    	"size CDATA #REQUIRED\n" +
                    	"family CDATA #REQUIRED\n" +
                    	"color CDATA #REQUIRED\n" +
                    ">\n" +
                    "<!ELEMENT text (#PCDATA | b | i)*>\n" +
                    "<!ATTLIST text\n" +
                    	"top CDATA #REQUIRED\n" +
                    	"left CDATA #REQUIRED\n" +
                    	"width CDATA #REQUIRED\n" +
                    	"height CDATA #REQUIRED\n" +
                    	"font CDATA #REQUIRED\n" +
                    ">\n" +
                    "<!ELEMENT b (#PCDATA)>\n" +
                    "<!ELEMENT i (#PCDATA)>\n" +
                    "<!ELEMENT line (text+)>\n" +
                    "<!ATTLIST line\n" +
                    	"typ CDATA #REQUIRED\n" +
                    	"top CDATA #REQUIRED\n" +
                    	"left CDATA #REQUIRED\n" +
                    	"font CDATA #REQUIRED\n" +
                    ">";

        osw.write(dtd, 0, dtd.length());
    }
}