package vn.gpay.jitin.core.org;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IOrgService extends Operations<Org>{

	public List<Org> findOrgByType(long root_orgid ,long orgid,long type);
	
	public List<Org> findStoreByType(long root_orgid,Long orgid,long type);
	
	public List<Org> findOrgInvCheckByType(long orgid);
	
	public List<Org> findRootOrgInvCheckByType(long orgid);
	
	public List<Org> findOrgAllByRoot(long orgrootid, long orgid, List<String> list_typeid, boolean isincludeorg);
	
	public List<Org> findAllorgByTypeId(int orgtypeid_link, long orgrootid);
	
	public List<OrgTree> getTreeOrg_byType(List<Org> listorg);
	
	public List<OrgTree> createTree( List<Org> nodes);
	
	public List<Org> findOrgByTypeForMenuOrg();
	
	public List<Org> findOrgByTypeForInvCheckDeviceMenuOrg();
	
	public List<Org> getorgChildrenbyOrg(long orgid_link, List<String> list_typeid);
	
	public List<Org> findOrgByParentAndType(Integer orgtypeid_link, Long parentid_link);
}
