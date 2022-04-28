package vn.gpay.jitin.core.org;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Table(name="org_autoid")
@Entity
public class Org_AutoID implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_autoid_generator")
	@SequenceGenerator(name="org_autoid_generator", sequenceName = "org_autoid_id_seq", allocationSize=1)
	private Long id;
	
	private String prefix;
    private Integer maxvalue;
    
    @Version
    private Integer version;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public Integer getMaxvalue() {
		return maxvalue;
	}
	public void setMaxvalue(Integer maxvalue) {
		this.maxvalue = maxvalue;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}