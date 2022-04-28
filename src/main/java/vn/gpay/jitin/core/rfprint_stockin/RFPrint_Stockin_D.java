package vn.gpay.jitin.core.rfprint_stockin;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="rfprint_stockin_d")
@Entity
public class RFPrint_Stockin_D implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rfprint_stockin_d_generator")
	@SequenceGenerator(name="rfprint_stockin_d_generator", sequenceName = "rfprint_stockin_d_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "rfprintid_link")
	private Long rfprintid_link;
	
	@Column(name = "inv_code",length=11)
    private String inv_code;
	
	@Column(name = "productid_link")
	private Long productid_link;
	
	@Column(name = "product_code",length=9)
    private String product_code;
	
	@Column(name = "material_id_link")
	private Long material_id_link;
	
	@Column(name = "material_code",length=10)
    private String material_code;
	
	@Column(name = "material_provider_code",length=12)
    private String material_provider_code;
	
	@Column(name = "color_name",length=25)
    private String color_name;
	
	@Column(name = "desc1",length=26)
    private String desc1;
	
	@Column(name = "desc2",length=22)
    private String desc2;
	
	@Column(name = "lotnumber",length=8)
    private String lotnumber;
	
	@Column(name = "packageid",length=5)
    private String packageid;
	
	@Column(name = "met",length=7)
    private String met;
	
	@Column(name = "width",length=6)
    private String width;
	
	@Column(name = "epc",length=32)
    private String epc;
	
	@Column(name = "tid",length=32)
    private String tid;
	
	@Column(name = "printtime")
    private Date printtime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRfprintid_link() {
		return rfprintid_link;
	}

	public void setRfprintid_link(Long rfprintid_link) {
		this.rfprintid_link = rfprintid_link;
	}

	public String getInv_code() {
		return inv_code;
	}

	public void setInv_code(String inv_code) {
		this.inv_code = inv_code;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public Long getMaterial_id_link() {
		return material_id_link;
	}

	public void setMaterial_id_link(Long material_id_link) {
		this.material_id_link = material_id_link;
	}

	public String getMaterial_code() {
		return material_code;
	}

	public void setMaterial_code(String material_code) {
		this.material_code = material_code;
	}

	public String getMaterial_provider_code() {
		return material_provider_code;
	}

	public void setMaterial_provider_code(String material_provider_code) {
		this.material_provider_code = material_provider_code;
	}

	public String getColor_name() {
		return color_name;
	}

	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}

	public String getDesc1() {
		return desc1;
	}

	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}

	public String getDesc2() {
		return desc2;
	}

	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}

	public String getLotnumber() {
		return lotnumber;
	}

	public void setLotnumber(String lotnumber) {
		this.lotnumber = lotnumber;
	}

	public String getPackageid() {
		return packageid;
	}

	public void setPackageid(String pakageid) {
		this.packageid = pakageid;
	}

	public String getMet() {
		return met;
	}

	public void setMet(String met) {
		this.met = met;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getEpc() {
		return epc;
	}

	public void setEpc(String epc) {
		this.epc = epc;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Date getPrinttime() {
		return printtime;
	}

	public void setPrinttime(Date printtime) {
		this.printtime = printtime;
	}


	
}
