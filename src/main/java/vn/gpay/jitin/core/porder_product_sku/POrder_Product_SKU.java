package vn.gpay.jitin.core.porder_product_sku;

import java.io.Serializable;
import java.util.Date;

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

import vn.gpay.jitin.core.porder.POrder;
import vn.gpay.jitin.core.sku.SKU;

@Table(name="porders_product_skus")
@Entity
public class POrder_Product_SKU implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porders_product_skus_generator")
	@SequenceGenerator(name="porders_product_skus_generator", sequenceName = "porders_product_skus_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long porderid_link;
	private Long productid_link;
	private Long skuid_link;
	private Integer pquantity_sample;
	private Integer pquantity_porder;
	private Integer pquantity_total;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private POrder porder;
	
	@Transient
	public Date getOrderdate() {
		if(porder!=null)
			return porder.getOrderdate();
		return null;
	}
	
	@Transient
	public int getProductionyear() {
		return porder.getProductionyear();
	}
	
	@Transient
	public int getTotalorder() {
		return porder.getTotalorder() == null ? 0 : porder.getTotalorder();
	}
	
	@Transient
	public String getSeason() {
		return porder.getSeason();
	}
	
	@Transient
	public Integer getPorder_year() {
		return porder.getProductionyear();
	}
	
	@Transient
	public String getSku_product_code() {
		if (sku!= null)
			return sku.getProduct_code();
		return "";
	}
	@Transient
	public Long getColorid_link() {
		if (sku!= null)
			return sku.getColorid_link();
		return (long)0;
	}
	
	@Transient
	public Long getSizeid_link() {
		if (sku!= null)
			return sku.getSizeid_link();
		return (long)0;
	}
	
	@Transient
	public String getProductName() {
		if(sku!=null)
			return sku.getName();
		return "";
	}
	
	@Transient
	public String getSkucode() {
		if (sku!= null)
			return sku.getCode();
		return "";
	}
	
	@Transient
	public String getSkuname() {
		if (sku!= null)
			return sku.getName();
		return "";
	}
	
	@Transient
	public String getOrdercode() {
		if(porder!=null)
			return porder.getOrdercode();
		return"";
	}
	
	@Transient
	public String getColor_name() {
		if(sku!=null)
			return sku.getColor_name();
		return "";
	}
	
	@Transient
	public String getSize_name() {
		if(sku!=null)
			return sku.getSize_name();
		return "";
	}
	
	public POrder getPorder() {
		return porder;
	}

	@Transient
	public Integer getSkutypeid_link() {
		if (sku!= null)
			return sku.getSkutypeid_link();
		return 0;
	}
	
	@Transient
	public Integer getUnitid_link() {
		if (sku!= null)
			return sku.getUnitid_link() == null ? 0 : sku.getUnitid_link();
		return (Integer)0;
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
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Integer getPquantity_sample() {
		return pquantity_sample;
	}
	public void setPquantity_sample(Integer pquantity_sample) {
		this.pquantity_sample = pquantity_sample;
	}
	public Integer getPquantity_porder() {
		return pquantity_porder;
	}
	public void setPquantity_porder(Integer pquantity_porder) {
		this.pquantity_porder = pquantity_porder;
	}
	public Integer getPquantity_total() {
		return pquantity_total;
	}
	public void setPquantity_total(Integer pquantity_total) {
		this.pquantity_total = pquantity_total;
	}
	
	
}
