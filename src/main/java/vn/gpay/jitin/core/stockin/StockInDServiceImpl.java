package vn.gpay.jitin.core.stockin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.utils.StockinStatus;
@Service
public class StockInDServiceImpl extends AbstractService<StockInD> implements IStockInDService{

	@Autowired StockInDRepository repository;
	@Autowired StockInPklistRepository repository_pklist; 
	@Override
	protected JpaRepository<StockInD, Long> getRepository() {
		return repository;
	}
	@Override
	public List<StockInD> findBy_InvoiceAndSku(Long material_invoiceid_link, Long skuid_link){
		return repository.findBy_InvoiceAndSku(material_invoiceid_link, skuid_link);
	}
	
	@Override
	public List<StockInD> from_vendor_findBy_PContractAndSku(Long pcontractid_link, Long stockid_link, Long skuid_link){
//		System.out.println(pcontractid_link + "-" + stockid_link + "-" + skuid_link);
		return repository.from_vendor_findBy_PContractAndSku(pcontractid_link, stockid_link, skuid_link);
	}
	@Override
	public List<StockInD> from_vendor_findBy_PContract(Long pcontractid_link, Long stockid_link){
		return repository.from_vendor_findBy_PContract(pcontractid_link, stockid_link);
	}
	
	//Nhap kho thanh pham tu hoan thien
	@Override
	public List<StockInD> pfrom_packing_findBy_PContractAndSku(Long pcontractid_link, Long stockid_link, Long skuid_link){
		return repository.pfrom_packing_findBy_PContractAndSku(pcontractid_link, stockid_link, skuid_link);
	}
	@Override
	public List<StockInD> pfrom_packing_findBy_PContract(Long pcontractid_link, Long stockid_link){
		return repository.pfrom_packing_findBy_PContract(pcontractid_link, stockid_link);
	}
	
	@Override
	public List<StockInD> getByStockinId(Long stockinid_link) {
		// TODO Auto-generated method stub
		return repository.getByStockinId(stockinid_link);
	}
	
	@Override
	public List<StockInD> getbyStockinID_StockinDID(Long stockinid_link, Long stockindid_link) {
		return repository.getbyStockinID_StockinDID(stockinid_link, stockindid_link);
	}
	
	@Override
	public void recal_Totalcheck(Long stockindid_link){
		StockInD theStockinD = repository.getOne(stockindid_link);
		
		Specification<StockInPklist> specification = Specifications.<StockInPklist>and()
	            .ge( "status", StockinStatus.STOCKIN_EPC_STATUS_CHECK_LABEL)
	            .eq( "stockindid_link", stockindid_link)
	            .build();
		List<StockInPklist> ls_Pklist = repository_pklist.findAll(specification);
		
		//Tổng cây vải đã check
		theStockinD.setTotalpackagecheck(ls_Pklist.size());
		
		//Tổng dài vải đã check, Tổng cân vải đã check
		Float totalydscheck = (float) 0;
		Float totalmet_check = (float) 0;
		Float netweight = (float) 0;
		Float netweight_lbs = (float) 0;
		for(StockInPklist pklist:ls_Pklist){
			totalydscheck += null != pklist.getYdscheck()?pklist.getYdscheck():0;
			totalmet_check += null != pklist.getMet_check()?pklist.getMet_check():0;
			netweight += null != pklist.getGrossweight_check()?pklist.getGrossweight_check():0;
			netweight_lbs += null != pklist.getGrossweight_lbs_check()?pklist.getGrossweight_lbs_check():0;
		}
		theStockinD.setTotalydscheck(totalydscheck);
		theStockinD.setTotalmet_check(totalmet_check);
		theStockinD.setNetweight(netweight);
		theStockinD.setNetweight_lbs(netweight_lbs);
		
		//Set lại status
		if(theStockinD.getTotalmet_check() >= theStockinD.getTotalmet_origin()) {
			theStockinD.setStatus(StockinStatus.STOCKIN_D_STATUS_OK);
		}else {
			theStockinD.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
		}
		
		repository.save(theStockinD);
	}
	@Override
	public List<Long> getStockinIdBySkuId(Long skuid_link) {
		return repository.getStockinIdBySkuId(skuid_link);
	}
	
}
