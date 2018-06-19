package de.tomahawk.shoppingcalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
//		Item item = new Item("A", 50L, new SpecialPrice(3, 130L));
	    model.put("order", new Order(pc.getItems().get(0), 1));
//		model.put("order", null);
	    model.put("basket", basket);
	    model.put("orders", basket);

		return "calculate";
	}
	
	@PostMapping("/")
	public String add(@ModelAttribute("order") Order order, Model model) {
		// TODO 
		
		Item item = order.getItem();
		System.out.println(item);
		System.out.println(item.getUnitPrice()); // TODO why null?!
		
		pc.addToBasket(order, basket);
		
		model.addAttribute("items", pc.getItems());
		//model.addAttribute("order", order);//TODO needed?
		model.addAttribute("basket", basket);
	
//		Long result = pc.doCheckout(basket);
//		model.addAttribute("totalCost", result);
		
        order = null;
        
	    return "calculate";
	}
		
}
