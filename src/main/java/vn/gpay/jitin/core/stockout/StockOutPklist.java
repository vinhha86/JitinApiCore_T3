package vn.gpay.jitin.core.stockout;

import java.io.Serializable;
import java.util.Date;

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

import vn.gpay.jitin.core.category.Unit;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.warehouse.Warehouse;

@Table(name="stockout_pklist")
@Entity
public class StockOutPklist implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_pklist_generator")
	@SequenceGenerator(name="stockout_pklist_generator", sequenceName = "stock_out_pklist_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="stockoutid_link")
    private Long stockoutid_link;
	
	@Column(name ="stockoutdid_link")
    private Long stockoutdid_link;
	
	@Column(name ="skuid_link")
    private Long skuid_link;
	
	@Column(name ="skutypeid_link")
    private Integer skutypeid_link ;	

	@Column(name ="colorid_link")
    private Long colorid_link;
	
	@Column(name ="unitid_link")
    private Integer unitid_link;
	
	@Column(name ="lotnumber",length=100)
    private String lotnumber ;
	
	@Column(name ="packageid")
    private Integer packageid;
	
	@Column(name="ydsorigin")
	private Float ydsorigin;
	@Transient
    private Float ydsoriginold;		
	
	@Column(name="ydscheck")
	private Float ydscheck;
	
	@Column(name="widthorigin")
	private Float widthorigin;
	
	@Transient
    private Float ydscheckold;	
	
	@Transient
    private Float ydsprocessedold;		
	
	@Transient
    private Float widthoriginold;	

	@Transient
    private Float widthcheckold;	
	
	@Transient
    private Float widthprocessedold;		
	
	@Transient
    private Float totalerrorold;	
	
	@Column(name ="netweight")
    private Float netweight;
	
	@Column(name ="grossweight")
    private Float grossweight;
	
	@Column(name ="grossweight_lbs")
    private Float grossweight_lbs;
	
	@Column(name ="grossweight_check")
    private Float grossweight_check; 
	
	@Column(name ="grossweight_lbs_check")
    private Float grossweight_lbs_check;
	
	@Column(name ="epc",length =50)
    private String epc;
	
	@Column(name ="rssi")
	private Integer rssi;		
	
	@Column(name ="status")
	private Integer status;				
	
	@Column(name ="encryptdatetime")
    private Date encryptdatetime;
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	private String extrainfo;
	private Float met_origin;
	private Float met_check;
	private Float met_processed;
	private Float widthcheck;
	
	private Integer warehousestatus;
	
	@Transient
	private String spaceString;
	
	@Transient
	private Float met_remain; // ware house met còn (xé vải)
	@Transient
	private Float yds_remain; // ware house yds còn (xé vải)
	@Transient
	private Float met_remain_and_check; // tổng met xé + còn
	@Transient
	private Float yds_remain_and_check; // tổng yds xé + còn
	@Transient
	private String warehouseStatusString;
	@Transient
	private Date date_check;

	//mo rong
	@Transient
	public String getColor_name() {
		if(sku!=null)
			return sku.getColor_name();
		return "";
	}
	public String getSkucode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
		
	}
	public String getSkuname() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	
	public String getHscode() {
		if(sku!=null) {
			return sku.getHscode();
		}
		return "";
		
	}
	
	public String getHsname() {
		if(sku!=null) {
			return sku.getHsname();
		}
		return "";
	}
	
	
