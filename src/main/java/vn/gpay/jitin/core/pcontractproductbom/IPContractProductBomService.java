package vn.gpay.jitin.core.pcontractproductbom;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IPContractProductBomService extends Operations<PContractProductBom>{
	public List<PContractProductBom> get_pcontract_productBOMbyid(long productid_link, long pcontractid_link);
	public List<PContractProductBom> getby_pcontract_product_material(long productid_link, long pcontractid_link, long materialid_link);
}
