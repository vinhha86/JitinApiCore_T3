package vn.gpay.jitin.core.stockout_order;

import java.util.Date;

public class Stockout_orderObj {
	private Long id;
	private String ordercode;
	private Date orderdate;
	private Date stockoutdate;
	private Integer stockouttypeid_link;
	private Long orgid_from_link;
	private String org_from_name;
	private Long orgid_to_link;
	private String org_to_name;
	private String orderperson;
	private Integer totalpackage;
	private Float totalm3;
	private Float totalnetweight;
	private Float totalgrossweight;
	private Long p_skuid_link;
	private String extrainfo;
	private Integer status;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private Long porderid_link;
	private String stockout_order_code;
	private Long unitid_link;
	private Long pcontractid_link;
	private Long pcontract_poid_link;
	private String porder_product_buyercode;
	private String porder_code;
	private String typename;
	private Date date_to_vai_yc;
	private Date date_xuat_yc;
	private Long porder_grantid_link;
	private Long porder_Product_id;
	
	// thông tin cho yêu cầu xuất vải (1 yêu càu xuất chỉ có 1 loại vải)
	private Long skuid_link;
	private String skuCode;
	private String skuName;
	private String skuDescription;
	private String skuColor;
	private String skuSize;
	private Integer cayYc;
	private Float metYc;
	private Integer cayTo;
	private Float metTo;
	private Integer cayXuat;
	private Float metXuat;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public Date getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
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
	public String getOrderperson() {
		return orderperson;
	}
	public void setOrderperson(String orderperson) {
		this.orderperson = orderperson;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public Float getTotalm3() {
		return totalm3;
	}
	public void setTotalm3(Float totalm3) {
		this.totalm3 = totalm3;
	}
	public Float getTotalnetweight() {
		return totalnetweight;
	}
	public void setTotalnetweight(Float totalnetweight) {
		this.totalnetweight = totalnetweight;
	}
	public Float getTotalgrossweight() {
		return totalgrossweight;
	}
	public void setTotalgrossweight(Float totalgrossweight) {
		this.totalgrossweight = totalgrossweight;
	}
	public Long getP_skuid_link() {
		return p_skuid_link;
	}
	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
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
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public String getStockout_order_code() {
		return stockout_order_code;
	}
	public void setStockout_order_code(String stockout_order_code) {
		this.stockout_order_code = stockout_order_code;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}
	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}
	public String getPorder_product_buyercode() {
		return porder_product_buyercode;
	}
	public void setPorder_product_buyercode(String porder_product_buyercode) {
		this.porder_product_buyercode = porder_product_buyercode;
	}
	public String getPorder_code() {
		return porder_code;
	}
	public void setPorder_code(String porder_code) {
		this.porder_code = porder_code;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public Date getDate_to_vai_yc() {
		return date_to_vai_yc;
	}
	public void setDate_to_vai_yc(Date date_to_vai_yc) {
		this.date_to_vai_yc = date_to_vai_yc;
	}
	public Date getDate_xuat_yc() {
		return date_xuat_yc;
	}
	public void setDate_xuat_yc(Date date_xuat_yc) {
		this.date_xuat_yc = date_xuat_yc;
	}
	public Long getPorder_grantid_link() {
		return porder_grantid_link;
	}
	public void setPorder_grantid_link(Long porder_grantid_link) {
		this.porder_grantid_link = porder_grantid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getSkuDescription() {
		return skuDescription;
	}
	public void setSkuDescription(String skuDescription) {
		this.skuDescription = skuDescription;
	}
	public String getSkuColor() {
		return skuColor;
	}
	public void setSkuColor(String skuColor) {
		this.skuColor = skuColor;
	}
	public String getSkuSize() {
		return skuSize;
	}
	public void setSkuSize(String skuSize) {
		this.skuSize = skuSize;
	}
	public Integer getCayYc() {
		return cayYc;
	}
	public void setCayYc(Integer cayYc) {
		this.cayYc = cayYc;
	}
	public Float getMetYc() {
		return metYc;
	}
	public void setMetYc(Float metYc) {
		this.metYc = metYc;
	}
	public Integer getCayTo() {
		return cayTo;
	}
	public void setCayTo(Integer cayTo) {
		this.cayTo = cayTo;
	}
	public Float getMetTo() {
		return metTo;
	}
	public void setMetTo(Float metTo) {
		this.metTo = metTo;
	}
	public Integer getCayXuat() {
		return cayXuat;
	}
	public void setCayXuat(Integer cayXuat) {
		this.cayXuat = cayXuat;
	}
	public Float getMetXuat() {
		return metXuat;
	}
	public void setMetXuat(Float metXuat) {
		this.metXuat = metXuat;
	}
	public Long getPorder_Product_id() {
		return porder_Product_id;
	}
	public void setPorder_Product_id(Long porder_Product_id) {
		this.porder_Product_id = porder_Product_id;
	}
	
}