//	public String getColorcode() {
//		if(color!=null) {
//			return color.getCode();
//		}
//		return "";
//		
//	}
//	public String getColorname() {
//		if(color!=null) {
//			return color.getName();
//		}
//		return "";
//		
//	}
//	public String getColorRGB() {
//		if(color!=null) {
//			return color.getRgbvalue();
//		}
//		return "";
//		
//	}
	
	@Transient
	public Integer getChecked() {
		if(status!=null) {
			if(status == 0) {
				return 1;
			}else {
				return 0;
			}
		}
		return 0;
	}
	
	@Transient
	public String getWarning() {
		if(null!=status && status == 0) {
			if(null!=ydscheck && null!=ydsorigin && ydscheck.equals(ydsorigin)) {
				return "warning2";
			}else {
				return "warning1";
			}
		}
		return "warning0";
	}
	
	@Transient
	public String getUnitname() {
		if(unit!=null) {
			return unit.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",updatable =false,insertable =false)
    private SKU sku;
	
//	@NotFound(action = NotFoundAction.IGNORE)
//	@ManyToOne
//    @JoinColumn(name="colorid_link",updatable =false,insertable =false)
//    private Color color;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="epc",referencedColumnName="epc",insertable=false,updatable =false)
    private Warehouse warehouse;
	
	@Transient
	public String getStockinProductString() {
		if(warehouse!=null) {
			return warehouse.getStockinProductString();
		}
		return "";
	}
	
	@Transient
	public String getInvoice() {
		if(warehouse!=null) {
			return warehouse.getInvoice();
		}
		return "";
	}
	
	@Transient
	public Date getNgayNhapKho() {
		if(warehouse!=null) {
			return warehouse.getTimecreate();
		}
		return null;
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
	public Long getStockoutid_link() {
		return stockoutid_link;
	}
	public void setStockoutid_link(Long stockoutid_link) {
		this.stockoutid_link = stockoutid_link;
	}
	public Long getStockoutdid_link() {
		return stockoutdid_link;
	}
	public void setStockoutdid_link(Long stockoutdid_link) {
		this.stockoutdid_link = stockoutdid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public Integer getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}
	public String getLotnumber() {
		return lotnumber;
	}
	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}
	public Integer getPackageid() {
		return packageid;
	}
	public void setPackageid(Integer packageid) {
		this.packageid = packageid;
	}
	public Float getYdsorigin() {
		return ydsorigin;
	}
	public void setYdsorigin(Float ydsorigin) {
		this.ydsorigin = ydsorigin;
	}
	public Float getYdsoriginold() {
		return ydsoriginold;
	}
	public void setYdsoriginold(Float ydsoriginold) {
		this.ydsoriginold = ydsoriginold;
	}
	public Float getYdscheck() {
		return ydscheck;
	}
	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}
	public Float getYdscheckold() {
		return ydscheckold;
	}
	public void setYdscheckold(Float ydscheckold) {
		this.ydscheckold = ydscheckold;
	}
	public Float getYdsprocessedold() {
		return ydsprocessedold;
	}
	public void setYdsprocessedold(Float ydsprocessedold) {
		this.ydsprocessedold = ydsprocessedold;
	}
	public Float getWidthoriginold() {
		return widthoriginold;
	}
	public void setWidthoriginold(Float widthoriginold) {
		this.widthoriginold = widthoriginold;
	}
	public Float getWidthcheckold() {
		return widthcheckold;
	}
	public void setWidthcheckold(Float widthcheckold) {
		this.widthcheckold = widthcheckold;
	}
	public Float getWidthprocessedold() {
		return widthprocessedold;
	}
	public void setWidthprocessedold(Float widthprocessedold) {
		this.widthprocessedold = widthprocessedold;
	}
	public Float getTotalerrorold() {
		return totalerrorold;
	}
	public void setTotalerrorold(Float totalerrorold) {
		this.totalerrorold = totalerrorold;
	}
	public Float getNetweight() {
		return netweight;
	}
	public void setNetweight(Float netweight) {
		this.netweight = netweight;
	}
	public Float getGrossweight() {
		return grossweight;
	}
	public void setGrossweight(Float grossweight) {
		this.grossweight = grossweight;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public Integer getRssi() {
		return rssi;
	}
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}
	public Date getEncryptdatetime() {
		return encryptdatetime;
	}
	public void setEncryptdatetime(Date encryptdatetime) {
		this.encryptdatetime = encryptdatetime;
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
	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}
	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}
	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}
	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
//	public SKU getSku() {
//		return sku;
//	}
//	public void setSku(SKU sku) {
//		this.sku = sku;
//	}
//	public Color getColor() {
//		return color;
//	}
//	public void setColor(Color color) {
//		this.color = color;
//	}
//	public Unit getUnit() {
//		return unit;
//	}
//	public void setUnit(Unit unit) {
//		this.unit = unit;
//	}
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Float getWidthorigin() {
		return widthorigin;
	}
	public void setWidthorigin(Float widthorigin) {
		this.widthorigin = widthorigin;
	}
	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}
	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
	}
	public Float getMet_origin() {
		return met_origin;
	}
	public void setMet_origin(Float met_origin) {
		this.met_origin = met_origin;
	}
	public Float getMet_check() {
		return met_check;
	}
	public void setMet_check(Float met_check) {
		this.met_check = met_check;
	}
	public Float getMet_processed() {
		return met_processed;
	}
	public void setMet_processed(Float met_processed) {
		this.met_processed = met_processed;
	}
	public String getExtrainfo() {
		return extrainfo;
	}
	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}
	public Float getWidthcheck() {
		return widthcheck;
	}
	public void setWidthcheck(Float widthcheck) {
		this.widthcheck = widthcheck;
	}
	public Integer getWarehousestatus() {
		return warehousestatus;
	}
	public void setWarehousestatus(Integer warehousestatus) {
		this.warehousestatus = warehousestatus;
	}
	public Float getMet_remain() {
		return met_remain;
	}
	public void setMet_remain(Float met_remain) {
		this.met_remain = met_remain;
	}
	public Float getYds_remain() {
		return yds_remain;
	}
	public void setYds_remain(Float yds_remain) {
		this.yds_remain = yds_remain;
	}
	public Float getMet_remain_and_check() {
		return met_remain_and_check;
	}
	public void setMet_remain_and_check(Float met_remain_and_check) {
		this.met_remain_and_check = met_remain_and_check;
	}
	public Float getYds_remain_and_check() {
		return yds_remain_and_check;
	}
	public void setYds_remain_and_check(Float yds_remain_and_check) {
		this.yds_remain_and_check = yds_remain_and_check;
	}
	public String getSpaceString() {
		return spaceString;
	}
	public void setSpaceString(String spaceString) {
		this.spaceString = spaceString;
	}
	public String getWarehouseStatusString() {
		return warehouseStatusString;
	}
	public void setWarehouseStatusString(String warehouseStatusString) {
		this.warehouseStatusString = warehouseStatusString;
	}
	public Date getDate_check() {
		return date_check;
	}
	public void setDate_check(Date date_check) {
		this.date_check = date_check;
	}
	public Float getGrossweight_lbs() {
		return grossweight_lbs;
	}
	public void setGrossweight_lbs(Float grossweight_lbs) {
		this.grossweight_lbs = grossweight_lbs;
	}
	public Float getGrossweight_check() {
		return grossweight_check;
	}
	public void setGrossweight_check(Float grossweight_check) {
		this.grossweight_check = grossweight_check;
	}
	public Float getGrossweight_lbs_check() {
		return grossweight_lbs_check;
	}
	public void setGrossweight_lbs_check(Float grossweight_lbs_check) {
		this.grossweight_lbs_check = grossweight_lbs_check;
	}
	
}
