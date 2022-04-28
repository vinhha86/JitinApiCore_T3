package vn.gpay.jitin.core.stock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StockTree implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// ds phan xuong -> ds kho -> ds day -> ds hang -> ds tang
	
	private String idString;
	
		// type: 0: dha, 1: phan xuong, 2: kho, 3: day, 4: hang, 5: tang
		// dha = type + id dha
		// phan xuong = type + id phanxuong //
		// kho = type + id kho
		// day = type + id day
		// hang = type + rowid_link + spacename
		// tang = type + spaceepc
		// phân cách bằng dấu ; 
	
	private String parentIdString;
	
	private Long id;
	
	private Long orgid_link;
	
	private String name;
	
	private String spaceepc;
	
	private String spacename;
	
	private Long rowid_link;
	
	private Integer floorid;
	
	private boolean isKhoangKhongXacDinh;
	
	private boolean isShop;
	
	private Integer type; // 0: dha, 1: phan xuong, 2: kho, 3: day, 4: hang, 5: tang
	
	private boolean checked;
    
    private boolean expanded;
	
	private StockTree parent;
	
	private List<StockTree> children =new ArrayList<StockTree>();
	
	public void addChild(StockTree child) {
        if (!this.children.contains(child) && child != null)
            this.children.add(child);
    }
    
    public boolean getLeaf() {
    	if(this.children.size()>0) {
    		return false;
    	}else {
    		return true;
    	}
    }
    public boolean getSelectable() {
    	if(this.children.size()>0) {
    		return false;
    	}else {
    		return true;
    	}
    }
    
    ///// Getters, setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpaceepc() {
		return spaceepc;
	}

	public void setSpaceepc(String spaceepc) {
		this.spaceepc = spaceepc;
	}

	public String getSpacename() {
		return spacename;
	}

	public void setSpacename(String spacename) {
		this.spacename = spacename;
	}

	public Long getRowid_link() {
		return rowid_link;
	}

	public void setRowid_link(Long rowid_link) {
		this.rowid_link = rowid_link;
	}

	public Integer getFloorid() {
		return floorid;
	}

	public void setFloorid(Integer floorid) {
		this.floorid = floorid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public StockTree getParent() {
		return parent;
	}

	public void setParent(StockTree parent) {
		this.parent = parent;
	}

	public List<StockTree> getChildren() {
		return children;
	}

	public void setChildren(List<StockTree> children) {
		this.children = children;
	}

	public String getIdString() {
		return idString;
	}

	public void setIdString(String idString) {
		this.idString = idString;
	}

	public String getParentIdString() {
		return parentIdString;
	}

	public void setParentIdString(String parentIdString) {
		this.parentIdString = parentIdString;
	}

	public boolean isKhoangKhongXacDinh() {
		return isKhoangKhongXacDinh;
	}

	public void setKhoangKhongXacDinh(boolean isKhoangKhongXacDinh) {
		this.isKhoangKhongXacDinh = isKhoangKhongXacDinh;
	}

	public boolean isShop() {
		return isShop;
	}

	public void setShop(boolean isShop) {
		this.isShop = isShop;
	}
	
}
