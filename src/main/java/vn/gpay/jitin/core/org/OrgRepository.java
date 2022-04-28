package vn.gpay.jitin.core.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OrgRepository extends JpaRepository<Org, Long>,JpaSpecificationExecutor<Org>{

	@Query(value = "select c from Org c where c.orgrootid_link=:orgrootid_link"
			+ " and orgtypeid_link =:type "
			+ "and id <> :orgid_link ")
	public List<Org> findOrgByType(@Param ("orgrootid_link")final long orgrootid_link,
			@Param ("orgid_link")final long orgid_link
			,@Param ("type")final long type);
	
	@Query(value = "select c from Org c where c.id =:orgid_link and c.orgtypeid_link in(3,4,8)")
	public List<Org> findOrgInvCheckByType(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(3,4,8) and c.orgrootid_link =:orgid_link")
	public List<Org> findRootOrgInvCheckByType(@Param ("orgid_link")final long orgid_link);
	
	@Query(value = "select c from Org c where c.orgrootid_link =:orgrootid_link "
			+ "and ((:orgid = CAST(0 as long)) or (:orgid <> id ))")
	public List<Org> findOrgAllByRoot(@Param ("orgrootid_link")final long orgrootid_link,
			@Param ("orgid")final Long orgid);
	
	@Query(value = "select c from Org c where c.orgrootid_link =:orgrootid "
			+ "and orgtypeid_link = :orgtypeid_link and c.status = 1")
	public List<Org> findAllOrgbyType(@Param ("orgrootid")final long orgrootid,
			@Param ("orgtypeid_link")final Integer orgtypeid_link);
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(1,4,13,14,17) order by c.orgtypeid_link, c.name asc")
	public List<Org> findOrgByTypeForMenuOrg();
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(1,4,13,14,17) order by c.orgtypeid_link, c.name asc")
	public List<Org> findOrgByTypeForInvCheckDeviceMenuOrg();
	
	@Query(value = "select c from Org c where c.orgtypeid_link in(3,8,19) order by c.orgtypeid_link, c.name asc")
	public List<Org> findOrgByTypeKho();
	
	@Query(value = "select c from Org c "
			+ "where c.orgtypeid_link =:orgtypeid_link "
			+ "and c.parentid_link = :parentid_link "
			+ "and c.status = 1")
	public List<Org> findOrgByParentAndType(
			@Param ("orgtypeid_link")final Integer orgtypeid_link,
			@Param ("parentid_link")final Long parentid_link
			);
}
