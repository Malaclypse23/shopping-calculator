package de.tomahawk.shoppingcalculator.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import de.tomahawk.shoppingcalculator.model.Item;
import de.tomahawk.shoppingcalculator.model.Order;
import de.tomahawk.shoppingcalculator.model.SpecialPrice;

@Service
public class PriceCalculatorService {
	
	private final List<Item> items = new ArrayList<Item>();

	@PostConstruct
	public void initialize() {
		if (items.isEmpty()) {
			items.add(new Item("A", 50L, new SpecialPrice(3, 130L)));
			items.add(new Item("B", 30L, new SpecialPrice(2, 45L)));
		    items.add(new Item("C", 20L, null));
		    items.add(new Item("D", 15L, null));
		}
	}
	
	public List<Item> getItems() {
		return this.items;
	}
	
	public Long doCheckout(List<Order> orders) {
		Long result = 0L;
		for (Order order : orders) {
			result += calculateTotalCosts(order);
		}
		return result;
	}
	
	public Long calculateTotalCosts(Order order) {
		if (order == null || order.getItem() == null || order.getQuantity() == null) {
			return 0L;
		}
		
		SpecialPrice sp = getSpecialPrice(order);
		// TODO get rid of stupid nullcheck
		if (sp != null) {
			return calculateCost(order.getQuantity(), order.getItem().getUnitPrice(), sp.getQuantity(), sp.getPrice()); 
		} 
		
		// no special price, return unitPrice * quantity
		return order.getItem().getUnitPrice() * order.getQuantity();
	}
	
	public Long calculateCost(int desired, long unitPrice, int specialQuantity, long specialPrice) {
		Long result = 0L;

		while (desired > 0) {
			if (isSpecialPrice(desired, specialQuantity)) {
				result += specialPrice; 
			} else {
				result += desired * unitPrice;
			}
			desired -= specialQuantity;
		}		
		return result;
	}
	
	private Boolean isSpecialPrice(int desired, int quantity) {
		if (desired >= quantity && quantity > 0) {
			return true;
		}
		return false;
	}
	
	/** Get Special Price For Item::name: with highest special quantity
	 * 	No Special Price For Item or order quantity not sufficient: return null
	 *  */
	private SpecialPrice getSpecialPrice(Order order) {
		final int desired = order.getQuantity();
		
		if (order.getItem().getSpecialPrice() != null && isSpecialPrice(desired, order.getItem().getSpecialPrice().getQuantity())) {
			return order.getItem().getSpecialPrice();
		}
		return null;
//		return specialPrices.stream()
//			.filter(sp -> sp.getItem().getName().equals(name) && isSpecialPrice(desired, sp.getQuantity()))
//			.max(Comparator.comparing(SpecialPrice::getQuantity))
//			.orElse(null);
	}

	public void addToBasket(Order order, List<Order> basket) {
		for (Order o : basket) {
			if (o.getItem().getName().equals(order.getItem().getName())) {
				int oldCount = o.getQuantity();
				o.setQuantity(oldCount + order.getQuantity());
				return;
			}
		}
//		order.setTotalCost(calculateTotalCosts(order));
		basket.add(order);
	}

}
