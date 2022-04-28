package vn.gpay.jitin.core.porder;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrderRepository extends JpaRepository<POrder, Long>, JpaSpecificationExecutor<POrder> {
	@Query(value = "select c from POrder c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and ordercode = :ordercode "
			+ "and status <> -1")
	public List<POrder> get_by_code(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("ordercode")final  String ordercode);
	
	@Query(value = "select distinct c.id from POrder c "
			+ "inner join Product d on c.productid_link = d.id "
			+ "and lower(trim(d.buyercode)) like concat('%',lower(trim(:buyercode)),'%') "
			)
	public List<Long> getidby_buyercode(
			@Param ("buyercode")final String buyercode
			);
}
