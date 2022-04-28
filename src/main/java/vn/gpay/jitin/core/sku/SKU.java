package vn.gpay.jitin.core.sku;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.jitin.core.category.Unit;
import vn.gpay.jitin.core.product.Product;
//import vn.gpay.jitin.core.product.Product;
import vn.gpay.jitin.core.utils.AtributeFixValues;



@Table(name="sku")
@Entity
public class SKU implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sku_generator")
	@SequenceGenerator(name="sku_generator", sequenceName = "sku_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	
	private String code;
	
	private Integer skutypeid_link;
	
	private String name;
	
	private String name_en;
	
	private Integer categoryid_link;
	
	private Integer bossid_link;
	
	private Integer providerid_link;
	
	private Integer fabricid_link;
	
	private Integer unitid_link;
	
	private Integer packingtype;
	
	private String imgurl1;
	
	private String imgurl2;
	
	private String imgurl3;
	
	private String hscode;
	
	private String hsname;
	
	private Float saleprice;
	
	private BigDecimal discountpercent;
	
	private BigDecimal vatpercent;
	
	private Long productid_link;
	
//	private String product_code;
	
	private String provider_code;
	
//	public String getProduct_code() {
//		return product_code;
//	}
//
//	public void setProduct_code(String product_code) {
//		this.product_code = product_code;
//	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;

	@Transient
	public String getProduct_managecode() {
		if(product != null) {
			return product.getCode();
		}
		return null;
	}
	
	@Transient
	public String getProduct_code() {
		if(product != null) {
			return product.getBuyercode();
		}
		return null;
	}
	
	@Transient
	public String getProduct_desc() {
		if(product != null) {
			return product.getDescription();
		}
		return null;
	}
	
	@Transient
	public String getProduct_color() {
		if(product != null) {
			return product.getTenMauNPL();
		}
		return null;
	}
	
	@Transient
	public String getProduct_name() {
		if(product != null) {
			return product.getBuyername();
		}
		return null;
	}
	
	public String getProvider_code() {
		return provider_code;
	}

	public void setProvider_code(String provider_code) {
		this.provider_code = provider_code;
	}

	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private Set<SKU_Attribute_Value> listSKUvalue;
    
    @NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
    
//    @NotFound(action = NotFoundAction.IGNORE)
//	@ManyToOne
//    @JoinColumn(name="productid_link",insertable=false,updatable =false)
//    private Product product;
//    
//    @Transient
//    public String getUnitprice() {
//    	if(product != null)
//    		return product.getU
//    }
    
    @Transient
    public String getColor_name() {
    	String name ="";
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_COLOR) {
				name = sku_Attribute_Value.getAttributeValueName();
				break;
			}
		}
    	return name;
    }
    
    @Transient
    public String getSize_name() {
    	String name ="";
    	for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZE) {
				name = sku_Attribute_Value.getAttributeValueName();
				break;
			}
			else if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZEWIDTH) {
				name = sku_Attribute_Value.getAttributeValueName();
				break;
			}
		}
    	return name;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}

	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_en() {
		return name_en;
	}

	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	public Integer getCategoryid_link() {
		return categoryid_link;
	}

	public void setCategoryid_link(Integer categoryid_link) {
		this.categoryid_link = categoryid_link;
	}

	public Integer getBossid_link() {
		return bossid_link;
	}

	public void setBossid_link(Integer bossid_link) {
		this.bossid_link = bossid_link;
	}

	public Integer getProviderid_link() {
		return providerid_link;
	}

	public void setProviderid_link(Integer providerid_link) {
		this.providerid_link = providerid_link;
	}

	public Integer getFabricid_link() {
		return fabricid_link;
	}

	public void setFabricid_link(Integer fabricid_link) {
		this.fabricid_link = fabricid_link;
	}

	public Integer getPackingtype() {
		return packingtype;
	}

	public void setPackingtype(Integer packingtype) {
		this.packingtype = packingtype;
	}
	
	@Transient
	public Long getColorid_link() {
		long ID_Mau =0;
		for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_COLOR) {
				ID_Mau = sku_Attribute_Value.getAttributevalueid_link();
				break;
			}
//			else if(sku_Attribute_Value.getAttributeid_link() == 36) {
//				ID_Mau = sku_Attribute_Value.getAttributevalueid_link();
//				break;
//			}
		}
		return ID_Mau;
	}
	
	@Transient
	public String getUnit_name() {
		if(unit != null)
			return unit.getName();
		return "";
	}
	
	@Transient
	public Long getSizeid_link() {
		long ID_Co =0;
		for (SKU_Attribute_Value sku_Attribute_Value : listSKUvalue) {
			if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZE) {
				ID_Co = sku_Attribute_Value.getAttributevalueid_link();
				break;
			}
			else if(sku_Attribute_Value.getAttributeid_link() == AtributeFixValues.ATTR_SIZEWIDTH) {
				ID_Co = sku_Attribute_Value.getAttributevalueid_link();
				break;
			}
		}
		return ID_Co;
	}

	public String getImgurl1() {
		return imgurl1;
	}

	public void setImgurl1(String imgurl1) {
		this.imgurl1 = imgurl1;
	}

	public String getImgurl2() {
		return imgurl2;
	}

	public void setImgurl2(String imgurl2) {
		this.imgurl2 = imgurl2;
	}

	public String getImgurl3() {
		return imgurl3;
	}

	public void setImgurl3(String imgurl3) {
		this.imgurl3 = imgurl3;
	}

	public String getHscode() {
		return hscode;
	}

	public void setHscode(String hscode) {
		this.hscode = hscode;
	}

	public String getHsname() {
		return hsname;
	}

	public void setHsname(String hsname) {
		this.hsname = hsname;
	}

	public Float getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(Float saleprice) {
		this.saleprice = saleprice;
	}

	public BigDecimal getDiscountpercent() {
		return discountpercent;
	}

	public void setDiscountpercent(BigDecimal discountpercent) {
		this.discountpercent = discountpercent;
	}

	public BigDecimal getVatpercent() {
		return vatpercent;
	}

	public void setVatpercent(BigDecimal vatpercent) {
		this.vatpercent = vatpercent;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public Set<SKU_Attribute_Value> getListSKUvalue() {
		return listSKUvalue;
	}

	public void setListSKUvalue(Set<SKU_Attribute_Value> listSKUvalue) {
		this.listSKUvalue = listSKUvalue;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Integer getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}

	/**
	 * 
	 */
}
