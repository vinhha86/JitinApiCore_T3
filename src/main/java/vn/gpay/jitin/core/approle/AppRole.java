package vn.gpay.jitin.core.approle;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="app_role")
@Entity
public class AppRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_role_generator")
	@SequenceGenerator(name="app_role_generator", sequenceName = "app_role_id_seq", allocationSize=1)
	private Long id;
	private String name;
	private String name_en;
	
	@Transient
	public boolean checked;
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getName_en() {
		return name_en;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	
	

}
