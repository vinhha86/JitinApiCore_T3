package vn.gpay.jitin.core.warehouse;

import java.io.Serializable;

import javax.persistence.Column;

public class SKUSpace implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name ="spaceepc_link")
	private String spaceepc_link;
	@Column(name ="itemcount")
	private Integer itemcount;
	@Column(name ="itemtotal")
	private Float itemtotal;
	
	public void SKUSpace (String spaceepc_link, Integer itemcount,Float itemtotal){
		this.spaceepc_link = spaceepc_link;
		this.itemcount = itemcount;
		this.itemtotal = itemtotal;
	}
	public String getSpaceepc_link() {
		return spaceepc_link;
	}
	public void setSpaceepc_link(String spaceepc_link) {
		this.spaceepc_link = spaceepc_link;
	}
	public Integer getItemcount() {
		return itemcount;
	}
	public void setItemcount(Integer itemcount) {
		this.itemcount = itemcount;
	}
	public Float getItemtotal() {
		return itemtotal;
	}
	public void setItemtotal(Float itemtotal) {
		this.itemtotal = itemtotal;
	}
	
}
