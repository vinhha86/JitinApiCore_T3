package vn.gpay.jitin.core.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class StockspaceServiceImpl extends AbstractService<Stockspace> implements IStockspaceService{

	@Autowired
	StockspaceRepository repositoty;
	
	@Override
	protected JpaRepository<Stockspace, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Stockspace> findStockspaceByEpc(long orgid, String spaceepc) {
		// TODO Auto-generated method stub
		return repositoty.findStockspaceByEpc(orgid, spaceepc);
	}

	@Override
	public List<Stockspace> findStockspaceByRowSpaceFloor(String code, String spacename, Integer floorid, Long orgid_link) {
		// TODO Auto-generated method stub
		return repositoty.findStockspaceByRowSpaceFloor(code, spacename, floorid, orgid_link);
	}

	@Override
	public List<Stockspace> findStockspaceByRowIdSpaceFloor(Long rowid_link, String spacename, Integer floorid) {
		// TODO Auto-generated method stub
		return repositoty.findStockspaceByRowIdSpaceFloor(rowid_link, spacename, floorid);
	}

	@Override
	public List<Stockspace> findStockspaceByRowIdSpace(Long rowid_link, String spacename) {
		// TODO Auto-generated method stub
		return repositoty.findStockspaceByRowIdSpace(rowid_link, spacename);
	}

	@Override
	public List<Stockspace> findStockspaceByEpc(String epc) {
		// TODO Auto-generated method stub
		return repositoty.findStockspaceByEpc(epc);
	}	
}
