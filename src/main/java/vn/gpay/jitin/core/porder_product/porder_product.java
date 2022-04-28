package vn.gpay.jitin.core.porder_product;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="porders_products")
@Entity
public class porder_product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porders_products_generator")
	@SequenceGenerator(name="porders_products_generator", sequenceName = "porders_products_id_seq", allocationSize=1)
	private Long id;
	
	private Long porderid_link;
	private Long productid_link;
	private Integer pquantity;
	private Long orgrootid_link;
	private Float price;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Integer getPquantity() {
		return pquantity;
	}
	public void setPquantity(Integer pquantity) {
		this.pquantity = pquantity;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	
	
}
