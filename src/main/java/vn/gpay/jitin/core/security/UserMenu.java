package vn.gpay.jitin.core.security;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "app_user_menu")
public class UserMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_menu_generator")
	@SequenceGenerator(name="app_user_menu_generator", sequenceName = "app_user_menu_id_seq", allocationSize=1)
	protected Long id;
	
	private String menuid;
	private Long userid;
	public Long getId() {
		return id;
	}
	public String getMenuid() {
		return menuid;
	}
	public Long getUserid() {
		return userid;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	

}
