package vn.gpay.jitin.core.cutplan_processing;

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

import vn.gpay.jitin.core.org.Org;

@Table(name="cutplan_processing")
@Entity
public class CutplanProcessing implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cutplan_processing_generator")
	@SequenceGenerator(name="cutplan_processing_generator", sequenceName = "cutplan_processing_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name = "processingdate")
	private Date processingdate;
	
	@Column(name = "cutplanrowid_link")
	private Long cutplanrowid_link;
	
	@Column(name = "cutorgid_link")
	private Long cutorgid_link;
	
	@Column(name = "amountcut")
	private Integer amountcut;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "usercreatedid_link")
	private Long usercreatedid_link;
	
	@Column(name = "timecreated")
	private Date timecreated;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="cutorgid_link",insertable=false,updatable =false)
    private Org cutorg ;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="cutplan_processingid_link", referencedColumnName="id")
	private List<CutplanProcessingD> cutplanProcessingD = new ArrayList<CutplanProcessingD>();

	@Transient
	public String getCutorg_name() {
		if(cutorg!=null) {
			return cutorg.getName();
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

	public Date getProcessingdate() {
		return processingdate;
	}

	public void setProcessingdate(Date processingdate) {
		this.processingdate = processingdate;
	}

	public Long getCutplanrowid_link() {
		return cutplanrowid_link;
	}

	public void setCutplanrowid_link(Long cutplanrowid_link) {
		this.cutplanrowid_link = cutplanrowid_link;
	}

	public Long getCutorgid_link() {
		return cutorgid_link;
	}

	public void setCutorgid_link(Long cutorgid_link) {
		this.cutorgid_link = cutorgid_link;
	}

	public Integer getAmountcut() {
		return amountcut;
	}

	public void setAmountcut(Integer amountcut) {
		this.amountcut = amountcut;
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

	public List<CutplanProcessingD> getCutplanProcessingD() {
		return cutplanProcessingD;
	}

	public void setCutplanProcessingD(List<CutplanProcessingD> cutplanProcessingD) {
		this.cutplanProcessingD = cutplanProcessingD;
	}
	
}
