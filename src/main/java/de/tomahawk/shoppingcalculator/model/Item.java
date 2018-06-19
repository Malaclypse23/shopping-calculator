package de.tomahawk.shoppingcalculator.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Item {

	@NotNull
	private String name;
	
	@NotNull
	@Min(0L)
	private Long unitPrice;

	private SpecialPrice specialPrice;
	
	public Item() {
	}
	 
	public Item(String name, Long unitPrice, SpecialPrice specialPrice) {
		super();
		this.name = name;
		this.unitPrice = unitPrice;
		this.specialPrice = specialPrice;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}

	public SpecialPrice getSpecialPrice() {
		return specialPrice;
	}

	public void setSpecialPrice(SpecialPrice specialPrice) {
		this.specialPrice = specialPrice;
	}
	
}
