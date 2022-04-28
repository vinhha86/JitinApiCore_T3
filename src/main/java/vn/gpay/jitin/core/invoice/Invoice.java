package vn.gpay.jitin.core.invoice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import vn.gpay.jitin.core.category.Port;
import vn.gpay.jitin.core.org.Org;
import vn.gpay.jitin.core.pcontract.PContract;
import vn.gpay.jitin.core.sku.SKU;

@Table(name = "invoice")
@Entity
public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_generator")
	@SequenceGenerator(name = "invoice_generator", sequenceName = "invoice_id_seq", allocationSize = 1)
	protected Long id;
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;

	@Column(name = "orgid_link")
	private Long orgid_link;
	
	@Column(name = "custom_declaration", length = 50)
	private String custom_declaration;

	@Column(name = "invoicenumber", length = 50)
	private String invoicenumber;

	@Column(name = "invoicedate")
	private Date invoicedate;

	@Column(name = "orgid_from_link")
	private Long orgid_from_link;

	@Column(name = "orgid_to_link")
	private Long orgid_to_link;

	@Column(name = "port_from_link")
	private Long port_from_link;

	@Column(name = "port_to_link")
	private Long port_to_link;

	@Column(name = "shipdatefrom")
	private Date shipdatefrom;

	@Column(name = "shipdateto")
	private Date shipdateto;

	@Column(name = "customsid_link")
	private Long customsid_link;

	@Column(name = "customsnumber", length = 50)
	private String customsnumber;

	@Column(name = "totalpackage")
	private Integer totalpackage;

	@Column(name = "totalm3")
	private Float totalm3;

	@Column(name = "totalnetweight")
	private Float totalnetweight;

	@Column(name = "totalgrossweight")
	private Float totalgrossweight;

	@Column(name = "p_skuid_link")
	private Long p_skuid_link;

	@Column(name = "shippersson")
	private String shippersson;

	@Column(name = "extrainfo", length = 200)
	private String extrainfo;

	@Column(name = "status")
	private Integer status;

	private Long pcontractid_link;
	private String pcontractcode;

	@Column(name = "usercreateid_link")
	private Long usercreateid_link;

	@Column(name = "timecreate")
	private Date timecreate;

	@Column(name = "lastuserupdateid_link")
	private Long lastuserupdateid_link;

	@Column(name = "lasttimeupdate")
	private Date lasttimeupdate;

	@Column(name = "org_prodviderid_link")
	private Long org_prodviderid_link;
	
	@Column(name = "unitid_link")
	private Long unitid_link;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="pcontractid_link",insertable=false,updatable =false)
	private PContract pcontract;
	
	@Transient
	public Date getContractdate() {
		if (pcontract != null) {
			if (pcontract.getContractdate() != null) {
				return pcontract.getContractdate();
			}
		}
		return null;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="org_prodviderid_link",insertable=false,updatable =false)
	private Org org_prodvider;
	
	@Transient
	public String getOrgProviderName() {
		if (org_prodvider != null) {
			if (org_prodvider.getName() != null) {
				return org_prodvider.getName();
			}
		}
		return "";
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "invoiceid_link", referencedColumnName = "id")
	private List<InvoiceD> invoice_d = new ArrayList<>();
	
	@Transient
	public Integer getInvoiceDTotalPackage() {
		Integer totalpackage = 0;
		for(InvoiceD invoiceD : invoice_d) {
			if(invoiceD.getTotalpackage() != null) {
				totalpackage+=invoiceD.getTotalpackage();
			}
		}
		return totalpackage;
	}

	@Transient
	public String getProductcode() {
		if (sku != null) {
			return sku.getCode();
		}
		return "";
	}

	@Transient
	public String getProductname() {
		if (sku != null) {
			return sku.getName();
		}
		return "";
	}

	@Transient
	public String getHscode() {
		if (sku != null) {
			return sku.getHscode();
		}
		return "";
	}

	@Transient
	public String getHsname() {
		if (sku != null) {
			return sku.getHsname();
		}
		return "";
	}

	@Transient
	public String getOrgfrom_name() {
		if (orgfrom != null) {
			return orgfrom.getName();
		}
		return "";
	}

	@Transient
	public String getOrgfrom_code() {
		if (orgfrom != null) {
			return orgfrom.getCode();
		}
		return "";
	}

	@Transient
	public String getOrgto_name() {
		if (orgto != null) {
			return orgto.getName();
		}
		return "";
	}

	@Transient
	public String getOrgto_code() {
		if (orgto != null) {
			return orgto.getCode();
		}
		return "";
	}

	@Transient
	public String getPortfrom_name() {
		if (portfrom != null) {
			return portfrom.getName();
		}
		return "";
	}

	@Transient
	public String getPortfrom_code() {
		if (portfrom != null) {
			return portfrom.getCode();
		}
		return "";
	}

	@Transient
	public String getPortto_name() {
		if (portto != null) {
			return portto.getName();
		}
		return "";
	}

	@Transient
	public String getPortto_code() {
		if (portto != null) {
			return portto.getCode();
		}
		return "";
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "p_skuid_link", insertable = false, updatable = false)
	private SKU sku;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "orgid_from_link", insertable = false, updatable = false)
	private Org orgfrom;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "orgid_to_link", insertable = false, updatable = false)
	private Org orgto;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "port_from_link", insertable = false, updatable = false)
	private Port portfrom;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "port_to_link", insertable = false, updatable = false)
	private Port portto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public String getInvoicenumber() {
		return invoicenumber;
	}

	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public Date getInvoicedate() {
		return invoicedate;
	}

	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
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

	public Long getPort_from_link() {
		return port_from_link;
	}

	public void setPort_from_link(Long port_from_link) {
		this.port_from_link = port_from_link;
	}

	public Long getPort_to_link() {
		return port_to_link;
	}

	public void setPort_to_link(Long port_to_link) {
		this.port_to_link = port_to_link;
	}

	public Date getShipdatefrom() {
		return shipdatefrom;
	}

	public void setShipdatefrom(Date shipdatefrom) {
		this.shipdatefrom = shipdatefrom;
	}

	public Date getShipdateto() {
		return shipdateto;
	}

	public void setShipdateto(Date shipdateto) {
		this.shipdateto = shipdateto;
	}

	public Long getCustomsid_link() {
		return customsid_link;
	}

	public void setCustomsid_link(Long customsid_link) {
		this.customsid_link = customsid_link;
	}

	public String getCustomsnumber() {
		return customsnumber;
	}

	public void setCustomsnumber(String customsnumber) {
		this.customsnumber = customsnumber;
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

	public String getShippersson() {
		return shippersson;
	}

	public void setShippersson(String shippersson) {
		this.shippersson = shippersson;
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

	public List<InvoiceD> getInvoice_d() {
		return invoice_d;
	}

	public void setInvoice_d(List<InvoiceD> invoice_d) {
		this.invoice_d = invoice_d;
	}

	public SKU getSku() {
		return sku;
	}

	public void setSku(SKU sku) {
		this.sku = sku;
	}

	public Org getOrgfrom() {
		return orgfrom;
	}

	public void setOrgfrom(Org orgfrom) {
		this.orgfrom = orgfrom;
	}

	public Org getOrgto() {
		return orgto;
	}

	public void setOrgto(Org orgto) {
		this.orgto = orgto;
	}

	public Port getPortfrom() {
		return portfrom;
	}

	public void setPortfrom(Port portfrom) {
		this.portfrom = portfrom;
	}

	public Port getPortto() {
		return portto;
	}

	public void setPortto(Port portto) {
		this.portto = portto;
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

	public Long getOrg_prodviderid_link() {
		return org_prodviderid_link;
	}

	public void setOrg_prodviderid_link(Long org_prodviderid_link) {
		this.org_prodviderid_link = org_prodviderid_link;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public String getCustom_declaration() {
		return custom_declaration;
	}

	public void setCustom_declaration(String custom_declaration) {
		this.custom_declaration = custom_declaration;
	}

	public Long getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}

}
