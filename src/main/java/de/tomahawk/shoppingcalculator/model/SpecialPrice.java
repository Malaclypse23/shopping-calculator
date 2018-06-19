package de.tomahawk.shoppingcalculator.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SpecialPrice {
	
	@NotNull
	@Min(2)
	private Integer quantity;
	
	@NotNull
	@Min(1L)
	private Long price;
	
	public SpecialPrice() {
	}
	
	public SpecialPrice(Integer quantity, Long price) {
		super();
		this.quantity = quantity;
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return this.quantity + " for " + this.price;
	}
	
}
