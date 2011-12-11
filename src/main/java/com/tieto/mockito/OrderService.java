package com.tieto.mockito;

public class OrderService {
	
	private OrderConnector connector;
	
	public String getOrder() {
		return connector.getOrder();
	}

	public void printOrder() {
		connector.printOrder();
	}
	
	public void setConnector(OrderConnector connector) {
		this.connector = connector;
	}
}
