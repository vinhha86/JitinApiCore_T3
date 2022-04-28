package vn.gpay.jitin.core.stockout_order;

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

@Table(name="stockout_order_pklist")
@Entity
public class Stockout_order_pkl implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_order_pklist_generator")
	@SequenceGenerator(name="stockout_order_pklist_generator", sequenceName = "stockout_order_pklist_id_seq", allocationSize=1)
	private Long id;
	private Long orgid_link;
	private Long stockoutorderid_link;
	private Long stockoutorderdid_link;
	private Long skuid_link;
	private Long colorid_link;
	private String lotnumber;
	private Integer packageid;
	private Float ydsorigin;
	private Float ydscheck;
	private Float width;
	private Float netweight;
	private Float grossweight;
	private Float grossweight_lbs;
	private String epc;
	private Date encryptdatetime;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private String spaceepc_link;
	private Integer status;
	private Float met;
	private Float metorigin;
	private Float metcheck;
	private Float width_yds_check;
	private Float width_yds;
	private Float width_met_check;
	private Float width_met; // width_yds_check, width_yds, width_met_check, width_met
	
	@Transient
	private String spaceString;
	@Transient
	private Integer warehouseStatus;
	@Transient
	private String warehouseStatusString;
	@Transient
	private Float warehouse_check_met_check;
	@Transient
	private Float warehouse_check_width_check;
	@Transient
	private Float warehouse_check_met_err;
	@Transient
	private Date date_check;
	@Transient
	private Boolean isInStock;
	
	
	public Float getMet() {
		return met;
	}

	public void setMet(Float met) {
		this.met = met;
	}
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="stockoutorderdid_link",insertable=false,updatable =false)
    private Stockout_order_d detail;
	
	@Transient
	public String getmaterial_product_code() {
		return detail.getMaterialCode();
	}
	
	@Transient
	public String getColor_name() {
		return detail.getTenMauNPL();
	}
	
	@Transient
	public String getunit_name() {
		return detail.getUnitName();
	}
	
	public Long getId() {
		return id;
	}
	public Long getOrgid_link() {
		return orgid_link;
	}
	public Long getStockoutorderid_link() {
		return stockoutorderid_link;
	}
	public Long getStockoutorderdid_link() {
		return stockoutorderdid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public String getLotnumber() {
		return lotnumber;
	}
	public Integer getPackageid() {
		return packageid;
	}
	public Float getYdsorigin() {
		return ydsorigin;
	}
	public Float getYdscheck() {
		return ydscheck;
	}
	public Float getWidth() {
		return width;
	}
	public Float getNetweight() {
		return netweight;
	}
	public Float getGrossweight() {
		return grossweight;
	}
	public String getEpc() {
		return epc;
	}
	public Date getEncryptdatetime() {
		return encryptdatetime;
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
	public String getSpaceepc_link() {
		return spaceepc_link;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}
	public void setStockoutorderid_link(Long stockoutorderid_link) {
		this.stockoutorderid_link = stockoutorderid_link;
	}
	public void setStockoutorderdid_link(Long stockoutorderdid_link) {
		this.stockoutorderdid_link = stockoutorderdid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}
	public void setPackageid(Integer packageid) {
		this.packageid = packageid;
	}
	public void setYdsorigin(Float ydsorigin) {
		this.ydsorigin = ydsorigin;
	}
	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}
	public void setWidth(Float width) {
		this.width = width;
	}
	public void setNetweight(Float netweight) {
		this.netweight = netweight;
	}
	public void setGrossweight(Float grossweight) {
		this.grossweight = grossweight;
	}
	
	public Float getGrossweight_lbs() {
		return grossweight_lbs;
	}

	public void setGrossweight_lbs(Float grossweight_lbs) {
		this.grossweight_lbs = grossweight_lbs;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}
	public void setEncryptdatetime(Date encryptdatetime) {
		this.encryptdatetime = encryptdatetime;
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
	public void setSpaceepc_link(String spaceepc_link) {
		this.spaceepc_link = spaceepc_link;
	}

	public Float getMetorigin() {
		return metorigin;
	}

	public void setMetorigin(Float metorigin) {
		this.metorigin = metorigin;
	}

	public Float getMetcheck() {
		return metcheck;
	}

	public void setMetcheck(Float metcheck) {
		this.metcheck = metcheck;
	}

	public Float getWidth_yds_check() {
		return width_yds_check;
	}

	public void setWidth_yds_check(Float width_yds_check) {
		this.width_yds_check = width_yds_check;
	}

	public Float getWidth_yds() {
		return width_yds;
	}

	public void setWidth_yds(Float width_yds) {
		this.width_yds = width_yds;
	}

	public Float getWidth_met_check() {
		return width_met_check;
	}

	public void setWidth_met_check(Float width_met_check) {
		this.width_met_check = width_met_check;
	}

	public Float getWidth_met() {
		return width_met;
	}

	public void setWidth_met(Float width_met) {
		this.width_met = width_met;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSpaceString() {
		return spaceString;
	}

	public void setSpaceString(String spaceString) {
		this.spaceString = spaceString;
	}

	public Integer getWarehouseStatus() {
		return warehouseStatus;
	}

	public void setWarehouseStatus(Integer warehouseStatus) {
		this.warehouseStatus = warehouseStatus;
	}

	public String getWarehouseStatusString() {
		return warehouseStatusString;
	}

	public void setWarehouseStatusString(String warehouseStatusString) {
		this.warehouseStatusString = warehouseStatusString;
	}

	public Float getWarehouse_check_met_check() {
		return warehouse_check_met_check;
	}

	public void setWarehouse_check_met_check(Float warehouse_check_met_check) {
		this.warehouse_check_met_check = warehouse_check_met_check;
	}

	public Float getWarehouse_check_width_check() {
		return warehouse_check_width_check;
	}

	public void setWarehouse_check_width_check(Float warehouse_check_width_check) {
		this.warehouse_check_width_check = warehouse_check_width_check;
	}

	public Float getWarehouse_check_met_err() {
		return warehouse_check_met_err;
	}

	public void setWarehouse_check_met_err(Float warehouse_check_met_err) {
		this.warehouse_check_met_err = warehouse_check_met_err;
	}

	public Date getDate_check() {
		return date_check;
	}

	public void setDate_check(Date date_check) {
		this.date_check = date_check;
	}

	public Boolean getIsInStock() {
		return isInStock;
	}

	public void setIsInStock(Boolean isInStock) {
		this.isInStock = isInStock;
	}
}
