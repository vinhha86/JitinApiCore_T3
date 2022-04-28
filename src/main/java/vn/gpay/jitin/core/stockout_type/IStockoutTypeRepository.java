package vn.gpay.jitin.core.stockout_type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IStockoutTypeRepository extends JpaRepository<StockoutType, Long>, JpaSpecificationExecutor<StockoutType> {
	@Query(value = "select a from StockoutType a "
			+ "where (a.id >= :idFrom or :idFrom is null) "
			+ "and (a.id <= :idTo or :idTo is null) "
			+ "order by a.id "
			)
	public List<StockoutType> findStockoutTypeByIdRange(
			@Param ("idFrom")final Long idFrom,
			@Param ("idTo")final Long idTo
			)
	;
}
