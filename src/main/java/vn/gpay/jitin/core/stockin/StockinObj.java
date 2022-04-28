package vn.gpay.jitin.core.stockin;

import java.util.Date;

public class StockinObj {
	private Long id;
	private String invoice_number;
	private Date invoice_date;
	private String stockincode;
	private String stockintype_name;
	private Integer stockintypeid_link;
	private String orgfrom_name;
	private Long orgid_from_link;
	private String orgto_name;
	private Long orgid_to_link;
	private Long stockoutid_link;
	private String stockout_code;
	private Integer totalpackage;
	private Double totalm3;
	private Double totalnetweight;
    private Double totalgrossweight;
    private Integer status;
    private String statusString;
	private Long usercreateid_link;
	private String usercreate_name;
	private Date timecreate;
	private Date stockindate;
	private String stockinProductString;
	private String reason;
	private Long unitid_link;
	private Long width_unitid_link;
	private Long pcontractid_link;
	private String pcontractcode;
	private Date expected_date;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInvoice_number() {
		return invoice_number;
	}
	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}
	public Date getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}
	public String getStockincode() {
		return stockincode;
	}
	public void setStockincode(String stockincode) {
		this.stockincode = stockincode;
	}
	public String getStockintype_name() {
		return stockintype_name;
	}
	public void setStockintype_name(String stockintype_name) {
		this.stockintype_name = stockintype_name;
	}
	public Integer getStockintypeid_link() {
		return stockintypeid_link;
	}
	public void setStockintypeid_link(Integer stockintypeid_link) {
		this.stockintypeid_link = stockintypeid_link;
	}
	public String getOrgfrom_name() {
		return orgfrom_name;
	}
	public void setOrgfrom_name(String orgfrom_name) {
		this.orgfrom_name = orgfrom_name;
	}
	public Long getOrgid_from_link() {
		return orgid_from_link;
	}
	public void setOrgid_from_link(Long orgid_from_link) {
		this.orgid_from_link = orgid_from_link;
	}
	public String getOrgto_name() {
		return orgto_name;
	}
	public void setOrgto_name(String orgto_name) {
		this.orgto_name = orgto_name;
	}
	public Long getOrgid_to_link() {
		return orgid_to_link;
	}
	public void setOrgid_to_link(Long orgid_to_link) {
		this.orgid_to_link = orgid_to_link;
	}
	public Long getStockoutid_link() {
		return stockoutid_link;
	}
	public void setStockoutid_link(Long stockoutid_link) {
		this.stockoutid_link = stockoutid_link;
	}
	public String getStockout_code() {
		return stockout_code;
	}
	public void setStockout_code(String stockout_code) {
		this.stockout_code = stockout_code;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public Double getTotalm3() {
		return totalm3;
	}
	public void setTotalm3(Double totalm3) {
		this.totalm3 = totalm3;
	}
	public Double getTotalnetweight() {
		return totalnetweight;
	}
	public void setTotalnetweight(Double totalnetweight) {
		this.totalnetweight = totalnetweight;
	}
	public Double getTotalgrossweight() {
		return totalgrossweight;
	}
	public void setTotalgrossweight(Double totalgrossweight) {
		this.totalgrossweight = totalgrossweight;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusString() {
		return statusString;
	}
	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}
	public Long getUsercreateid_link() {
		return usercreateid_link;
	}
	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}
	public String getUsercreate_name() {
		return usercreate_name;
	}
	public void setUsercreate_name(String usercreate_name) {
		this.usercreate_name = usercreate_name;
	}
	public Date getTimecreate() {
		return timecreate;
	}
	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}
	public Date getStockindate() {
		return stockindate;
	}
	public void setStockindate(Date stockindate) {
		this.stockindate = stockindate;
	}
	public String getStockinProductString() {
		return stockinProductString;
	}
	public void setStockinProductString(String stockinProductString) {
		this.stockinProductString = stockinProductString;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}
	public Long getWidth_unitid_link() {
		return width_unitid_link;
	}
	public void setWidth_unitid_link(Long width_unitid_link) {
		this.width_unitid_link = width_unitid_link;
	}
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public String getPcontractcode() {
		return pcontractcode;
	}
	public void setPcontractcode(String pcontractcode) {
		this.pcontractcode = pcontractcode;
	}
	public Date getExpected_date() {
		return expected_date;
	}
	public void setExpected_date(Date expected_date) {
		this.expected_date = expected_date;
	}
	
}
