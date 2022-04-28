package vn.gpay.jitin.core.porder_grant;

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

import vn.gpay.jitin.core.porder.POrder;

@Table(name="porder_grant")
@Entity
public class POrder_Grant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_grant_generator")
	@SequenceGenerator(name="porder_grant_generator", sequenceName = "porder_grant_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long porderid_link;
	private String ordercode;
	private Long granttoorgid_link;
	private Date grantdate;
	private Integer grantamount;
	private Integer amountcutsum;
	private Integer status;
	private Long usercreatedid_link;
	private Date timecreated;
	private Date start_date_plan;
	private Date finish_date_plan;
	
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private POrder porder;
	
	@Transient
	public String getProductcode() {
		if(porder!=null)
			return porder.getProductcode();
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
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public Long getGranttoorgid_link() {
		return granttoorgid_link;
	}
	public void setGranttoorgid_link(Long granttoorgid_link) {
		this.granttoorgid_link = granttoorgid_link;
	}
	public Date getGrantdate() {
		return grantdate;
	}
	public void setGrantdate(Date grantdate) {
		this.grantdate = grantdate;
	}
	public Integer getGrantamount() {
		return grantamount;
	}
	public void setGrantamount(Integer grantamount) {
		this.grantamount = grantamount;
	}
	public Integer getAmountcutsum() {
		return amountcutsum;
	}
	public void setAmountcutsum(Integer amountcutsum) {
		this.amountcutsum = amountcutsum;
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
	public Date getStart_date_plan() {
		return start_date_plan;
	}
	public void setStart_date_plan(Date start_date_plan) {
		this.start_date_plan = start_date_plan;
	}
	public Date getFinish_date_plan() {
		return finish_date_plan;
	}
	public void setFinish_date_plan(Date finish_date_plan) {
		this.finish_date_plan = finish_date_plan;
	}
	
	
}
