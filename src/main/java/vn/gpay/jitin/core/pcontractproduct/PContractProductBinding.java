package vn.gpay.jitin.core.pcontractproduct;


public class PContractProductBinding {
	private Long id;
	private Long pcontractid_link;
	private Long productid_link;
	private Integer pquantity;
	private Integer amount;
	private Long orgrootid_link;
	private byte[] imgproduct;
	private String productName;
	private String productCode;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPcontractid_link() {
		return pcontractid_link;
	}
	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}
	public Long getProductid_link() {
		return productid_link;
	}
	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}
	public Integer getPquantity() {
		return pquantity;
	}
	public void setPquantity(Integer pquantity) {
		this.pquantity = pquantity;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public byte[] getImgproduct() {
		return imgproduct;
	}
	public void setImgproduct(byte[] imgproduct) {
		this.imgproduct = imgproduct;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	
}
