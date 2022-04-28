package vn.gpay.jitin.core.stockout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.porder.POrder;
import vn.gpay.jitin.core.product.Product;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.stockout_order.Stockout_order;
import vn.gpay.jitin.core.stockout_type.StockoutType;

@Table(name="stockout")
@Entity
public class StockOut implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_generator")
	@SequenceGenerator(name="stockout_generator", sequenceName = "stock_out_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name ="stockoutorderid_link")
    private Long stockoutorderid_link;	
	
	@Column(name = "stockoutcode",length=50)
    private String stockoutcode;
	
	//public String getStockindate(){return "20 Feb 2019 10:12:22";}
	@Column(name = "stockoutdate")
	private Date stockoutdate;
	
	@Column(name = "stockouttypeid_link")
    private Integer stockouttypeid_link;
	
	@Column(name = "orgid_from_link")
    private Long orgid_from_link;
	
	@Column(name = "orgid_to_link")
    private Long orgid_to_link;
	
	@Column(name ="porderid_link")
    private Long porderid_link;	
	
	@Column(name ="pordercode", length=10)
    private String pordercode;	
	
	@Column(name = "shipperson",length =100)
    private String shipperson;
	
	private String reason;
	private String contract_number;
	private String invoice_number;
	private Date invoice_date;
	private Long vat_typeid_link;
	private String vat_sample;
	private String vat_symbol;
	private String vat_number;
	private Date vat_date;
	private Long vat_currencyid_link;
	private Float vat_exchangerate;
	private Long pcontract_poid_link;
	private Long pcontractid_link;
	private Long pcontract_productid_link;
	private Long productid_link;
	
	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}

	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}

	@Column(name = "totalpackage")
    private Integer totalpackage; 
	
	@Column(name ="totalyds")
    private Float totalyds;
	
	@Transient
    private Double total_product;
	@Transient
    private Double total_mainmaterial;
	@Transient
    private Double total_mainmaterial_check;
	@Transient
    private Double total_mainmaterial_processed;
	@Transient
    private Double total_mainmaterial_stockout;	
	@Transient
    private Double total_liningmaterial;
	@Transient
    private Double total_liningmaterial_check;
	@Transient
    private Double total_liningmaterial_processed;
	@Transient
    private Double total_liningmaterial_stockout;		
	@Transient
    private Double total_mex;
	@Transient
    private Double total_mex_check;
	@Transient
    private Double total_mex_processed;
	@Transient
    private Double total_mex_stockout;	
	@Transient
    private Double total_mixmaterial;
	@Transient
    private Double total_mixmaterial_check;
	@Transient
    private Double total_mixmaterial_processed;
	@Transient
    private Double total_mixmaterial_stockout;
	
	@Column(name ="totalpackagecheck")
    private Integer totalpackagecheck;
	
	@Column(name ="totalydscheck")
    private Float totalydscheck;
	
	@Column(name ="totalpackageprocessed")
    private Integer totalpackageprocessed;
	
	@Column(name ="totalydsprocessed")
    private Float totalydsprocessed;		
	
	@Column(name ="totalm3")
    private Double totalm3;
	
	@Column(name ="totalnetweight")
    private Double totalnetweight;
	
	@Column(name ="totalgrossweight")
    private Double totalgrossweight;	
	
	@Column(name ="totalprice")
    private Float totalprice;	
	
	@Column(name = "p_skuid_link")
    private Long p_skuid_link;
	
	@Column(name ="p_skucode")
    private String p_skucode;	
	
	@Column(name ="extrainfo",length=200)
    private String extrainfo;	
	
	@Column(name = "status")
    private Integer status;

	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	@Column(name ="stockout_userid_link")
	private Long stockout_userid_link; 
	@Column(name ="approver_userid_link")
	private Long approver_userid_link; 
	@Column(name ="receiver_userid_link")
	private Long receiver_userid_link;
	
	@Column(name ="approve_date")
	private Date approve_date;
	@Column(name ="receive_date")
	private Date receive_date;
	
	@Column(name ="unitid_link")
	private Long unitid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="productid_link", referencedColumnName="id",insertable=false,updatable =false)
	private Product product;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL)
    @JoinColumn(name="stockoutid_link",referencedColumnName="id")
	private List<StockOutD>  stockout_d  = new ArrayList<>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="stockouttypeid_link", referencedColumnName="id",insertable=false,updatable =false)
	private StockoutType  stockout_type;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="usercreateid_link", referencedColumnName="id",insertable=false,updatable =false)
	private GpayUser  user;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="orgid_from_link", referencedColumnName="id",insertable=false,updatable =false)
	private Org from;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="orgid_to_link", referencedColumnName="id",insertable=false,updatable =false)
	private Org to;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private POrder porder;
	
	@Transient
	public String getProduct_buyercode() {
		if(product != null)
			return product.getBuyercode();
		return "";
	}
	
	@Transient
	public String getPorder_product_buyercode() {
		if(porder != null)
			return porder.getProduct_buyercode();
		return "";
	}
	
	@Transient
	public String getUnit_name() {
		if(unit != null)
			return unit.getName();
		return "";
	}
	
	@Transient
	public String getOrg_from_name() {
		if(from != null)
			return from.getName();
		return "";
	}
	
	@Transient
	public  String getOrg_to_name() {
		if(to != null)
			return to.getName();
		return "";
	}
	
	@Transient
	public String getUsercreate_name() {
		if(user != null)
			return user.getFullname();
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="approver_userid_link", referencedColumnName="id",insertable=false,updatable =false)
	private GpayUser  user_approve;
	
	@Transient
	public String getUserApprove_name() {
		if(user_approve != null)
			return user_approve.getFullname();
		return "";
	}
	
	
	@Transient 
	public String getStockouttype_name() {
		if (stockout_type != null) {
			return stockout_type.getName();
		}
		return "";
	}
	@Transient
	public String getProductcode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
	}
	@Transient
	public String getProductname() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	@Transient
	public String getHscode() {
		if(sku!=null) {
			return sku.getHscode();
		}
		return "";
	}
	@Transient
	public String getHsname() {
		if(sku!=null) {
			return sku.getHsname();
		}
		return "";
	}
	
	@Transient
	public String getOrgfrom_name() {
		if(orgFrom!=null) {
			return orgFrom.getName();
		}
		return "";
	}
	@Transient
	public String getOrgfrom_code() {
		if(orgFrom!=null) {
			return orgFrom.getCode();
		}
		return "";
	}
	
	@Transient
	public String getOrgto_name() {
		if(orgTo!=null) {
			return orgTo.getName();
		}
		return "";
	}
	@Transient
	public String getOrgto_code() {
		if(orgTo!=null) {
			return orgTo.getCode();
		}
		return "";
	}
	
	@Transient
	public String getStockout_order_code() {
		if(stockout_order!=null) {
			return stockout_order.getStockout_order_code();
		}
		return "";
	}
	
	@Transient
	public String getStatusString() {
		if(status != null) {
			switch(status) {
			case -3:
				return "Đã xóa";
			case -2:
				return "Chưa nhặt hàng";
			case -1:
				return "Đang nhặt hàng";
			case 0:
				return "Đang kiểm tra";
			case 1:
				return "Đã duyệt";
			case 2:
				return "Đã nhận hàng";
			}
		}
		return "";
	}
	
