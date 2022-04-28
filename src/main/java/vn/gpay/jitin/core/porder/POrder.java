package vn.gpay.jitin.core.porder;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.jitin.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.jitin.core.product.Product;

@Table(name="porders")
@Entity
public class POrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porders_generator")
	@SequenceGenerator(name="porders_generator", sequenceName = "porders_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private String ordercode;
	private Date orderdate;
	private Long productid_link;
	private Integer totalorder;
	private Integer totalcut;
	private Integer totalstocked;
	private Date golivedate;
	private String golivedesc;
	private Integer balance_status;
	private Date balance_date;
	private Float balance_rate;
	private Date productiondate;
	private Integer productionyear;
	private Integer salaryyear;
	private String season;
	private String collection;
	private String comment;
	private Integer status;
	private Long usercreatedid_link;
	private Date timecreated;
	private Integer salarymonth;
	private Long pcontractid_link;
	private Date material_date;
	private Date sample_date;
	private Date cut_date;
	private Date packing_date;
	private Date qc_date;
	private Date stockout_date;
	private Long porderreqid_link;
	private Date productiondate_plan;
	private Date productiondate_fact;
	private Date finishdate_plan;
	private Date finishdate_fact;
	private Boolean isbomdone;
	private Boolean issewingcostdone;
	private Integer plan_productivity;
	private Integer plan_duration;
	private Float plan_linerequired;
	private Boolean ismap;
	
	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private List<POrder_Product_SKU> list_sku = new ArrayList<POrder_Product_SKU>();
	
    @Transient
	public String getProductcode() {
		if(product != null) {
			return product.getBuyercode();
		}
		return "";
	}
    
	@Transient
	public String getProductName() {
		if (product != null)
			 return product.getName();
		return "";
	}
	
	@Transient
	public Long getProduct_id() {
		if (product != null)
			 return product.getId();
		return (long) 0;
	}
	
	@Transient
	public String getProduct_buyercode() {
		if (product != null)
			 return product.getBuyercode();
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
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Integer getTotalorder() {
		return totalorder;
	}
	public void setTotalorder(Integer totalorder) {
		this.totalorder = totalorder;
	}
	public Integer getTotalcut() {
		return totalcut;
	}
	public void setTotalcut(Integer totalcut) {
		this.totalcut = totalcut;
	}
	public Integer getTotalstocked() {
		return totalstocked;
	}
	public void setTotalstocked(Integer totalstocked) {
		this.totalstocked = totalstocked;
	}
	public Date getGolivedate() {
		return golivedate;
	}
	public void setGolivedate(Date golivedate) {
		this.golivedate = golivedate;
	}
	public String getGolivedesc() {
		return golivedesc;
	}
	public void setGolivedesc(String golivedesc) {
		this.golivedesc = golivedesc;
	}
	public Integer getBalance_status() {
		return balance_status;
	}
	public void setBalance_status(Integer balance_status) {
		this.balance_status = balance_status;
	}
	public Date getBalance_date() {
		return balance_date;
	}
	public void setBalance_date(Date balance_date) {
		this.balance_date = balance_date;
	}
	public Float getBalance_rate() {
		return balance_rate;
	}
	public void setBalance_rate(Float balance_rate) {
		this.balance_rate = balance_rate;
	}
	public Date getProductiondate() {
		return productiondate;
	}
	public void setProductiondate(Date productiondate) {
		this.productiondate = productiondate;
	}
	public Integer getProductionyear() {
		return productionyear;
	}
	public void setProductionyear(Integer productionyear) {
		this.productionyear = productionyear;
	}
	public Integer getSalaryyear() {
		return salaryyear;
	}
	public void setSalaryyear(Integer salaryyear) {
		this.salaryyear = salaryyear;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}
	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}
	public Date getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(Date timecreated) {
		this.timecreated = timecreated;
	}
	public Integer getSalarymonth() {
		return salarymonth;
	}
	public void setSalarymonth(Integer salarymonth) {
		this.salarymonth = salarymonth;
	}

	public Date getMaterial_date() {
		return material_date;
	}

	public void setMaterial_date(Date material_date) {
		this.material_date = material_date;
	}

	public Date getSample_date() {
		return sample_date;
	}

	public void setSample_date(Date sample_date) {
		this.sample_date = sample_date;
	}

	public Date getCut_date() {
		return cut_date;
	}

	public void setCut_date(Date cut_date) {
		this.cut_date = cut_date;
	}

	public Date getPacking_date() {
		return packing_date;
	}

	public void setPacking_date(Date packing_date) {
		this.packing_date = packing_date;
	}

	public Date getQc_date() {
		return qc_date;
	}

	public void setQc_date(Date qc_date) {
		this.qc_date = qc_date;
	}

	public Date getStockout_date() {
		return stockout_date;
	}

	public void setStockout_date(Date stockout_date) {
		this.stockout_date = stockout_date;
	}

	public Long getPorderreqid_link() {
		return porderreqid_link;
	}

	public void setPorderreqid_link(Long porderreqid_link) {
		this.porderreqid_link = porderreqid_link;
	}

	public Date getProductiondate_plan() {
		return productiondate_plan;
	}

	public void setProductiondate_plan(Date productiondate_plan) {
		this.productiondate_plan = productiondate_plan;
	}

	public Date getProductiondate_fact() {
		return productiondate_fact;
	}

	public void setProductiondate_fact(Date productiondate_fact) {
		this.productiondate_fact = productiondate_fact;
	}

	public Date getFinishdate_plan() {
		return finishdate_plan;
	}

	public void setFinishdate_plan(Date finishdate_plan) {
		this.finishdate_plan = finishdate_plan;
	}

	public Date getFinishdate_fact() {
		return finishdate_fact;
	}

	public void setFinishdate_fact(Date finishdate_fact) {
		this.finishdate_fact = finishdate_fact;
	}

	public Boolean getIsbomdone() {
		return isbomdone;
	}

	public void setIsbomdone(Boolean isbomdone) {
		this.isbomdone = isbomdone;
	}

	public Boolean getIssewingcostdone() {
		return issewingcostdone;
	}

	public void setIssewingcostdone(Boolean issewingcostdone) {
		this.issewingcostdone = issewingcostdone;
	}

	public Integer getPlan_productivity() {
		return plan_productivity;
	}

	public void setPlan_productivity(Integer plan_productivity) {
		this.plan_productivity = plan_productivity;
	}

	public Integer getPlan_duration() {
		return plan_duration;
	}

	public void setPlan_duration(Integer plan_duration) {
		this.plan_duration = plan_duration;
	}

	public Float getPlan_linerequired() {
		return plan_linerequired;
	}

	public void setPlan_linerequired(Float plan_linerequired) {
		this.plan_linerequired = plan_linerequired;
	}

	public Boolean getIsmap() {
		return ismap;
	}

	public void setIsmap(Boolean ismap) {
		this.ismap = ismap;
	}
	
}
