package vn.gpay.jitin.core.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import vn.gpay.jitin.core.productattributevalue.ProductAttributeValue;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.utils.AtributeFixValues;

@Table(name="product")
@Entity
public class Product implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
	@SequenceGenerator(name="product_generator", sequenceName = "product_id_seq", allocationSize=1)
	private Long id;
	
	private Long orgrootid_link;
	
	private String code;
	
	private String name;
	private Long unitid_link;
	
	private Integer product_type;
	
	private Long designerid_link;
	
	private String buyercode;
	private String buyername;
	private String vendorcode;
	private String vendorname;
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBuyercode() {
		return buyercode;
	}

	public void setBuyercode(String buyercode) {
		this.buyercode = buyercode;
	}

	public String getBuyername() {
		return buyername;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}

	public String getVendorcode() {
		return vendorcode;
	}

	public void setVendorcode(String vendorcode) {
		this.vendorcode = vendorcode;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	private Integer status;
	
	private Long usercreateid_link;
	
	private Date timecreate;
	
	private String imgurl1;
	
	private String imgurl2;
	
	private String imgurl3;
	
	private String imgurl4;
	
	private String imgurl5;
	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="producttypeid_link",insertable=false,updatable =false)
    private ProductType producttype;
	
	@Transient
	public String getProducttype_name() {
		if(producttype != null) {
			return producttype.getName();
		}
		return null;
	}	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="designerid_link",insertable=false,updatable =false)
    private GpayUser designerName;
	
	@Transient
	public String getDesignerName() {
		if(designerName!=null) {
			return designerName.getFullname();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@Transient
	public String getUnitName() {
		if(unit != null)
			return unit.getName();
		return "";
	}
	
	//Tạm thời đóng lại khi có bảng may mẫu sẽ mở ra để hiển thị
//	@Setter(AccessLevel.NONE)
//	private String samplemakerid_link;
	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Set<ProductAttributeValue> listPAvalue = new HashSet<>();
		
	@Transient
	public String getProduct_typeName() {
		String name = "";
		switch (product_type) {
		case 2:
			name = "Nguyên liệu";
			break;
		case 3:
			name = "Phụ liệu may";
			break;
		case 4:
			name = "Phụ liệu hoàn thiện";
			break;
		default:
			break;
		}
		return name;
	}
	
	@Transient
	public List<Long> getProductAttribute() {
		List<Long> lst = new ArrayList<Long>();
		for (ProductAttributeValue pav : listPAvalue) {
			if(lst.contains((long)pav.getAttributeid_link())) continue;
			
			lst.add(pav.getAttributeid_link());
		}
		return lst;
	}
	
	@Transient
    public String getTenMauNPL() {
    	String name ="";
    	for (ProductAttributeValue pav : listPAvalue) {
			if(pav.getAttributeid_link() == AtributeFixValues.ATTR_COLOR) {
				if(pav.getAttributevalueid_link() != 0) {
					String attName = pav.getAttributeValueName();
					if (!attName.toLowerCase().equals("all")){
						if(name == "") {
							name += attName;
						}else {
							name += ", "+attName;
						}
					}
				}
			}
		}
    	return name;
    }
	
	@Transient
    public String getThanhPhanVai() {
    	String name ="";
    	for (ProductAttributeValue pav : listPAvalue) {
			if(pav.getAttributeid_link() == AtributeFixValues.ATTR_FABRIC) {
				if(pav.getAttributevalueid_link() != 0) {
					if(name == "") {
						name += pav.getAttributeValueName();
					}else {
						name += ", "+ pav.getAttributeValueName();
					}
				}
			}
		}
    	return name;
    }
	
	@Transient
    public String getCoKho() {
    	String name ="";
    	for (ProductAttributeValue pav : listPAvalue) {
			if(pav.getAttributeid_link() == AtributeFixValues.ATTR_SIZEWIDTH) {
				if(pav.getAttributevalueid_link() != 0) {
					if(name == "") {
						name += pav.getAttributeValueName().replace(" ", "");
					}else {
						name += ", "+ pav.getAttributeValueName().replace(" ", "");;
					}
				}
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}

	public Integer getProduct_type() {
		return product_type;
	}

	public void setProduct_type(Integer product_type) {
		this.product_type = product_type;
	}

	public Long getDesignerid_link() {
		return designerid_link;
	}

	public void setDesignerid_link(Long designerid_link) {
		this.designerid_link = designerid_link;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUsercreateid_link() {
		return usercreateid_link;
	}

	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}

	public Date getTimecreate() {
		return timecreate;
	}

	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
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

	public String getImgurl4() {
		return imgurl4;
	}

	public void setImgurl4(String imgurl4) {
		this.imgurl4 = imgurl4;
	}

	public String getImgurl5() {
		return imgurl5;
	}

	public void setImgurl5(String imgurl5) {
		this.imgurl5 = imgurl5;
	}

	public Set<ProductAttributeValue> getListPAvalue() {
		return listPAvalue;
	}

	public void setListPAvalue(Set<ProductAttributeValue> listPAvalue) {
		this.listPAvalue = listPAvalue;
	}

	public void setDesignerName(GpayUser designerName) {
		this.designerName = designerName;
	}
	
	
}
