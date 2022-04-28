package vn.gpay.jitin.core.stockin;

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

@Table(name="stockin_pklist")
@Entity
public class StockInPklist implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_pklist_generator")
	@SequenceGenerator(name="stockout_pklist_generator", sequenceName = "stock_out_pklist_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="stockinid_link")
    private Long stockinid_link;
	
	@Column(name ="stockindid_link")
    private Long stockindid_link;
	
	@Column(name ="skuid_link")
    private Long skuid_link;

	@Column(name ="colorid_link")
    private Long colorid_link;
	
	private Long sizeid_link;
	
	@Column(name ="unitid_link")
    private Integer unitid_link;
	
	@Column(name ="lotnumber",length=100)
    private String lotnumber ;
	
	@Column(name ="packageid")
    private Integer packageid;
	
	@Column(name="ydsorigin")
	private Float ydsorigin;
	
	@Column(name="ydscheck")
	private Float ydscheck;
	
	@Column(name ="width")
    private Float width;
	
	@Column(name ="width_check")
    private Float width_check;
	
	@Column(name ="netweight")
    private Float netweight;
	
	@Column(name ="grossweight")
    private Float grossweight;
	
	@Column(name ="grossweight_check")
    private Float grossweight_check;
	
	@Column(name ="width_yds")
    private Float width_yds;
	
	@Column(name ="width_yds_check")
    private Float width_yds_check;
	
	@Column(name ="width_met")
    private Float width_met;
	
	@Column(name ="width_met_check")
    private Float width_met_check;
	
	@Column(name ="m3")
    private Float m3;
	
	@Column(name ="epc",length =50)
    private String epc;
		
	@Column(name ="skutypeid_link")
	private Integer skutypeid_link;	
	
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
	
	@Column(name = "status")
    private Integer status;
	
	@Column(name ="comment")
    private String comment;
	
	@Column(name ="sample_check")
    private Float sample_check;
	
	private Float met_origin;
	private Float met_check;
	private String barcode;
	private Integer rssi;
	private String spaceepc_link;
	
	// Giá trị cũ trước khi update
	private Float yds_beforecheck;
	private Float met_beforecheck;
	private Float width_yds_beforecheck;
	private Float width_met_beforecheck;
	private Float grossweight_beforecheck;
	
	private Float netweight_lbs;
	private Float grossweight_lbs;
	private Float grossweight_lbs_check;
	
	private Integer is_mobile_checked;
	
	@Transient private String row;
	@Transient private String space;
	@Transient private Integer floor;

	public Integer getRssi() {
		return rssi;
	}
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}
	//mo rong
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
	
	@Transient
	public String getColor_name() {
		if (sku != null) {
			return sku.getColor_name();
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
		if(null!= status && status == 0) {
			if(ydscheck!=null && ydsorigin!=null && ydscheck.equals(ydsorigin)) {
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
	public Long getStockindid_link() {
		return stockindid_link;
	}
	public void setStockindid_link(Long stockindid_link) {
		this.stockindid_link = stockindid_link;
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
	public Float getYdscheck() {
		return ydscheck;
	}
	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}
	public Float getWidth() {
		return width;
	}
	public void setWidth(Float width) {
		this.width = width;
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
	public Float getM3() {
		return m3;
	}
	public void setM3(Float m3) {
		this.m3 = m3;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}
	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
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
	
	public void setSku(SKU sku) {
		this.sku = sku;
	}
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
	public Long getSizeid_link() {
		return sizeid_link;
	}
	public void setSizeid_link(Long sizeid_link) {
		this.sizeid_link = sizeid_link;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Float getWidth_check() {
		return width_check;
	}
	public void setWidth_check(Float width_check) {
		this.width_check = width_check;
	}
	public Float getSample_check() {
		return sample_check;
	}
	public void setSample_check(Float sample_check) {
		this.sample_check = sample_check;
	}
	public Float getGrossweight_check() {
		return grossweight_check;
	}
	public void setGrossweight_check(Float grossweight_check) {
		this.grossweight_check = grossweight_check;
	}
	public Float getWidth_yds() {
		return width_yds;
	}
	public void setWidth_yds(Float width_yds) {
		this.width_yds = width_yds;
	}
	public Float getWidth_yds_check() {
		return width_yds_check;
	}
	public void setWidth_yds_check(Float width_yds_check) {
		this.width_yds_check = width_yds_check;
	}
	public Float getWidth_met() {
		return width_met;
	}
	public void setWidth_met(Float width_met) {
		this.width_met = width_met;
	}
	public Float getWidth_met_check() {
		return width_met_check;
	}
	public void setWidth_met_check(Float width_met_check) {
		this.width_met_check = width_met_check;
	}
	
	public String getSpaceepc_link() {
		return spaceepc_link;
	}
	public void setSpaceepc_link(String spaceepc_link) {
		this.spaceepc_link = spaceepc_link;
	}
	public Float getYds_beforecheck() {
		return yds_beforecheck;
	}
	public void setYds_beforecheck(Float yds_beforecheck) {
		this.yds_beforecheck = yds_beforecheck;
	}
	public Float getMet_beforecheck() {
		return met_beforecheck;
	}
	public void setMet_beforecheck(Float met_beforecheck) {
		this.met_beforecheck = met_beforecheck;
	}
	public Float getWidth_yds_beforecheck() {
		return width_yds_beforecheck;
	}
	public void setWidth_yds_beforecheck(Float width_yds_beforecheck) {
		this.width_yds_beforecheck = width_yds_beforecheck;
	}
	public Float getWidth_met_beforecheck() {
		return width_met_beforecheck;
	}
	public void setWidth_met_beforecheck(Float width_met_beforecheck) {
		this.width_met_beforecheck = width_met_beforecheck;
	}
	public Float getGrossweight_beforecheck() {
		return grossweight_beforecheck;
	}
	public void setGrossweight_beforecheck(Float grossweight_beforecheck) {
		this.grossweight_beforecheck = grossweight_beforecheck;
	}
	public String getRow() {
		return row;
	}
	public void setRow(String row) {
		this.row = row;
	}
	public String getSpace() {
		return space;
	}
	public void setSpace(String space) {
		this.space = space;
	}
	public Integer getFloor() {
		return floor;
	}
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	public Float getNetweight_lbs() {
		return netweight_lbs;
	}
	public void setNetweight_lbs(Float netweight_lbs) {
		this.netweight_lbs = netweight_lbs;
	}
	public Float getGrossweight_lbs() {
		return grossweight_lbs;
	}
	public void setGrossweight_lbs(Float grossweight_lbs) {
		this.grossweight_lbs = grossweight_lbs;
	}
	public Float getGrossweight_lbs_check() {
		return grossweight_lbs_check;
	}
	public void setGrossweight_lbs_check(Float grossweight_lbs_check) {
		this.grossweight_lbs_check = grossweight_lbs_check;
	}
	public Integer getIs_mobile_checked() {
		return is_mobile_checked;
	}
	public void setIs_mobile_checked(Integer is_mobile_checked) {
		this.is_mobile_checked = is_mobile_checked;
	}
	
}
