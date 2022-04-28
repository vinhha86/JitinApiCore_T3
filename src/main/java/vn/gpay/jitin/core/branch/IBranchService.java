package vn.gpay.jitin.core.branch;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IBranchService extends Operations<Branch> {
	public List<Branch> getall_byorgrootid(long orgrootid_link);
}
