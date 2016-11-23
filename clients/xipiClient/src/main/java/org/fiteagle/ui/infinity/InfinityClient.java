package org.fiteagle.ui.infinity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import org.fiteagle.ui.infinity.model.InfinityArrayList;
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;

public abstract class InfinityClient {
	
	static int i = 0;

	protected InfinityParser parser;

	protected enum Methods {
		GET_INFRA_BY_ID("getInfrastructuresById"), GET_COMPONENT_BY_ID(
				"getComponentById"), SEARCH_INFRASTRUCTURES(
				"searchInfrastructures"),
				GET_TECHNICAL_COMPONENTS("getTechnicalComponents"),
				GET_COMPONENT_DETAIL("getComponentDetail");
		private String value;

		private Methods(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public InfinityClient() {
		this.parser = new InfinityParser();
	}
	
	

	public abstract InfinityInfrastructure getInfrastructuresById(Number i);

	public abstract ArrayList<InfinityValueID> searchInfrastructures();
	
	public abstract ArrayList<InfinityValueID> getTechnicalComponents();
	public abstract ArrayList<InfinityArrayList> getComponentDetail(String infrastructureId, String componentId);
	

	public InputStream fixXIPIEncoding(InputStream in) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = in.read(buffer)) > -1) {
				out.write(buffer, 0, len);
			}
		} catch (IOException e2) {
			throw new RuntimeException(e2.getMessage());
		}
		try {
			out.flush();
		} catch (IOException e1) {
			throw new RuntimeException(e1.getMessage());
		}

		byte[] utf8 = null;
		try {
			utf8 = new String(out.toByteArray(), "ISO-8859-1").getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
		
//		FileOutputStream fileOutput = null;
//		try {
//			fileOutput = new FileOutputStream(new File("/home/ozanoo/testSil/responseFromXIPI"+i));
//			i++;
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			out.writeTo(fileOutput);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return new ByteArrayInputStream(utf8);
	}

	
	  /*
	  Parts (or the complete Code) of the following method(s) were inspired or copied
	  from StackOverFlow/StackExchange.
	  Because of the Licensing of StackOverFlow under the CC-BY-SA [*¹] and MIT[*²] License this comment
	  has to link the original code:
	  
	  Link: https://stackoverflow.com/a/5445161
	  */
	public String convertStreamToString(InputStream is) {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}


}

/*
[1] https://creativecommons.org/licenses/by-sa/3.0/


[2] https://opensource.org/licenses/MIT
	The MIT License (MIT)
	Copyright (c) 2016 StackExchange

	Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files 
	(the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, 
	publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
	subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
	FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION 
	WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
