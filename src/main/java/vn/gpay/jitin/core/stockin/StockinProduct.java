package vn.gpay.jitin.core.stockin;

import java.io.Serializable;

import javax.persistence.Column;
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

@Table(name="stockin_product")
@Entity
public class StockinProduct implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockin_product_generator")
	@SequenceGenerator(name="stockin_product_generator", sequenceName = "stockin_product_id_seq", allocationSize=1)
	protected Long id;
	
	// id, orgrootid_link, stockinid_link, productid_link
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name = "stockinid_link")
	private Long stockinid_link;

	@Column(name = "productid_link")
	private Long productid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
	private Product product;
	
	@Transient
	public String getProduct_code() {
		if(product != null)
			return product.getBuyercode();
		return "";
	}
	
	@Transient
	public String getProduct_name() {
		if(product != null)
			return product.getBuyername();
		return "";
	}
	
	@Transient
	public String getProduct_desc() {
		if(product != null)
			return product.getDescription();
		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Long getStockinid_link() {
		return stockinid_link;
	}

	public void setStockinid_link(Long stockinid_link) {
		this.stockinid_link = stockinid_link;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	
}
