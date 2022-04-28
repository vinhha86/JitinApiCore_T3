package vn.gpay.jitin.core.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class OrgServiceImpl extends AbstractService<Org> implements IOrgService{

	@Autowired
	OrgRepository repositoty;

	@Override
	protected JpaRepository<Org, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Org> findOrgByType(long root_orgid,long orgid,long type) {
		// TODO Auto-generated method stub
		//return repositoty.findOrgByType(root_orgid,orgid,type);
		Specification<Org> specification = Specifications.<Org>and()
	            .eq( "orgrootid_link", root_orgid)
	            .ne("id", orgid)
	            .eq(type!=1, "orgtypeid_link", type)
	            .ge("status", 0)
	            .build();
//		Sort sort = Sorts.builder()
//		        .desc("id")
//		        .build();
//	    return repositoty.findAll(specification,sort);
		List<Org> a = repositoty.findAll(specification);
		return a;
		
	}
	@Override
	public List<Org> findStoreByType(long root_orgid, Long orgid, long type) {
		// TODO Auto-generated method stub
		Specification<Org> specification = Specifications.<Org>and()
	            .eq( "orgrootid_link", root_orgid)
	            .eq(Objects.nonNull(orgid),"id", orgid)
	            .eq(type!=1, "orgtypeid_link", type)
	            .ge("status", 0)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
	    return repositoty.findAll(specification,sort);
	}

	@Override
	public List<Org> findOrgInvCheckByType(long orgid) {
		// TODO Auto-generated method stub
		return repositoty.findOrgInvCheckByType(orgid);
	}

	@Override
	public List<Org> findRootOrgInvCheckByType(long orgid) {
		// TODO Auto-generated method stub
		return repositoty.findRootOrgInvCheckByType(orgid);
	}

	@Override
	public List<Org> findOrgAllByRoot(long orgrootid, long orgid,
			List<String> list_typeid, boolean isincludeorg) {
		// TODO Auto-generated method stub
		 
		List<Org> list  = new ArrayList<Org>();
		Specification<Org> specification = Specifications.<Org>and()
	            .eq("orgrootid_link", orgrootid)
	            .ne(!isincludeorg, "id", orgid)
	            .in(list_typeid.size() > 0 ,"orgtypeid_link", list_typeid.toArray())
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
		list =  repositoty.findAll(specification,sort);
		if(isincludeorg)
		list = getOrgChildrenbyId(orgid, list);
		return list;
	}
	
	private List<Org> getOrgChildrenbyId(long orgid, List<Org> listAll){
		List<Org> list = new ArrayList<>();
		
		if(list.size() == 0) {
			for(Org org : listAll) {
				if(org.getId() == orgid) {
					list.add(org);
					listAll.remove(org);
					break;
				}
			}
		}
		else { 
			for(Org org : listAll) {
				check(list, org);
			}
		}
		
		return list;
	}
	
	private void check(List<Org> list, Org obj) {
		for(Org org : list) {
			if(org.getId() == obj.getParentid_link()) {
				list.add(org);
			}
		}
	}

	@Override
	public List<Org> findAllorgByTypeId(int orgtypeid_link, long orgrootid) {
		// TODO Auto-generated method stub
		return repositoty.findAllOrgbyType(orgrootid, orgtypeid_link);
	}

	@Override
	public List<OrgTree> getTreeOrg_byType(final List<Org> listorg) {
		// TODO Auto-generated method stub
		Map<Long, OrgTree> mapTmp = new HashMap<>();
		
		for (Org current : listorg) {
			OrgTree org = new OrgTree();
			org.setId(current.getId());
			org.setText(current.getName());
			org.setParentid_link(current.getParentid_link());
        	
            mapTmp.put(current.getId(), org);
        }
 
        //loop and assign parent/child relationships
        for (Org current : listorg) {
            Long parentId = current.getParentid_link();
 
            if (parentId != null ) {
            	OrgTree parent = mapTmp.get(parentId);
                if (parent != null) {
                	OrgTree current_n = new OrgTree();
                	current_n.setId(current.getId());
                	current_n.setText(current.getName());
                	current_n.setParentid_link(parentId);
                    mapTmp.put(parentId, parent);
                    mapTmp.put(current_n.getId(), current_n);
                }
            }
 
        }
        
    
        //get the root
        List<OrgTree> root = new ArrayList<OrgTree>();
        for (OrgTree node : mapTmp.values()) {
            if(node.getParentid_link()==null) {
                root.add(node);
            }
        }
 
        return root;
	}
	
	@Override
	public List<Org> findOrgByTypeForInvCheckDeviceMenuOrg() {
		return repositoty.findOrgByTypeForInvCheckDeviceMenuOrg();
	}

	@Override
	public List<Org> findOrgByTypeForMenuOrg() {
		return repositoty.findOrgByTypeForMenuOrg();
	}
	
	@Override
	public List<OrgTree> createTree(final List<Org> nodes) {
		Map<Long, OrgTree> mapTmp = new HashMap<>();
//		Long index = 0L;
		
		// Save all nodes (Org) to a map with Ids as keys, Org objs as values
		for(Org current : nodes) {
			if(current.getParentid_link()==null) {
				current.setParentid_link(-1L);
			}
			OrgTree menu = new OrgTree();
			menu.setId(current.getId());
			menu.setCode(current.getCode());
			menu.setOrgtypeid_link(current.getOrgtypeid_link());
			menu.setName(current.getName());
			menu.setCountryid_link(current.getCountryid_link());
			menu.setCity(current.getCity());
			menu.setAddress(current.getAddress());
			menu.setGpslat(current.getGpslat());
			menu.setGpslong(current.getGpslong());
			menu.setMainbizid_link(current.getMainbizid_link());
			menu.setTimezone(current.getTimezone());
			menu.setContactperson(current.getContactperson());
			menu.setEmail(current.getEmail());
			menu.setLangid_link(current.getLangid_link());
			menu.setStatus(current.getStatus());
			menu.setParentid_link(current.getParentid_link());
			menu.setPhone(current.getPhone());
			menu.setOrgrootid_link(current.getOrgrootid_link());
//			menu.setColorid_link(current.getColorid_link());
//			menu.setLinecost(current.getLinecost());
//			menu.setCostpersec(current.getCostpersec());
//			menu.setIs_manufacturer(current.getIs_manufacturer());
			menu.setExpanded(true);
			menu.setChecked(current.checked);
			mapTmp.put(current.getId(), menu);
			
			
		}
		
		//loop and assign parent/child relationships
		for(Org current : nodes) {
			if(current.getParentid_link()==null){
				current.setParentid_link(-1L);
			}
			Long parentId = current.getParentid_link();
			
			// if == -1 >> no parent
			if(parentId != -1) {
				OrgTree parent = mapTmp.get(parentId);
				if(parent != null) {
					OrgTree current_n = new OrgTree();
					current_n.setId(current.getId());
					current_n.setCode(current.getCode());
					current_n.setOrgtypeid_link(current.getOrgtypeid_link());
					current_n.setName(current.getName());
					current_n.setCountryid_link(current.getCountryid_link());
					current_n.setCity(current.getCity());
					current_n.setAddress(current.getAddress());
					current_n.setGpslat(current.getGpslat());
					current_n.setGpslong(current.getGpslong());
					current_n.setMainbizid_link(current.getMainbizid_link());
					current_n.setTimezone(current.getTimezone());
					current_n.setContactperson(current.getContactperson());
					current_n.setEmail(current.getEmail());
					current_n.setLangid_link(current.getLangid_link());
					current_n.setStatus(current.getStatus());
					current_n.setParentid_link(current.getParentid_link());
					current_n.setPhone(current.getPhone());
					current_n.setOrgrootid_link(current.getOrgrootid_link());
//					current_n.setColorid_link(current.getColorid_link());
//					current_n.setLinecost(current.getLinecost());
//					current_n.setCostpersec(current.getCostpersec());
//					current_n.setIs_manufacturer(current.getIs_manufacturer());
					current_n.setChecked(current.checked);
					parent.addChild(current_n);
					parent.setExpanded(false);
					mapTmp.put(parentId, parent);
					mapTmp.put(current_n.getId(), current_n);
				}
			}
			
		}
		
		// get the root
		List<OrgTree> root = new ArrayList<OrgTree>();
		
		for(OrgTree node : mapTmp.values()) {
			if(node.getParentid_link() == -1) {
				root.add(node);
				node.setExpanded(true);
			}
		}
		return root;
	}

	@Override
	public List<Org> getorgChildrenbyOrg(long orgid_link, List<String> list_typeid) {
		// TODO Auto-generated method stub
		Specification<Org> specification = Specifications.<Org>and()
	            .eq("parentid_link", orgid_link)
	            .ge("status", 0)
	            .in(list_typeid.size() > 0,"orgtypeid_link", list_typeid.toArray())
	            .build();
		Sort sort = Sorts.builder()
		        .asc("id")
		        .build();
		List<Org> list =  repositoty.findAll(specification,sort);
		
		
		return list;
	}

	@Override
	public List<Org> findOrgByParentAndType(Integer orgtypeid_link, Long parentid_link) {
		// TODO Auto-generated method stub
		return repositoty.findOrgByParentAndType(orgtypeid_link, parentid_link);
	}
	
}