//	@Transient
//	public Date getStockout_order_date() {
//		if(stockout_order!=null) {
//			return stockout_order.getTimecreate();
//		}
//		return null;
//	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="stockoutorderid_link",insertable=false,updatable =false)
    private Stockout_order stockout_order;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="p_skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_from_link",insertable=false,updatable =false)
    private Org orgFrom ;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_to_link",insertable=false,updatable =false)
    private Org orgTo ;

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
	public Long getStockoutorderid_link() {
		return stockoutorderid_link;
	}
	public void setStockoutorderid_link(Long stockoutorderid_link) {
		this.stockoutorderid_link = stockoutorderid_link;
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
	public Long getOrgid_from_link() {
		return orgid_from_link;
	}
	public void setOrgid_from_link(Long orgid_from_link) {
		this.orgid_from_link = orgid_from_link;
	}
	public Long getOrgid_to_link() {
		return orgid_to_link;
	}
	public void setOrgid_to_link(Long orgid_to_link) {
		this.orgid_to_link = orgid_to_link;
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
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public Float getTotalyds() {
		return totalyds;
	}
	public void setTotalyds(Float totalyds) {
		this.totalyds = totalyds;
	}
	public Double getTotal_product() {
		return total_product;
	}
	public void setTotal_product(Double total_product) {
		this.total_product = total_product;
	}
	public Double getTotal_mainmaterial() {
		return total_mainmaterial;
	}
	public void setTotal_mainmaterial(Double total_mainmaterial) {
		this.total_mainmaterial = total_mainmaterial;
	}
	public Double getTotal_mainmaterial_check() {
		return total_mainmaterial_check;
	}
	public void setTotal_mainmaterial_check(Double total_mainmaterial_check) {
		this.total_mainmaterial_check = total_mainmaterial_check;
	}
	public Double getTotal_mainmaterial_processed() {
		return total_mainmaterial_processed;
	}
	public void setTotal_mainmaterial_processed(Double total_mainmaterial_processed) {
		this.total_mainmaterial_processed = total_mainmaterial_processed;
	}
	public Double getTotal_mainmaterial_stockout() {
		return total_mainmaterial_stockout;
	}
	public void setTotal_mainmaterial_stockout(Double total_mainmaterial_stockout) {
		this.total_mainmaterial_stockout = total_mainmaterial_stockout;
	}
	public Double getTotal_liningmaterial() {
		return total_liningmaterial;
	}
	public void setTotal_liningmaterial(Double total_liningmaterial) {
		this.total_liningmaterial = total_liningmaterial;
	}
	public Double getTotal_liningmaterial_check() {
		return total_liningmaterial_check;
	}
	public void setTotal_liningmaterial_check(Double total_liningmaterial_check) {
		this.total_liningmaterial_check = total_liningmaterial_check;
	}
	public Double getTotal_liningmaterial_processed() {
		return total_liningmaterial_processed;
	}
	public void setTotal_liningmaterial_processed(Double total_liningmaterial_processed) {
		this.total_liningmaterial_processed = total_liningmaterial_processed;
	}
	public Double getTotal_liningmaterial_stockout() {
		return total_liningmaterial_stockout;
	}
	public void setTotal_liningmaterial_stockout(Double total_liningmaterial_stockout) {
		this.total_liningmaterial_stockout = total_liningmaterial_stockout;
	}
	public Double getTotal_mex() {
		return total_mex;
	}
	public void setTotal_mex(Double total_mex) {
		this.total_mex = total_mex;
	}
	public Double getTotal_mex_check() {
		return total_mex_check;
	}
	public void setTotal_mex_check(Double total_mex_check) {
		this.total_mex_check = total_mex_check;
	}
	public Double getTotal_mex_processed() {
		return total_mex_processed;
	}
	public void setTotal_mex_processed(Double total_mex_processed) {
		this.total_mex_processed = total_mex_processed;
	}
	public Double getTotal_mex_stockout() {
		return total_mex_stockout;
	}
	public void setTotal_mex_stockout(Double total_mex_stockout) {
		this.total_mex_stockout = total_mex_stockout;
	}
	public Double getTotal_mixmaterial() {
		return total_mixmaterial;
	}
	public void setTotal_mixmaterial(Double total_mixmaterial) {
		this.total_mixmaterial = total_mixmaterial;
	}
	public Double getTotal_mixmaterial_check() {
		return total_mixmaterial_check;
	}
	public void setTotal_mixmaterial_check(Double total_mixmaterial_check) {
		this.total_mixmaterial_check = total_mixmaterial_check;
	}
	public Double getTotal_mixmaterial_processed() {
		return total_mixmaterial_processed;
	}
	public void setTotal_mixmaterial_processed(Double total_mixmaterial_processed) {
		this.total_mixmaterial_processed = total_mixmaterial_processed;
	}
	public Double getTotal_mixmaterial_stockout() {
		return total_mixmaterial_stockout;
	}
	public void setTotal_mixmaterial_stockout(Double total_mixmaterial_stockout) {
		this.total_mixmaterial_stockout = total_mixmaterial_stockout;
	}
	public Integer getTotalpackagecheck() {
		return totalpackagecheck;
	}
	public void setTotalpackagecheck(Integer totalpackagecheck) {
		this.totalpackagecheck = totalpackagecheck;
	}
	public Float getTotalydscheck() {
		return totalydscheck;
	}
	public void setTotalydscheck(Float totalydscheck) {
		this.totalydscheck = totalydscheck;
	}
	public Integer getTotalpackageprocessed() {
		return totalpackageprocessed;
	}
	public void setTotalpackageprocessed(Integer totalpackageprocessed) {
		this.totalpackageprocessed = totalpackageprocessed;
	}
	public Float getTotalydsprocessed() {
		return totalydsprocessed;
	}
	public void setTotalydsprocessed(Float totalydsprocessed) {
		this.totalydsprocessed = totalydsprocessed;
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
	public Float getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(Float totalprice) {
		this.totalprice = totalprice;
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

	public List<StockOutD> getStockout_d() {
		return stockout_d;
	}

	public void setStockout_d(List<StockOutD> stockout_d) {
		this.stockout_d = stockout_d;
	}

	public SKU getSku() {
		return sku;
	}
	public void setSku(SKU sku) {
		this.sku = sku;
	}
	public Org getOrgFrom() {
		return orgFrom;
	}
	public void setOrgFrom(Org orgFrom) {
		this.orgFrom = orgFrom;
	}
	public Org getOrgTo() {
		return orgTo;
	}
	public void setOrgTo(Org orgTo) {
		this.orgTo = orgTo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	public Long getVat_typeid_link() {
		return vat_typeid_link;
	}
	public void setVat_typeid_link(Long vat_typeid_link) {
		this.vat_typeid_link = vat_typeid_link;
	}
	public String getVat_sample() {
		return vat_sample;
	}
	public void setVat_sample(String vat_sample) {
		this.vat_sample = vat_sample;
	}
	public String getVat_symbol() {
		return vat_symbol;
	}
	public void setVat_symbol(String vat_symbol) {
		this.vat_symbol = vat_symbol;
	}
	public String getVat_number() {
		return vat_number;
	}
	public void setVat_number(String vat_number) {
		this.vat_number = vat_number;
	}
	public Date getVat_date() {
		return vat_date;
	}
	public void setVat_date(Date vat_date) {
		this.vat_date = vat_date;
	}
	public Long getVat_currencyid_link() {
		return vat_currencyid_link;
	}
	public void setVat_currencyid_link(Long vat_currencyid_link) {
		this.vat_currencyid_link = vat_currencyid_link;
	}
	public Float getVat_exchangerate() {
		return vat_exchangerate;
	}
	public void setVat_exchangerate(Float vat_exchangerate) {
		this.vat_exchangerate = vat_exchangerate;
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

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}

	public Long getPcontract_productid_link() {
		return pcontract_productid_link;
	}

	public void setPcontract_productid_link(Long pcontract_productid_link) {
		this.pcontract_productid_link = pcontract_productid_link;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	
}
