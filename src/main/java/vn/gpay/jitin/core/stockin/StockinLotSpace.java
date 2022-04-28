package vn.gpay.jitin.core.stockin;

import java.io.Serializable;

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

import vn.gpay.jitin.core.sku.SKU;
import vn.gpay.jitin.core.stock.Stockspace;

@Table(name="stockin_lot_space")
@Entity
public class StockinLotSpace  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockin_lot_space_generator")
	@SequenceGenerator(name="stockin_lot_space_generator", sequenceName = "stockin_lot_space_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "stockinlotid_link")
	private Long stockinlotid_link;
	
	@Column(name = "spaceepcid_link")
	private String spaceepcid_link;
	
	@Column(name = "totalpackage")
	private Integer totalpackage;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="spaceepcid_link",insertable=false,updatable =false)
    private Stockspace stockspace;

	@Transient
	public String getSpace() {
		if(stockspace!=null) {
			// D-1c|T-1|K-1|
			return "D-" + stockspace.getStockrow_code() 
			+ "|T-" + stockspace.getSpacename() 
			+ "|K-" + stockspace.getFloorid();
		}
		return null;
	}
	
	@Transient
	public String getSpaceInfo() {
		if(stockspace!=null) {
			// D-1c|T-1|K-1|
			return "D-" + stockspace.getStockrow_code() 
			+ "|T-" + stockspace.getSpacename() 
			+ "|K-" + stockspace.getFloorid()
			+ "(" + totalpackage + ")";
		}
		return null;
	}
	
	
	@Transient
	public String getStockspaceStockrow_code() {
		if(stockspace!=null) {
			return stockspace.getStockrow_code();
		}
		return "";
	}
	
	@Transient
	public String getStockspaceSpacename() {
		if(stockspace!=null) {
			return stockspace.getSpacename();
		}
		return "";
	}
	
	@Transient
	public Integer getStockspaceFloorid() {
		if(stockspace!=null) {
			return stockspace.getFloorid();
		}
		return 0;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStockinlotid_link() {
		return stockinlotid_link;
	}

	public void setStockinlotid_link(Long stockinlotid_link) {
		this.stockinlotid_link = stockinlotid_link;
	}

	public String getSpaceepcid_link() {
		return spaceepcid_link;
	}

	public void setSpaceepcid_link(String spaceepcid_link) {
		this.spaceepcid_link = spaceepcid_link;
	}

	public Integer getTotalpackage() {
		return totalpackage;
	}

	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	
	

}
