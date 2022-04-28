package vn.gpay.jitin.core.branch;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IBranchRepository extends JpaRepository<Branch, Long>, JpaSpecificationExecutor<Branch> {
	@Query(value = "select c from Branch c where c.orgrootid_link = :orgrootid_link ")
	public List<Branch> getall_byorgrootid(@Param ("orgrootid_link")final  Long orgrootid_link);
}
