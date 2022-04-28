package vn.gpay.jitin.core.stockin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockinLotRepository  extends JpaRepository<StockinLot, Long>,JpaSpecificationExecutor<StockinLot>{

	@Query(value = "select a from StockinLot a "
			+ " where a.stockinid_link =:stockinid_link")
	public List<StockinLot> getByStockinId(
			@Param ("stockinid_link")final Long stockinid_link
			);
	
	@Query(value = "select a from StockinLot a "
			+ " where a.stockindid_link =:stockindid_link")
	public List<StockinLot> getByStockinDId(
			@Param ("stockindid_link")final Long stockindid_link
			);
	
	@Query(value = "select a from StockinLot a "
			+ " where a.stockindid_link =:stockindid_link"
			+ " and lower(a.lot_number) = lower(:lot_number)")
	public List<StockinLot> getByStockinD_LOT(
			@Param ("stockindid_link")final Long stockindid_link,
			@Param ("lot_number")final String lot_number
			);
}
