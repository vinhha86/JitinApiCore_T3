package vn.gpay.jitin.core.devices;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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



@Table(name="devices")
@Entity
public class Devices implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "devices_generator")
	@SequenceGenerator(name="devices_generator", sequenceName = "devices_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="org_governid_link")
    private Long org_governid_link;
	
	@Column(name ="code",length=50)
    private String code;
	
	@Column(name ="name",length=100)
    private String name;
	
	@Column(name ="type")
    private Long type;
	
	@Column(name ="extrainfo",length=200)
    private String extrainfo;
	
	@Column(name ="status")
    private Integer status;
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	@Column(name ="devicegroupid_link")
    private Long devicegroupid_link;
	
	private Boolean disable;
	
	private String epc;
	
	@Column(name ="serialno",length=20)
    private String serialno;
	
	@Column(name ="fixassetno",length=20)
    private String fixassetno;
	
	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="devicegroupid_link",insertable=false,updatable =false)
    private DeviceGroup devicegroup;
	
	@Transient
	public String getDeviceGroupName() {
		if(devicegroup!=null) {
			return devicegroup.getName();
		}
		return "";
	}
	
	//thên trường 
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="type",insertable=false,updatable =false)
    private Devices_Type devices_type;
	
	@Transient
	public String getDevices_Type() {
		if(devices_type!=null) {
			return devices_type.getName();
		}
		return "";
	}
	
	
	@Transient
	public String getDeviceType() {
		if(devicegroup!=null) {
			if(devicegroup.getParentid_link() == null) return "";
			if(devicegroup.getParentid_link().equals((long)-1)) {
				return devicegroup.getName();
			}else {
				return devicegroup.getParentname();
			}
		}
		return "";
	}
	
	@Transient
	public String getDeviceModel() {
		if(devicegroup!=null) {
			if(devicegroup.getParentid_link().equals((long)-1)) {
				return "";
			}else {
				return devicegroup.getName();
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

	public Long getOrg_governid_link() {
		return org_governid_link;
	}

	public void setOrg_governid_link(Long org_governid_link) {
		this.org_governid_link = org_governid_link;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
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

	public Long getDevicegroupid_link() {
		return devicegroupid_link;
	}

	public void setDevicegroupid_link(Long devicegroupid_link) {
		this.devicegroupid_link = devicegroupid_link;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getFixassetno() {
		return fixassetno;
	}

	public void setFixassetno(String fixassetno) {
		this.fixassetno = fixassetno;
	}
	
}
