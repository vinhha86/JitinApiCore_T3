package vn.gpay.jitin.core.invcheck;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Table(name="invcheck_epc")
@Entity
@IdClass(InvcheckEpcID.class)
public class InvcheckEpc implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//@EmbeddedId
   // private InvcheckEpcID invcheckepc_pk;
	@Id
	@Column(name = "orgrootid_link")
    private Long orgrootid_link;
	@Id
    @Column(name = "invcheckid_link")
    private Long invcheckid_link;
	@Id
    @Column(name = "epc",length=50)
    private String epc;
	
	@Column(name ="skuid_link")
    private Long skuid_link;
	
	@Column(name = "unitid_link")
    private Integer unitid_link;	
	
	@Column(name ="ydsorigin")
    private Float ydsorigin;
	
	@Column(name ="ydscheck")
    private Float ydscheck;
	
	@Column(name ="met_origin")
    private Float met_origin;
	
	@Column(name ="met_check")
    private Float met_check;
	
	@Column(name ="unitprice")
    private Float unitprice;
	
	@Column(name ="rssi")
    private Integer rssi;
	
	@Column(name ="width")
    private Float width;
	
	@Column(name="packageid")
	private Long packageid;
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Long getInvcheckid_link() {
		return invcheckid_link;
	}

	public void setInvcheckid_link(Long invcheckid_link) {
		this.invcheckid_link = invcheckid_link;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

	public Integer getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}

	public Float getYdsorigin() {
		return ydsorigin;
	}

	public void setYdsorigin(Float ydsorigin) {
		this.ydsorigin = ydsorigin;
	}

	public Float getYdscheck() {
		return ydscheck;
	}

	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}

	public Float getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}

	public Integer getRssi() {
		return rssi;
	}

	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Long getPackageid() {
		return packageid;
	}

	public void setPackageid(Long packageid) {
		this.packageid = packageid;
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

	public Float getMet_origin() {
		return met_origin;
	}

	public void setMet_origin(Float met_origin) {
		this.met_origin = met_origin;
	}

	public Float getMet_check() {
		return met_check;
	}

	public void setMet_check(Float met_check) {
		this.met_check = met_check;
	}
	
	
}
