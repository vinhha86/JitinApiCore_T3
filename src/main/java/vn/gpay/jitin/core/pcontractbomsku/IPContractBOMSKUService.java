package vn.gpay.jitin.core.pcontractbomsku;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IPContractBOMSKUService extends Operations<PContractBOMSKU>{
	public List<PContractBOMSKU> getall_material_in_productBOMSKU(long pcontractid_link, long productid_link, long sizeid_link, long colorid_link, long materialid_link);
	public List<PContractBOMSKU> getcolor_bymaterial_in_productBOMSKU(long pcontractid_link, long productid_link, long materialid_link);
	public long getskuid_link_by_color_and_size(long colorid_link, long sizeid_link, long productid_link);
	public List<PContractBOMSKU> getmaterial_bycolorid_link(long pcontractid_link, long productid_link, long colorid_link, long materialid_link);
	public List<Long> getsize_bycolor(long pcontractid_link, long productid_link, long colorid_link);
}
