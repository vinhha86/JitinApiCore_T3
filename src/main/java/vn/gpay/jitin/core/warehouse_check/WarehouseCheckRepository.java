package vn.gpay.jitin.core.warehouse_check;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WarehouseCheckRepository extends JpaRepository<WarehouseCheck, Long>, JpaSpecificationExecutor<WarehouseCheck>{

//	@Query(value = "select c from WarehouseCheck c "
//			+ " inner join Warehouse d on d.id = c.warehouseid_link"
//			+ " where c.stockoutorderid_link = :stockoutorderid_link"
//			+ " and d.skuid_link = :skuid_link"
//			)
//	public List<WarehouseCheck> findByStockoutOrderAndSku(
//			@Param ("stockoutorderid_link")final Long stockoutorderid_link,
//			@Param ("skuid_link")final Long skuid_link
//			);
	
//	SELECT a.id, a.rev, a.contents
//	FROM YourTable a
//	INNER JOIN (
//	    SELECT id, MAX(rev) rev
//	    FROM YourTable
//	    GROUP BY id
//	) b ON a.id = b.id AND a.rev = b.rev
	
//	@Query(value = "select c from WarehouseCheck c"
//			+ " inner join Warehouse d on d.id = c.warehouseid_link"
//			+ " inner join (select b.warehouseid_link, max(b.date_check) date_check from WarehouseCheck b group by b.warehouseid_link) e"
//			+ " on c.warehouseid_link = e.warehouseid_link and c.date_check = e.date_check"
//			+ " where c.stockoutorderid_link = :stockoutorderid_link"
//			+ " and d.skuid_link = :skuid_link"
//			)
//	public List<WarehouseCheck> findByStockoutOrderAndSku(
//			@Param ("stockoutorderid_link")final Long stockoutorderid_link,
//			@Param ("skuid_link")final Long skuid_link
//			);
	
//	SELECT a.*
//	FROM YourTable a
//	LEFT OUTER JOIN YourTable b
//	    ON a.id = b.id AND a.rev < b.rev
//	WHERE b.id IS NULL;
	
	@Query(value = "select c from WarehouseCheck c"
		+ " inner join Warehouse d on d.id = c.warehouseid_link"
		+ " left join WarehouseCheck b on c.warehouseid_link = b.warehouseid_link and c.date_check < b.date_check"
		+ " where c.stockoutorderid_link = :stockoutorderid_link"
		+ " and d.skuid_link = :skuid_link"
		+ " and b.warehouseid_link is null"
		)
	public List<WarehouseCheck> findByStockoutOrderAndSku(
		@Param ("stockoutorderid_link")final Long stockoutorderid_link,
		@Param ("skuid_link")final Long skuid_link
		);
	
	@Query(value = "select c from WarehouseCheck c "
			+ "where ( c.stockoutorderid_link = :stockoutorderid_link or :stockoutorderid_link is null ) "
			+ "and c.warehouseid_link = :warehouseid_link "
			+ "order by c.date_check asc"
			)
		public List<WarehouseCheck> findBy_Warehouse_StockoutOrder(
			@Param ("stockoutorderid_link")final Long stockoutorderid_link,
			@Param ("warehouseid_link")final Long warehouseid_link
			);

}
