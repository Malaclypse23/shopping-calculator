package de.tomahawk.shoppingcalculator.model;

import java.beans.Transient;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Order {

	public Order() {
	}
	
	public Order(Item item, Integer quantity) {
		super();
		this.item = item;
		this.quantity = quantity;
	}

	@NotNull
	private Item item;
	
	@NotNull
	@Min(0)
	private Integer quantity;

	private Long totalCost;
		
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Transient
	public Long getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Long totalCost) {
		this.totalCost = totalCost;
	}
	
}
