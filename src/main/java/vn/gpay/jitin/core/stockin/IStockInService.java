package vn.gpay.jitin.core.stockin;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.jitin.core.base.Operations;



public interface IStockInService extends Operations<StockIn>{

	public List<StockIn> findByStockinCode(Long orgid_link,String stockincode);
	
	public List<StockIn> stockin_getone(Long orgid_link,String stockincode,String stockcode);
	
	public List<StockIn> stockin_list(Long orgrootid_link,Long stockintypeid_link,Long orgid_from_link,
			Long orgid_to_link,Date stockindate_from,Date stockindate_to);
	
	public Page<StockIn> stockin_page(Long orgrootid_link,Long stockintypeid_link,Long orgid_from_link,
			Long orgid_to_link,Date stockindate_from,Date stockindate_to, int limit, int page);
	
	public Page<StockIn> stockin_page(Integer userOrgType, Long userOrgId, Long orgrootid_link,Long stockintypeid_link,Long orgid_from_link,
			Long orgid_to_link,Date stockindate_from,Date stockindate_to, int limit, int page);
	
	public List<Results> test(String stockincode);

	List<StockIn> stockin_material_list(Long orgrootid_link, Long stockintypeid_link, Long orgid_from_link,
			List<Long> orgid_to_linkArray, Date stockindate_from, Date stockindate_to, Long pcontractid_link, 
			Long stockintypeid_link_from, Long stockintypeid_link_to,List<Integer> statuses,
			List<Long> listStockinId, List<Long> listPContractId
			);
	List<StockIn> stockin_material_list_pcontract(Long orgrootid_link, Long stockintypeid_link, Long orgid_from_link,
			List<Long> orgid_to_linkArray, Date stockindate_from, Date stockindate_to, Long pcontractid_link, 
			Long stockintypeid_link_from, Long stockintypeid_link_to,List<Integer> statuses, List<Long> listStockinId,
			Long userId
			);

	List<StockIn> stockin_product_list(Long orgrootid_link, Long stockintypeid_link, Long orgid_from_link,
			List<Long> orgid_to_linkArray, Date stockindate_from, Date stockindate_to, Long stockintypeid_link_from, Long stockintypeid_link_to, List<Integer> status);

	void stockin_update_status(Long stockinid_link, Integer status);
	
	List<StockIn> findByPO_Type_Status(Long pcontract_poid_link, Integer stockintypeid_link, Integer status);
	
	List<StockinProduct> getStockinProductByStockinId(Long stockinid_link);
	
	public List<StockIn> findByStockoutId(Long stockoutid_link);
}
