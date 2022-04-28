package vn.gpay.jitin.core.rfprint_stockin;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Table(name="rfprint_stockin")
@Entity
public class RFPrint_Stockin implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rfprint_stockin_generator")
	@SequenceGenerator(name="rfprint_stockin_generator", sequenceName = "rfprint_stockin_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "code",length=5)
    private String code;
	
	@Column(name = "stockinid_link")
	private Long stockinid_link;
	
	@Column(name = "total_req")
    private Integer total_req;
	
	@Column(name = "total_printed")
    private Integer total_printed;
	
	@Column(name = "err_code",length=50)
    private String err_code;
	
	@Column(name = "status")
    private Integer status;
	
	@Column(name = "rfid_enable")
    private Boolean rfid_enable;
	
	@Column(name = "passcode")
    private String passcode;
	
	@Column(name = "deviceid_link")
	private Long deviceid_link;
	
	@Column(name="usercreatedid_link")
	private Long usercreatedid_link;
	
	@Column(name ="timecreated")
	private Date timecreated;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	//@BatchSize(size=10)
	@JoinColumn( name="rfprintid_link", referencedColumnName="id")
	private List<RFPrint_Stockin_D>  rfprint_d  = new ArrayList<RFPrint_Stockin_D>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getStockinid_link() {
		return stockinid_link;
	}

	public void setStockinid_link(Long stockinid_link) {
		this.stockinid_link = stockinid_link;
	}

	public Integer getTotal_req() {
		return total_req;
	}

	public void setTotal_req(Integer total_req) {
		this.total_req = total_req;
	}

	public Integer getTotal_printed() {
		return total_printed;
	}

	public void setTotal_printed(Integer total_printed) {
		this.total_printed = total_printed;
	}

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getDeviceid_link() {
		return deviceid_link;
	}

	public void setDeviceid_link(Long deviceid_link) {
		this.deviceid_link = deviceid_link;
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

	public List<RFPrint_Stockin_D> getRfprint_d() {
		return rfprint_d;
	}

	public void setRfprint_d(List<RFPrint_Stockin_D> rfprint_d) {
		this.rfprint_d = rfprint_d;
	}

	public Boolean getRfid_enable() {
		return rfid_enable;
	}

	public void setRfid_enable(Boolean rfid_enable) {
		this.rfid_enable = rfid_enable;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}
	
	
}
