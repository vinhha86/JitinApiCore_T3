package vn.gpay.jitin.core.stockout;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IStockOutDService extends Operations<StockOutD>{
	List<StockOutD> getBystockOutId(Long stockoutid_link);
	
	List<Long> getStockoutIdBySkuId(Long skuid_link);

	List<StockOutD> findBy_PContractAndSku(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link,Long stockid_link, Long skuid_link);

	void recal_Totalcheck(Long stockoutdid_link);

	List<StockOutD> to_production_m_findBy_PContractAndSku(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link,
			Long stockid_link, Long skuid_link);

	List<StockOutD> to_vendor_p_findBy_PContractAndSku(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link,
			Long stockid_link, Long skuid_link);

//	List<StockOutD> p_findBy_PContractAndSku(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link,
//			Long stockid_link, Long skuid_link);
	
	List<StockOutD> getBy_MaNpl_StockoutId(String maNpl, Long stockoutid_link);

	List<StockOutD> to_vendor_p_findBy_PContract(Long pcontractid_link, Long pcontract_poid_link, Long porderid_link,
			Long stockid_link);

	List<StockOutD> to_production_m_findBy_PContract(Long pcontractid_link, Long pcontract_poid_link,
			Long porderid_link, Long stockid_link);
	
	List<StockOutD> getBy_StockoutOrder_and_SkuId(Long stockoutorderid_link, Long skuid_link, Integer status_from, Integer status_to);
}
