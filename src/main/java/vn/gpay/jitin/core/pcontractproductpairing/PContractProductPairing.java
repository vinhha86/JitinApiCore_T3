package vn.gpay.jitin.core.pcontractproductpairing;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.jitin.core.product.Product;

@Table(name="pcontract_product_pairing")
@Entity
public class PContractProductPairing implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_product_pairing_generator")
	@SequenceGenerator(name="pcontract_product_pairing_generator", sequenceName = "pcontract_product_pairing_id_seq", allocationSize=1)
	private Long id;
	private Long pcontractid_link;
	private Long productpairid_link;
	private Long orgrootid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productpairid_link",insertable=false,updatable =false)
    private Product product;
	
	@Transient
	public String getproductpairName(){
		if(product != null)
			return product.getName();
		return "";
	}
	
	@Transient
	public String getproductpairCode(){
		if(product != null)
			return product.getCode();
		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}

	public Long getProductpairid_link() {
		return productpairid_link;
	}

	public void setProductpairid_link(Long productpairid_link) {
		this.productpairid_link = productpairid_link;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
