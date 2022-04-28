package vn.gpay.jitin.core.pcontractproduct;

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

@Table(name="pcontract_products")
@Entity
public class PContractProduct implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_products_generator")
	@SequenceGenerator(name="pcontract_products_generator", sequenceName = "pcontract_products_id_seq", allocationSize=1)
	private Long id;
	private Long pcontractid_link;
	private Long productid_link;
	private Integer pquantity;
	private Long orgrootid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
	
	@Transient
	public String getProductName() {
		if(product!=null) {
			return product.getName();
		}
		return "";
	}
	
	@Transient
	public String getProductCode() {
		if(product!=null) {
			return product.getCode();
		}
		return "";
	}
	
	@Transient
	public String getImgurl1() {
		if(product!=null) {
			return product.getImgurl1();
		}
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
