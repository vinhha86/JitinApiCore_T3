package vn.gpay.jitin.core.stockout;

import java.util.Date;

public class StockOutObj {
	private Long id;
    private Long stockoutorderid_link;	
    private String stockout_order_code;
    private String stockoutcode;
	private Date stockoutdate;
    private Integer stockouttypeid_link;
    private String stockouttype_name;
    private Long orgid_from_link;
    private String org_from_name;
    private String org_from_parent_name;
    private Long orgid_to_link;
    private String org_to_name;
    private String org_to_parent_name;
    private Long porderid_link;	
    private String pordercode;	
    private String shipperson;
    private String reason;
	private String contract_number;
	private String invoice_number;
	private Date invoice_date;
	private Long pcontract_poid_link;
	private Long pcontractid_link;
    private Integer totalpackage; 
    private Integer totalpackagecheck;
    private Float totalyds;
    private Float totalydscheck;
    private Double totalm3;
    private Double totalnetweight;
    private Double totalgrossweight;	
    private Long p_skuid_link;
    private String p_skucode;	
    private String extrainfo;	
    private Integer status;
    private String statusString;
	private Long usercreateid_link;
	private String usercreate_name;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private Long stockout_userid_link; 
	private Long approver_userid_link; 
	private Long receiver_userid_link;
	private Date approve_date;
	private Date receive_date;
	private Long unitid_link;
	private String porder_product_buyercode;
	private String product_buyercode;
	
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
	public String getStockout_order_code() {
		return stockout_order_code;
	}
	public void setStockout_order_code(String stockout_order_code) {
		this.stockout_order_code = stockout_order_code;
	}
	public String getStockoutcode() {
		return stockoutcode;
	}
	public void setStockoutcode(String stockoutcode) {
		this.stockoutcode = stockoutcode;
	}
	public Date getStockoutdate() {
		return stockoutdate;
	}
	public void setStockoutdate(Date stockoutdate) {
		this.stockoutdate = stockoutdate;
	}
	public Integer getStockouttypeid_link() {
		return stockouttypeid_link;
	}
	public void setStockouttypeid_link(Integer stockouttypeid_link) {
		this.stockouttypeid_link = stockouttypeid_link;
	}
	public String getStockouttype_name() {
		return stockouttype_name;
	}
	public void setStockouttype_name(String stockouttype_name) {
		this.stockouttype_name = stockouttype_name;
	}
	public Long getOrgid_from_link() {
		return orgid_from_link;
	}
	public void setOrgid_from_link(Long orgid_from_link) {
		this.orgid_from_link = orgid_from_link;
	}
	public String getOrg_from_name() {
		return org_from_name;
	}
	public void setOrg_from_name(String org_from_name) {
		this.org_from_name = org_from_name;
	}
	public Long getOrgid_to_link() {
		return orgid_to_link;
	}
	public void setOrgid_to_link(Long orgid_to_link) {
		this.orgid_to_link = orgid_to_link;
	}
	public String getOrg_to_name() {
		return org_to_name;
	}
	public void setOrg_to_name(String org_to_name) {
		this.org_to_name = org_to_name;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public String getPordercode() {
		return pordercode;
	}
	public void setPordercode(String pordercode) {
		this.pordercode = pordercode;
	}
	public String getShipperson() {
		return shipperson;
	}
	public void setShipperson(String shipperson) {
		this.shipperson = shipperson;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getContract_number() {
		return contract_number;
	}
	public void setContract_number(String contract_number) {
		this.contract_number = contract_number;
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
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public Integer getTotalpackagecheck() {
		return totalpackagecheck;
	}
	public void setTotalpackagecheck(Integer totalpackagecheck) {
		this.totalpackagecheck = totalpackagecheck;
	}
	public Float getTotalyds() {
		return totalyds;
	}
	public void setTotalyds(Float totalyds) {
		this.totalyds = totalyds;
	}
	public Float getTotalydscheck() {
		return totalydscheck;
	}
	public void setTotalydscheck(Float totalydscheck) {
		this.totalydscheck = totalydscheck;
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
	public Long getP_skuid_link() {
		return p_skuid_link;
	}
	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
	}
	public String getP_skucode() {
		return p_skucode;
	}
	public void setP_skucode(String p_skucode) {
		this.p_skucode = p_skucode;
	}
	public String getExtrainfo() {
		return extrainfo;
	}
	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
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
	public Long getStockout_userid_link() {
		return stockout_userid_link;
	}
	public void setStockout_userid_link(Long stockout_userid_link) {
		this.stockout_userid_link = stockout_userid_link;
	}
	public Long getApprover_userid_link() {
		return approver_userid_link;
	}
	public void setApprover_userid_link(Long approver_userid_link) {
		this.approver_userid_link = approver_userid_link;
	}
	public Long getReceiver_userid_link() {
		return receiver_userid_link;
	}
	public void setReceiver_userid_link(Long receiver_userid_link) {
		this.receiver_userid_link = receiver_userid_link;
	}
	public Date getApprove_date() {
		return approve_date;
	}
	public void setApprove_date(Date approve_date) {
		this.approve_date = approve_date;
	}
	public Date getReceive_date() {
		return receive_date;
	}
	public void setReceive_date(Date receive_date) {
		this.receive_date = receive_date;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}
	public String getPorder_product_buyercode() {
		return porder_product_buyercode;
	}
	public void setPorder_product_buyercode(String porder_product_buyercode) {
		this.porder_product_buyercode = porder_product_buyercode;
	}
	public String getProduct_buyercode() {
		return product_buyercode;
	}
	public void setProduct_buyercode(String product_buyercode) {
		this.product_buyercode = product_buyercode;
	}
	public String getOrg_from_parent_name() {
		return org_from_parent_name;
	}
	public void setOrg_from_parent_name(String org_from_parent_name) {
		this.org_from_parent_name = org_from_parent_name;
	}
	public String getOrg_to_parent_name() {
		return org_to_parent_name;
	}
	public void setOrg_to_parent_name(String org_to_parent_name) {
		this.org_to_parent_name = org_to_parent_name;
	}
	
}
