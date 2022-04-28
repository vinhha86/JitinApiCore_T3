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
public interface StockinLotSpaceRepository extends JpaRepository<StockinLotSpace, Long>,JpaSpecificationExecutor<StockinLotSpace>{
	@Query(value = "select a from StockinLotSpace a "
			+ " where a.stockinlotid_link =:stockinlotid_link"
			+ " and lower(a.spaceepcid_link) = lower(:spaceepcid_link)")
	public List<StockinLotSpace> getByLotAndSpace(
			@Param ("stockinlotid_link")final Long stockinlotid_link,
			@Param ("spaceepcid_link")final String spaceepcid_link
			);
	
	@Query(value = "select a from StockinLotSpace a "
			+ " where a.stockinlotid_link =:stockinlotid_link")
	public List<StockinLotSpace> getByLot(
			@Param ("stockinlotid_link")final Long stockinlotid_link
			);
}
