package de.tomahawk.shoppingcalculator.service;

import java.util.List;

import org.springframework.stereotype.Service;

import de.tomahawk.shoppingcalculator.model.Order;
import de.tomahawk.shoppingcalculator.model.SpecialPrice;

@Service
public class PriceCalculatorService {
	
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
	}

}
