package vn.gpay.jitin.core.stockout_order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import vn.gpay.jitin.core.sku.SKU;

@Table(name="stockout_order_d")
@Entity
public class Stockout_order_d implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_order_d_generator")
	@SequenceGenerator(name="stockout_order_d_generator", sequenceName = "stockout_order_d_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long stockoutorderid_link;
	private Long material_skuid_link;
	private Long colorid_link;
	private Long unitid_link;
	private Integer totalpackage;
	private Float totalyds;
	private Integer totalpackagecheck;
	private Float totalydscheck;
	private Float unitprice;
	private Long p_skuid_link;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private Float totalmet;
	private Float totalmetcheck;
	
	@Transient
	private String data_spaces; //  danh sách khoang
	
	// thong tin thanh pham
	@Transient
	private String skucode_product;
	@Transient
	private String sku_product_code;
	@Transient
	private String skuname_product;
	@Transient
	private String color_name_product;
	@Transient
	private String size_name_product;
	@Transient
	private String loaiThanhPham;
	@Transient
	private Long totalSLTon;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL)
	//@BatchSize(size=10)
	@JoinColumn( name="stockoutorderdid_link", referencedColumnName="id")
	private List<Stockout_order_pkl>  stockout_order_pkl  = new ArrayList<Stockout_order_pkl>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="material_skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@Transient
	public String getUnitName() {
		if(unit!=null)
			return unit.getName();
		return "";
	}
	
	@Transient
	public String getMaterialCode() {
		if(sku!=null)
			return sku.getProduct_code();
		return "";
	}
	
	@Transient
	public String getMaterialName() {
		if(sku!=null)
			return sku.getProduct_name();
		return "";
	}
	
	@Transient
	public String getTenMauNPL() {
		if(sku!=null)
			return sku.getColor_name();
		return "";
	}
	
	@Transient
	public String getCoKho() {
		if(sku!=null)
			return sku.getSize_name();
		return "";
	}
	
	@Transient
	public String getSkuname() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	
	@Transient
	public String getSkucode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
	}
	
	@Transient
	public String getSku_product_color() {
		if(sku != null) {
			return sku.getProduct_color();
		}
		return "";
	}
	
	@Transient
	public String getSku_product_desc() {
		if(sku != null) {
			return sku.getProduct_desc();
		}
		return "";
	}
	
	@Transient
	public String getColor_name() {
		if (sku != null) {
			return sku.getColor_name();
		}
		return "";
	}
	
//	@Transient
//	public Long getColorid_link() {
//		if (sku != null) {
//			return sku.getColorid_link();
//		}
//		return 0L;
//	}
	
	@Transient
	public String getSize_name() {
		if (sku != null) {
			return sku.getSize_name();
		}
		return "";
	}
	
	public String getUnitname() {
		if (unit != null) {
			return unit.getName();
		}
		return "";
	}
	
	public Long getId() {
		return id;
	}
	public Long getStockoutorderid_link() {
		return stockoutorderid_link;
	}
	public Long getMaterial_skuid_link() {
		return material_skuid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public Float getTotalyds() {
		return totalyds;
	}
	public Integer getTotalpackagecheck() {
		return totalpackagecheck;
	}
	public Float getTotalydscheck() {
		return totalydscheck;
	}
	public Float getUnitprice() {
		return unitprice;
	}
	public Long getP_skuid_link() {
		return p_skuid_link;
	}
	public Long getUsercreateid_link() {
		return usercreateid_link;
	}
	public Date getTimecreate() {
		return timecreate;
	}
	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}
	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setStockoutorderid_link(Long stockoutorderid_link) {
		this.stockoutorderid_link = stockoutorderid_link;
	}
	public void setMaterial_skuid_link(Long material_skuid_link) {
		this.material_skuid_link = material_skuid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public void setTotalyds(Float totalyds) {
		this.totalyds = totalyds;
	}
	public void setTotalpackagecheck(Integer totalpackagecheck) {
		this.totalpackagecheck = totalpackagecheck;
	}
	public void setTotalydscheck(Float totalydscheck) {
		this.totalydscheck = totalydscheck;
	}
	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
	}
	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}
	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}
	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}
	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Float getTotalmet() {
		return totalmet;
	}

	public void setTotalmet(Float totalmet) {
		this.totalmet = totalmet;
	}

	public Float getTotalmetcheck() {
		return totalmetcheck;
	}

	public void setTotalmetcheck(Float totalmetcheck) {
		this.totalmetcheck = totalmetcheck;
	}

	public List<Stockout_order_pkl> getStockout_order_pkl() {
		return stockout_order_pkl;
	}

	public void setStockout_order_pkl(List<Stockout_order_pkl> stockout_order_pkl) {
		this.stockout_order_pkl = stockout_order_pkl;
	}

	public String getData_spaces() {
		return data_spaces;
	}

	public void setData_spaces(String data_spaces) {
		this.data_spaces = data_spaces;
	}
	
	// thong tin thanh pham

	public String getSkucode_product() {
		return skucode_product;
	}

	public void setSkucode_product(String skucode_product) {
		this.skucode_product = skucode_product;
	}

	public String getSku_product_code() {
		return sku_product_code;
	}

	public void setSku_product_code(String sku_product_code) {
		this.sku_product_code = sku_product_code;
	}

	public String getSkuname_product() {
		return skuname_product;
	}

	public void setSkuname_product(String skuname_product) {
		this.skuname_product = skuname_product;
	}

	public String getColor_name_product() {
		return color_name_product;
	}

	public void setColor_name_product(String color_name_product) {
		this.color_name_product = color_name_product;
	}

	public String getSize_name_product() {
		return size_name_product;
	}

	public void setSize_name_product(String size_name_product) {
		this.size_name_product = size_name_product;
	}

	public String getLoaiThanhPham() {
		return loaiThanhPham;
	}

	public void setLoaiThanhPham(String loaiThanhPham) {
		this.loaiThanhPham = loaiThanhPham;
	}

	public Long getTotalSLTon() {
		return totalSLTon;
	}

	public void setTotalSLTon(Long totalSLTon) {
		this.totalSLTon = totalSLTon;
	}
	
}
