package de.tomahawk.shoppingcalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
	
	private final List<Order> basket = new ArrayList<Order>();
	private final List<Item> items = new ArrayList<Item>();
	
	@Autowired
	private PriceCalculatorService pc;
	
	@PostConstruct
	public void initialize() {
		if (items.isEmpty()) {
			items.add(new Item("A", 50L, new SpecialPrice(3, 130L)));
			items.add(new Item("B", 30L, new SpecialPrice(2, 45L)));
		    items.add(new Item("C", 20L, null));
		    items.add(new Item("D", 15L, null));
		}
	}
	
	@RequestMapping("/")
	public String calculate(Map<String, Object> model) throws CloneNotSupportedException {
		model.put("items", items);
		final Order order = new Order((Item) items.get(0).clone(), new Integer(1));
	    model.put("order", order);
		return "calculate";
	}
	
	@PostMapping("/")
	public String add(@ModelAttribute("order") Order order, Map<String, Object> model) {
		model.put("items", items);
		
		final Item item = order.getItem();
		final long unitPrice = items.stream().filter(i -> item.getName().equals(i.getName())).findFirst().get().getUnitPrice();
		item.setUnitPrice(unitPrice);
		final SpecialPrice sp = items.stream().filter(i -> item.getName().equals(i.getName())).findFirst().get().getSpecialPrice();
		item.setSpecialPrice(sp);
		
		final Order existingOrder = basket.stream().filter(o -> o.getItem().getName().equals(item.getName())).findFirst().orElse(null);
		updateQuantity(order, existingOrder);
		
		updateCosts(order, item, sp);
		putBasket(model, order, existingOrder);
		putTotalCost(model, basket);

	    return "calculate";
	}

	private void updateQuantity(Order order, final Order existingOrder) {
		if (existingOrder != null) {
			order.setQuantity(existingOrder.getQuantity() + order.getQuantity());
		} else {
			order.setQuantity(order.getQuantity());
		}
	}

	private void updateCosts(Order order, Item item, SpecialPrice sp) {
		if (sp == null) {
			order.setTotalCost(order.getQuantity() * item.getUnitPrice());
		} else {
			order.setTotalCost(pc.calculateCost(order.getQuantity(), item.getUnitPrice(), sp.getQuantity(), sp.getPrice()));
		}
	}

	private void putBasket(Map<String, Object> model, Order order, Order existingOrder) {
		if (existingOrder == null) {
			basket.add(order);
		} else {
			basket.set(basket.indexOf(existingOrder), order);
		}	
		model.put("basket", basket);
	}

	private void putTotalCost(Map<String, Object> model, List<Order> orders) {
		model.put("totalCost", pc.doCheckout(orders));
	}
		
}
