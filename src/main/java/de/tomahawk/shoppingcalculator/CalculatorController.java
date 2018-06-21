package de.tomahawk.shoppingcalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.tomahawk.shoppingcalculator.model.Item;
import de.tomahawk.shoppingcalculator.model.Order;
import de.tomahawk.shoppingcalculator.model.SpecialPrice;
import de.tomahawk.shoppingcalculator.service.PriceCalculatorService;

@Controller
public class CalculatorController {
	
	private List<Order> basket = new ArrayList<Order>();
	private Long totalCost = 0L;
	
	@Autowired
	private PriceCalculatorService pc;
	

	@RequestMapping("/")
	public String calculate(Map<String, Object> model) {
		
		model.put("items", pc.getItems());
		model.put("totalCost", totalCost);
		
		// reset:
		//Item item = new Item("A", 50L, new SpecialPrice(3, 130L));
		Order order = new Order(pc.getItems().get(0), 1);
	    model.put("order", order);
//		model.put("order", null);
	    
	    
	    model.put("basket", basket);

		return "calculate";
	}
	
	@PostMapping("/")
	public String add(@ModelAttribute("order") Order order, Map<String, Object> model) {
		
		model.put("items", pc.getItems());
		totalCost = 0L;
		
		Item item = order.getItem();
		String name = item.getName();
		
		Order existingOrder = basket.stream().filter(o -> o.getItem().getName().equals(item.getName())).findFirst().orElse(null);
		if (existingOrder != null) {
			int exQ = existingOrder.getQuantity();
			order.setQuantity(exQ + order.getQuantity());
		} else {
			order.setQuantity(order.getQuantity());
			basket.add(order);
		}
		
		long unitPrice = pc.getItems().stream().filter(i -> name.equals(i.getName())).findFirst().get().getUnitPrice();
		item.setUnitPrice(unitPrice);
		SpecialPrice sp = pc.getItems().stream().filter(i -> name.equals(i.getName())).findFirst().get().getSpecialPrice();
		
		if (sp == null) {
			order.setTotalCost(order.getQuantity() * item.getUnitPrice());
		} else {
			item.setSpecialPrice(sp);
			Long tc = pc.calculateCost(order.getQuantity(), unitPrice, sp.getQuantity(), sp.getPrice());
			order.setTotalCost(tc);
		}
		System.out.println(order.getQuantity()); // TODO why null?!

		//pc.addToBasket(order, basket);
		model.put("basket", basket);
		
		System.out.println("Basket Size: " + basket.size());
		
		for (Order o : basket) {
			totalCost += o.getTotalCost();
			System.out.println(o.getQuantity() + " x " + o.getItem().getName() + " = " + o.getTotalCost());
		}
		
		model.put("totalCost", totalCost);
		System.out.println("Total Cost: " + totalCost);
		
        //order = null;
        
	    return "calculate";
	}
		
}
