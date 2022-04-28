package vn.gpay.jitin.core.stockin;

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
import vn.gpay.jitin.core.invoice.Invoice;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.pcontract.PContract;
import vn.gpay.jitin.core.porder.POrder;
import vn.gpay.jitin.core.security.GpayUser;
import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.stockin_type.StockinType;

@Table(name="Stockin")
@Entity
public class StockIn implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockin_generator")
	@SequenceGenerator(name="stockin_generator", sequenceName = "stock_in_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name = "stockincode",length=50)
    private String stockincode;
	
	//public String getStockindate(){return "20 Feb 2019 10:12:22";}
	@Column(name = "stockindate")
	private Date stockindate;
	
	@Column(name = "stockintypeid_link")
    private Integer stockintypeid_link;
	
	private Long porderid_link;
	
	private String pordercode;
	
	@Column(name = "p_skuid_link")
    private Long p_skuid_link;
	
	@Column(name = "orgid_from_link")
    private Long orgid_from_link;
	
	@Column(name = "orgid_to_link")
    private Long orgid_to_link;
	
	@Column(name = "stockoutid_link")
    private Long stockoutid_link;	
	
	@Column(name = "stockout_code")
	private String stockout_code;
	
	//@Column(name = "invoiceid_link")
   // private Long invoiceid_link;	
	
    private String invoice_number;
	
	private String invoice_paymentype;
	
	private Date invoice_date;
	
	private Integer invoice_paymentdue;
	
	private Long vat_typeid_link;
	
	private String vat_sample;
	
	private String vat_symbol;
	
	private String vat_number;
	
	private Date vat_date;
	
	private Long vat_currencyid_link;
	
	private Float vat_exchangerate;
	
	private Date vat_paymentduedate;
			
    private String customs_number;
	
	private Date customs_date;
	
	private String contract_number;
	
	@Column(name = "shipperson",length =100)
    private String shipperson;
	
	@Column(name = "totalpackage")
    private Integer totalpackage; 
	
	@Column(name ="totalm3")
    private Double totalm3;
	
	@Column(name ="totalnetweight")
    private Double totalnetweight;
	
	@Column(name ="totalgrossweight")
    private Double totalgrossweight;	
	
	private String reason;
	
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
	
	private Long material_invoiceid_link;
	
	private Long approverid_link;
	
	private Date approve_date;
	
	private Long unitid_link;
	private Long width_unitid_link;
	
	private Long pcontract_poid_link;
	private Long pcontractid_link;
	
	private Date expected_date;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(cascade =  CascadeType.ALL)
	//@BatchSize(size=10)
	@JoinColumn( name="stockinid_link", referencedColumnName="id")
	private List<StockInD>  stockin_d  = new ArrayList<StockInD>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(cascade =  CascadeType.ALL)
	//@BatchSize(size=10)
	@JoinColumn( name="stockinid_link", referencedColumnName="id")
	private List<StockinProduct>  stockin_product  = new ArrayList<StockinProduct>();
	
//	@NotFound(action = NotFoundAction.IGNORE)
//	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
//	//@BatchSize(size=10)
//	@JoinColumn( name="stockinid_link", referencedColumnName="id")
//	private List<StockinLot>  stockin_lot  = new ArrayList<StockinLot>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="stockintypeid_link",insertable=false,updatable =false)
    private StockinType type;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private POrder porder;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontractid_link",insertable=false,updatable =false)
    private PContract pcontract;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="p_skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_from_link",insertable=false,updatable =false)
    private Org org ;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_to_link",insertable=false,updatable =false)
    private Org org_to ;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="usercreateid_link", referencedColumnName="id",insertable=false,updatable =false)
	private GpayUser  user;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="approverid_link", referencedColumnName="id",insertable=false,updatable =false)
	private GpayUser  user_approve;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn( name="material_invoiceid_link", referencedColumnName="id",insertable=false,updatable =false)
	private Invoice  material_invoice;
	
	// sl yêu cầu của stockin (tổng totalpackage của danh sách stockin_d)
		@Transient
		public Integer getSlYeuCau() {
			Integer result = 0;
			if(stockin_d != null) {
				for(StockInD item : stockin_d) {
					if(item.getTotalpackage() != null) {
						result += item.getTotalpackage();
					}
				}
			}
			return result;
		}
	
	//stockin_product
	@Transient
	public String getStockinProductString() {
		String result = "";
		if(stockin_product != null)
			for(StockinProduct stockinProduct : stockin_product) {
				if(!stockinProduct.getProduct_code().equals("")) {
					if(result.equals("")) {
						result+=stockinProduct.getProduct_code();
					}else {
						result+="; " + stockinProduct.getProduct_code();
					}
				}
			}
		return result;
	}
	
	@Transient
	public String getPcontractcode() {
		if(pcontract != null)
			return pcontract.getContractcode();
		return "";
	}
	
	@Transient
	public String getUnitName() {
		if(unit != null)
			return unit.getName();
		return "";
	}
	
	@Transient
	public String getUsercreate_name() {
		if(user != null)
			return user.getFullname();
		return "";
	}
	
	@Transient
	public String getUserApprove_name() {
		if(user_approve != null)
			return user_approve.getFullname();
		return "";
	}
	
	@Transient
	public String getOrdercode() {
		if(porder != null)
			return porder.getOrdercode();
		return "";
	}
	
	@Transient
	public String getStockintype_name() {
		if(type!=null) {
			return type.getName();
		}
		return "";
	}
	
	@Transient
	public Long getMaterialinvoice_pcontractid() {
		if(material_invoice!=null) {
			return material_invoice.getPcontractid_link();
		}
		return null;
	}
	
	@Transient
	public String getMaterialinvoice_code() {
		if(material_invoice!=null) {
			return material_invoice.getInvoicenumber();
		}
		return "";
	}
	
	@Transient
	public Date getMaterialinvoice_date() {
		if(material_invoice!=null) {
			return material_invoice.getInvoicedate();
		}
		return null;
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
		if(org!=null) {
			return org.getName();
		}
		return "";
	}
	@Transient
	public String getOrgfrom_name_parent() {
		if(org!=null) {
			if(org.getParentname() != null) {
				return org.getName() + " (" + org.getParentname() + ")";
			}
			return org.getName();
		}
		return "";
	}
	@Transient
	public String getOrgfrom_code() {
		if(org!=null) {
			return org.getCode();
		}
		return "";
	}
	
	@Transient
	public String getOrgto_name() {
		if(org_to!=null) {
			return org_to.getName();
		}
		return "";
	}
	@Transient
	public String getOrgto_name_parent() {
		if(org_to!=null) {
			if(org_to.getParentname() != null) {
				return org_to.getName() + " (" + org_to.getParentname() + ")";
			}
			return org_to.getName();
		}
		return "";
	}
	
	@Transient
	public String getStatusString() {
		if(status != null) {
			switch(status) {
			case -1:
				return "Chưa kiểm tra";
			case 0:
				return "Đang kiểm tra";
			case 1:
				return "Đã duyệt";
			case 2:
				return "Đã đồng bộ";
			case -2:
				return "Đã xóa";
			}
		}
		return "";
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
	public String getStockincode() {
		return stockincode;
	}
	public void setStockincode(String stockincode) {
		this.stockincode = stockincode;
	}
	public Date getStockindate() {
		return stockindate;
	}
	public void setStockindate(Date stockindate) {
		this.stockindate = stockindate;
	}
	public Integer getStockintypeid_link() {
		return stockintypeid_link;
	}
	public void setStockintypeid_link(Integer stockintypeid_link) {
		this.stockintypeid_link = stockintypeid_link;
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
	public Long getStockoutid_link() {
		return stockoutid_link;
	}
	public void setStockoutid_link(Long stockoutid_link) {
		this.stockoutid_link = stockoutid_link;
	}
	public String getCustoms_number() {
		return customs_number;
	}
	public void setCustoms_number(String customs_number) {
		this.customs_number = customs_number;
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
	public List<StockInD> getStockin_d() {
		return stockin_d;
	}
	public void setStockin_d(List<StockInD> stockind) {
		this.stockin_d = stockind;
	}
	
	public void setSku(SKU sku) {
		this.sku = sku;
	}
	public Org getOrg() {
		return org;
	}
	public void setOrg(Org org) {
		this.org = org;
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
	public String getInvoice_number() {
		return invoice_number;
	}
	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}
	public String getInvoice_paymentype() {
		return invoice_paymentype;
	}
	public void setInvoice_paymentype(String invoice_paymentype) {
		this.invoice_paymentype = invoice_paymentype;
	}
	public Date getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}
	public Integer getInvoice_paymentdue() {
		return invoice_paymentdue;
	}
	public void setInvoice_paymentdue(Integer invoice_paymentdue) {
		this.invoice_paymentdue = invoice_paymentdue;
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
	public Date getVat_paymentduedate() {
		return vat_paymentduedate;
	}
	public void setVat_paymentduedate(Date vat_paymentduedate) {
		this.vat_paymentduedate = vat_paymentduedate;
	}
	public Date getCustoms_date() {
		return customs_date;
	}
	public void setCustoms_date(Date customs_date) {
		this.customs_date = customs_date;
	}
	public String getContract_number() {
		return contract_number;
	}
	public void setContract_number(String contract_number) {
		this.contract_number = contract_number;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getVat_date() {
		return vat_date;
	}
	public void setVat_date(Date vat_date) {
		this.vat_date = vat_date;
	}

	public Long getMaterial_invoiceid_link() {
		return material_invoiceid_link;
	}

	public void setMaterial_invoiceid_link(Long material_invoiceid_link) {
		this.material_invoiceid_link = material_invoiceid_link;
	}

	public Long getApproverid_link() {
		return approverid_link;
	}

	public void setApproverid_link(Long approverid_link) {
		this.approverid_link = approverid_link;
	}

	public Date getApprove_date() {
		return approve_date;
	}

	public void setApprove_date(Date approve_date) {
		this.approve_date = approve_date;
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

	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}

	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}

	public String getStockout_code() {
		return stockout_code;
	}

	public void setStockout_code(String stockout_code) {
		this.stockout_code = stockout_code;
	}

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	
	public Date getExpected_date() {
		return expected_date;
	}

	public void setExpected_date(Date expected_date) {
		this.expected_date = expected_date;
	}

	public List<StockinProduct> getStockin_product() {
		return stockin_product;
	}

	public void setStockin_product(List<StockinProduct> stockin_product) {
		this.stockin_product = stockin_product;
	}
}
