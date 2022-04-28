package vn.gpay.jitin.core.stock;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IStockspaceService extends Operations<Stockspace>{
	public List<Stockspace> findStockspaceByEpc(long orgid, String epc);
	
	List<Stockspace> findStockspaceByRowSpaceFloor(String code, String spacename, Integer floorid, Long orgid_link);
	
	List<Stockspace> findStockspaceByRowIdSpaceFloor(Long rowid_link, String spacename, Integer floorid);
	
	List<Stockspace> findStockspaceByRowIdSpace(Long rowid_link, String spacename);
	
	public List<Stockspace> findStockspaceByEpc(String epc);

}
