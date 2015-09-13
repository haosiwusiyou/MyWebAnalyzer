package com.cn.derek.temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class tryIO {
  public static void main(String[] args) throws FileNotFoundException{
	  InputStream input = new FileInputStream(new File("/Users/derek/Desktop/mapred-site.xml")) ;
	  FileReader  reader = new FileReader(new File("/Users/derek/Desktop/mapred-site.xml")) ;
	   
  }
}
