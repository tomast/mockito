package com.tieto.mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

	private OrderService service;
	@Mock
	private OrderConnector connectorMock;
	private final static String GET_ORDER = "getting order";

	@Before
	public void setUp() throws Exception {
		service = new OrderService();
		service.setConnector(connectorMock);
	}

	@Test
	public void testGetOrder() {
		when(connectorMock.getOrder()).thenReturn(GET_ORDER);
		String result = service.getOrder();
		assertEquals(GET_ORDER, result);
	}

	@Test
	public void testPrintOrder() {
		service.printOrder();
		verify(connectorMock, times(1)).printOrder();
		verify(connectorMock, never()).getOrder();
	}

	@Test
	public void testSpy() {
		OrderService spy = spy(service);
		when(spy.getOrder()).thenReturn(GET_ORDER);
		
		String result = spy.getOrder();
		assertEquals(GET_ORDER, result);
	}
	
	@Test(expected = IOException.class)
	public void OutputStreamWriter_rethrows_an_exception_from_OutputStream()
			throws IOException {
		OutputStream mock = mock(OutputStream.class);
		OutputStreamWriter osw = new OutputStreamWriter(mock);
		doThrow(new IOException()).when(mock).close();
		osw.close();
	}
}
