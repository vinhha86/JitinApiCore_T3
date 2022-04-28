package vn.gpay.jitin.core.warehouse;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="warehouse_logs")
@Entity
//@IdClass(WarehouseId.class)
public class Warehouse_logs implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_logs_generator")
	@SequenceGenerator(name="warehouse_logs_generator", sequenceName = "warehouse_logs_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="epc",length=50)
    private String epc;
	
	@Column(name ="orglogid_link")
    private Long orglogid_link;
	
	@Column(name ="timelog")
    private Date timelog;
	
	@Column(name ="eventtype")
    private Integer eventtype;	
	
	@Column(name ="event_objid_link")
    private Long event_objid_link;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public Long getOrglogid_link() {
		return orglogid_link;
	}

	public void setOrglogid_link(Long orglogid_link) {
		this.orglogid_link = orglogid_link;
	}

	public Date getTimelog() {
		return timelog;
	}

	public void setTimelog(Date timelog) {
		this.timelog = timelog;
	}

	public Integer getEventtype() {
		return eventtype;
	}

	public void setEventtype(Integer eventtype) {
		this.eventtype = eventtype;
	}

	public Long getEvent_objid_link() {
		return event_objid_link;
	}

	public void setEvent_objid_link(Long event_objid_link) {
		this.event_objid_link = event_objid_link;
	}

}
