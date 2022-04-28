package vn.gpay.jitin.core.warehouse_check;

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

import vn.gpay.jitin.core.warehouse.Warehouse;

@Table(name="warehouse_check")
@Entity
public class WarehouseCheck  implements Serializable {
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_check_generator")
	@SequenceGenerator(name="warehouse_check_generator", sequenceName = "warehouse_check_id_seq", allocationSize=1)
	protected Long id;
	
	// stockoutorderid_link, warehouseid_link, yds_origin, yds_check, met_origin, met_check, width_origin, width_check, 
	// netweight_origin, netweight_check, grossweight_origin, grossweight_check, usercheckid_link, date_check
	private Long stockoutorderid_link;
	private Long warehouseid_link;
	private Float yds_origin;
	private Float yds_check;
	private Float met_origin;
	private Float met_check;
	private Float width_origin;
	private Float width_check;
	private Float netweight_origin;
	private Float netweight_check;
	private Float grossweight_origin;
	private Float grossweight_check;
	private Long usercheckid_link;
	private Date date_check;
	private Float met_err;
	
	@Transient
	private String lotnumber;
	@Transient
	private Integer packageid; 
	@Transient
	private String epc; 
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="warehouseid_link ",updatable =false,insertable =false)
	private Warehouse warehouse;
	
//	@Transient
//	public String getEpc() {
//		if(warehouse != null) {
//			return warehouse.getEpc();
//		}
//		return "";
//	}
	
//	@Transient
//	public String getLotnumber() {
//		if(warehouse != null) {
//			return warehouse.getLotnumber();
//		}
//		return "";
//	}
//	
//	@Transient
//	public Integer getPackageid() {
//		if(warehouse != null) {
//			return warehouse.getPackageid();
//		}
//		return null;
//	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getStockoutorderid_link() {
		return stockoutorderid_link;
	}
	public void setStockoutorderid_link(Long stockoutorderid_link) {
		this.stockoutorderid_link = stockoutorderid_link;
	}
	public Long getWarehouseid_link() {
		return warehouseid_link;
	}
	public void setWarehouseid_link(Long warehouseid_link) {
		this.warehouseid_link = warehouseid_link;
	}
	public Float getYds_origin() {
		return yds_origin;
	}
	public void setYds_origin(Float yds_origin) {
		this.yds_origin = yds_origin;
	}
	public Float getYds_check() {
		return yds_check;
	}
	public void setYds_check(Float yds_check) {
		this.yds_check = yds_check;
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
	public Float getWidth_origin() {
		return width_origin;
	}
	public void setWidth_origin(Float width_origin) {
		this.width_origin = width_origin;
	}
	public Float getWidth_check() {
		return width_check;
	}
	public void setWidth_check(Float width_check) {
		this.width_check = width_check;
	}
	public Float getNetweight_origin() {
		return netweight_origin;
	}
	public void setNetweight_origin(Float netweight_origin) {
		this.netweight_origin = netweight_origin;
	}
	public Float getNetweight_check() {
		return netweight_check;
	}
	public void setNetweight_check(Float netweight_check) {
		this.netweight_check = netweight_check;
	}
	public Float getGrossweight_origin() {
		return grossweight_origin;
	}
	public void setGrossweight_origin(Float grossweight_origin) {
		this.grossweight_origin = grossweight_origin;
	}
	public Float getGrossweight_check() {
		return grossweight_check;
	}
	public void setGrossweight_check(Float grossweight_check) {
		this.grossweight_check = grossweight_check;
	}
	public Long getUsercheckid_link() {
		return usercheckid_link;
	}
	public void setUsercheckid_link(Long usercheckid_link) {
		this.usercheckid_link = usercheckid_link;
	}
	public Date getDate_check() {
		return date_check;
	}
	public void setDate_check(Date date_check) {
		this.date_check = date_check;
	}

	public Float getMet_err() {
		return met_err;
	}

	public void setMet_err(Float met_err) {
		this.met_err = met_err;
	}

	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}

	public void setPackageid(Integer packageid) {
		this.packageid = packageid;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public String getLotnumber() {
		return lotnumber;
	}
	public Integer getPackageid() {
		return packageid;
	}
}
