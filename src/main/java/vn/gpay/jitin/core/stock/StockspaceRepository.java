package vn.gpay.jitin.core.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface StockspaceRepository extends JpaRepository<Stockspace, Long>{
	
	@Query(value = "select c from Stockspace c where orgid_link =:orgid and spaceepc =:spaceepc")
	public List<Stockspace> findStockspaceByEpc(@Param ("orgid")final long orgid, @Param ("spaceepc")final String spaceepc);
	
	@Query(value = "select c from Stockspace c where spaceepc =:spaceepc")
	public List<Stockspace> findStockspaceByEpc(@Param ("spaceepc")final String spaceepc);
	
	@Query(value = "select a from Stockspace a "
		+ "inner join Stockrow b on a.rowid_link = b.id "
		+ "where lower(b.code) = lower(:code) "
		+ "and a.spacename = :spacename "
		+ "and a.floorid = :floorid "
		+ "and b.orgid_link = :orgid_link ")
	public List<Stockspace> findStockspaceByRowSpaceFloor(
			@Param ("code")final String code, // code = row
			@Param ("spacename")final String spacename, // spacename = space
			@Param ("floorid")final Integer floorid, // floorid = floor
			@Param ("orgid_link")final Long orgid_link
			);
	
	@Query(value = "select a from Stockspace a "
			+ "where a.rowid_link = :rowid_link "
			+ "and a.spacename = :spacename "
			+ "and a.floorid = :floorid ")
		public List<Stockspace> findStockspaceByRowIdSpaceFloor(
				@Param ("rowid_link")final Long rowid_link, // rowid_link = row
				@Param ("spacename")final String spacename, // spacename = space
				@Param ("floorid")final Integer floorid // floorid = floor
				);
	
	@Query(value = "select a from Stockspace a "
			+ "where a.rowid_link = :rowid_link "
			+ "and a.spacename = :spacename ")
		public List<Stockspace> findStockspaceByRowIdSpace(
				@Param ("rowid_link")final Long rowid_link, // rowid_link = row
				@Param ("spacename")final String spacename // spacename = space
				);
}
