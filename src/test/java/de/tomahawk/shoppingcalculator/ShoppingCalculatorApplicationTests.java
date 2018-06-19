package de.tomahawk.shoppingcalculator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.tomahawk.shoppingcalculator.model.Item;
import de.tomahawk.shoppingcalculator.model.Order;
import de.tomahawk.shoppingcalculator.model.SpecialPrice;
import de.tomahawk.shoppingcalculator.service.PriceCalculatorService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingCalculatorApplicationTests {

	private List<Item> items;
	private Order order;
	private PriceCalculatorService priceCalculator;
	
	@Test
	public void contextLoads() {
	}
	
    @Before public void initialize() {
       priceCalculator = new PriceCalculatorService();
       items = new ArrayList<Item>();
       items.add(new Item("A", 50L, new SpecialPrice(3, 130L)));
       items.add(new Item("B", 30L, new SpecialPrice(2, 45L)));
	   items.add(new Item("C", 20L, null));
	   items.add(new Item("D", 15L, null));
    }
	
    @Test
    public void testOrder() {
    	final List<Order> orders = new ArrayList<Order>();
    	order = new Order(items.get(0), 3);
    	orders.add(order);
    	order = new Order(items.get(1), 2);
    	orders.add(order);
    	
    	Long totalCosts = priceCalculator.doCheckout(orders);
    	assertEquals(Long.valueOf(175L), totalCosts);
    	
    	orders.forEach(o -> System.out.print(o.getQuantity() + " x " + o.getItem().getName() + " = " + priceCalculator.calculateTotalCosts(o) + "; "));
    	System.out.println("Total Costs: " + totalCosts);
    	
    	System.out.println("##########");
    	
    	order = new Order(items.get(2), 1);
    	orders.add(order);
    	
    	order = new Order(items.get(3), 1);
    	orders.add(order);
    	
    	totalCosts = priceCalculator.doCheckout(orders);
    	assertEquals(Long.valueOf(210L), totalCosts);
    	orders.forEach(o -> System.out.print(o.getQuantity() + " x " + o.getItem().getName() + " = " + priceCalculator.calculateTotalCosts(o) + "; "));
    	System.out.println("Total Costs: " + totalCosts);
    	
    	System.out.println("##########");
    }
    
	@Test
	public void testCalculate() {
		  order = new Order(items.get(0), 0);
		  Long totalCost = priceCalculator.calculateTotalCosts(order);
		  System.out.println(order.getQuantity() + " x " + order.getItem().getName() + " = " + totalCost);
		  assertEquals(Long.valueOf(0L), totalCost);
		  
		  order = new Order(items.get(0), 1);
		  totalCost = priceCalculator.calculateTotalCosts(order);
		  System.out.println(order.getQuantity() + " x " + order.getItem().getName() + " = " + totalCost);
		  assertEquals(Long.valueOf(50L), totalCost);
		  
		  order = new Order(items.get(0), 2);
		  totalCost = priceCalculator.calculateTotalCosts(order);
		  System.out.println(order.getQuantity() + " x " + order.getItem().getName() + " = " + totalCost);
		  assertEquals(Long.valueOf(100L), totalCost);
		  
		  order = new Order(items.get(0), 3);
		  totalCost = priceCalculator.calculateTotalCosts(order);
		  System.out.println(order.getQuantity() + " x " + order.getItem().getName() + " = " + totalCost);
		  assertEquals(Long.valueOf(130L), totalCost);
		  
		  order = new Order(items.get(0), 4);
		  totalCost = priceCalculator.calculateTotalCosts(order);
		  System.out.println(order.getQuantity() + " x " + order.getItem().getName() + " = " + totalCost);
		  assertEquals(Long.valueOf(180L), totalCost);
		  
		  order = new Order(items.get(0), 5);
		  totalCost = priceCalculator.calculateTotalCosts(order);
		  System.out.println(order.getQuantity() + " x " + order.getItem().getName() + " = " + totalCost);
		  assertEquals(Long.valueOf(230L), totalCost);
		  
		  order = new Order(items.get(0), 6);
		  totalCost = priceCalculator.calculateTotalCosts(order);
		  System.out.println(order.getQuantity() + " x " + order.getItem().getName() + " = " + totalCost);
		  assertEquals(Long.valueOf(260L), totalCost);
	}

}
