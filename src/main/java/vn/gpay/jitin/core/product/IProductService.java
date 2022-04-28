package vn.gpay.jitin.core.product;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.jitin.core.api.product.Product_getall_request;
import vn.gpay.jitin.core.base.Operations;


public interface IProductService extends Operations<Product> {
	public List<Product> getall_by_orgrootid(Long orgrootid_link, int product_type);
	public Page<Product> getall_by_orgrootid_paging(Long orgrootid_link, Product_getall_request request);
	public List<Product> getone_by_code(Long orgrootid_link, String code, Long productid_link, int product_type);
	public List<Product> getList_material_notin_ProductBOM(Long orgrootid_link, String code, String name, String TenMauNPL, Long productid_link, int product_type);
	public List<Product> getList_material_notin_PContractProductBOM(Long orgrootid_link, String code, String name, String TenMauNPL, Long productid_link, int product_type, long pcontractid_link);
}
