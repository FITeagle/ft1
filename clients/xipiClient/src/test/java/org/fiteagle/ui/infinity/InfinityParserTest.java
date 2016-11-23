package org.fiteagle.ui.infinity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.fiteagle.ui.infinity.model.InfinityArrayList;
import org.fiteagle.ui.infinity.model.InfinityInfrastructure;
import org.fiteagle.ui.infinity.model.InfinityValueID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InfinityParserTest {

	private InfinityParser parser;

	@Before
	public void setup() {
		this.parser = new InfinityParser();
	}

	@Test
	public void testGetTechnicalComponentsParser() throws IOException {
		String input = getMockedInput("/getTechnicalComponentsResponse.json");
		ArrayList<InfinityValueID> result = this.parser
				.parseGetTechnicalComponentsResponse(input);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void testGetInfrastructuresByIdParser() throws IOException {
		String input = getMockedInput("/getInfrastructuresByIdResponse.json");
		InfinityInfrastructure result = this.parser
				.parseGetInfrastructuresById(input);
		Assert.assertNotNull(result);
	}

	@Test
	public void testWritingGetInfrastructuresByIdResponse() throws IOException {
		String input = getMockedInput("/getInfrastructuresByIdResponse.json");
		InfinityInfrastructure result = this.parser
				.parseGetInfrastructuresById(input);
		this.parser.write(new NullOutputStream(), result);
	}

	@Test
	public void testGetComponentDetailResponseParser() throws IOException {
		String input = getMockedInput("/getComponentDetailResponse.json");
		ArrayList<InfinityArrayList> result = this.parser
				.parseGetComponentDetailResponse(input);
		Assert.assertFalse(result.isEmpty());
	}

	@Test
	public void testSearchInfrastructuresResponseParser() throws IOException {
		String input = getMockedInput("/searchInfrastructuresResponse.json");
		ArrayList<InfinityValueID> result = this.parser
				.parseSearchInfrastructuresResponse(input);
		Assert.assertFalse(result.isEmpty());
	}

	private String getMockedInput(String path) {
		InputStream in = this.getClass().getResourceAsStream(path);
		return convertStreamToString(in);
	}

	  /*
	  Parts (or the complete Code) of the following method(s) were inspired or copied
	  from StackOverFlow/StackExchange.
	  Because of the Licensing of StackOverFlow under the CC-BY-SA [*¹] and MIT[*²] License this comment
	  has to link the original code:
	  
	  Link: https://stackoverflow.com/a/5445161
	  */
	private static String convertStreamToString(InputStream is) {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	private class NullOutputStream extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
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
