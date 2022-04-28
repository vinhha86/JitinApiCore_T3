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
public class StockinLotServiceImpl extends AbstractService<StockinLot> implements IStockinLotService{

	@Autowired StockinLotRepository repository;
	@Autowired StockInPklistRepository repository_pklist;
	
	@Override
	protected JpaRepository<StockinLot, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<StockinLot> getByStockinId(Long stockinid_link) {
		// TODO Auto-generated method stub
		return repository.getByStockinId(stockinid_link);
	}

	@Override
	public void recal_Totalcheck(Long stockindid_link, String lot_number){
		Specification<StockInPklist> specification = Specifications.<StockInPklist>and()
	            .ge( "status", StockinStatus.STOCKIN_EPC_STATUS_CHECK_LABEL)
	            .eq( "stockindid_link", stockindid_link)
	            .eq( "lotnumber", lot_number)
	            .build();
		List<StockInPklist> ls_Pklist = repository_pklist.findAll(specification);
		
		List<StockinLot> lsStockin_Lot = repository.getByStockinD_LOT(stockindid_link,lot_number);
		for(StockinLot theStockin_Lot: lsStockin_Lot){
			//Tổng cây vải đã check
			theStockin_Lot.setTotalpackagepklist(ls_Pklist.size());
			
			//Tổng dài vải đã check, Tổng cân vải đã check
			Float totalydscheck = (float) 0;
			Float totalmet_check = (float) 0;
			Float grossweight_check = (float) 0;
			Float grossweight_lbs_check = (float) 0;
			for(StockInPklist pklist:ls_Pklist){
				totalydscheck += null != pklist.getYdscheck()?pklist.getYdscheck():0;
				totalmet_check += null != pklist.getMet_check()?pklist.getMet_check():0;
				grossweight_check += null != pklist.getGrossweight_check()?pklist.getGrossweight_check():0;
				grossweight_lbs_check += null != pklist.getGrossweight_lbs_check()?pklist.getGrossweight_lbs_check():0;
			}
			theStockin_Lot.setTotalydscheck(totalydscheck);
			theStockin_Lot.setTotalmetcheck(totalmet_check);
			theStockin_Lot.setGrossweight_check(grossweight_check);
			theStockin_Lot.setGrossweight_lbs_check(grossweight_lbs_check);
			
			repository.save(theStockin_Lot);
		}
	}

	@Override
	public List<StockinLot> getByStockinD_LOT(Long stockindid_link, String lot_number) {
		return repository.getByStockinD_LOT(stockindid_link, lot_number);
	}

	@Override
	public List<StockinLot> getByStockinDId(Long stockindid_link) {
		return repository.getByStockinDId(stockindid_link);
	}
}
