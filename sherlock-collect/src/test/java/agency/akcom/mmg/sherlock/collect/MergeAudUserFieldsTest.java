package agency.akcom.mmg.sherlock.collect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

import agency.akcom.mmg.sherlock.collect.domain.AudUser;
@Ignore
public class MergeAudUserFieldsTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private static final CollectServlet COLLECT_SERVLET = new CollectServlet();
	private static final ObjectifyService os = new ObjectifyService();
	private com.googlecode.objectify.util.Closeable closeable;
	
	//Merge by uid
	static final JSONObject USER_1 = new JSONObject().put("uid", "uid1").put("cd107", "107").put("ua", "os1").put("__bv", "version1").put("time", "1535987245548");
	static final AudUser AUDUSER_1_V1 = new AudUser(USER_1);
	static final JSONObject USER_1_V2 = new JSONObject().put("uid", "uid1").put("cd108", "108").put("ul", "eng").put("time", "1535987246000");
	static final JSONObject RESULT_MERGE_USERS_1_AND_1_V2 = new JSONObject().put("uid", "uid1").put("cd107", "107").put("ua", "os1").put("__bv", "version1")
																			.put("cd108", "108").put("ul", "eng").put("time", "1535987246000");
	static final AudUser AUDUSER_1_V2 = new AudUser(RESULT_MERGE_USERS_1_AND_1_V2);

	//Merge by cd ID
	static final JSONObject USER_1_V3 = new JSONObject().put("cd107", "107").put("__dm", "model").put("time", "1535987247000");
	static final JSONObject RESULT_MERGE_USERS_1_AND_1_V3 = new JSONObject().put("uid", "uid1").put("cd107", "107").put("ua", "os1").put("__bv", "version1")
																			.put("cd108", "108").put("ul", "eng").put("__dm", "model").put("time", "1535987247000");
	static final AudUser AUDUSER_1_V3 = new AudUser(RESULT_MERGE_USERS_1_AND_1_V3);
	
	//Create new user
	static final JSONObject USER_2 = new JSONObject().put("uid", "uid2").put("cd109", "109").put("cd43", "gender").put("time", "1535987248000");
	
	//Create third user, merge two users by another user
	static final JSONObject USER_3 = new JSONObject().put("cd107", "107").put("cd109", "109").put("cd84", "age").put("time", "1535987249000");
	static final JSONObject RESULT_MERGE_USERS_1_AND_2_AND_3 = new JSONObject().put("uid", "uid1").put("cd107", "107").put("ua", "os1").put("__bv", "version1")
								.put("cd108", "108").put("ul", "eng").put("__dm", "model").put("cd109", "109").put("cd43", "gender").put("cd84", "age").put("time", "1535987249000");
	static final AudUser AUDUSER_3 = new AudUser(RESULT_MERGE_USERS_1_AND_2_AND_3);
	
	@Before
	public void setUp() throws ServletException {
		helper.setUp();
		COLLECT_SERVLET.init();
		closeable = ObjectifyService.begin();
		ObjectifyService.register(AudUser.class);
		
	}
	
	@After
	public void tearDown() {
		closeable.close();
		helper.tearDown();
	}
	
	public List<Entity> getEntityList() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("AudUser").addSort("uid");
		PreparedQuery pq = ds.prepare(q);
		Iterator<Entity> iter = pq.asIterator();
		List<Entity> audUserList = new ArrayList<>();

		while(iter.hasNext()) {
			Entity ent = iter.next();
			audUserList.add(ent);
		}
		return audUserList;
	}

	public Object getFieldValue(Entity entity, String key) {
		Map<String, Object> mp = entity.getProperties();
		for (Map.Entry<String, Object> entry : mp.entrySet()) {
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	@Test
	public void test() throws Exception{
		//Merge by uid
		/*put first AudUser to database and check fields*/
		AudienceService.processUIds(USER_1);
		List<Entity> entityList = getEntityList();
		AUDUSER_1_V1.setFrequency(1L);
		Assert.assertEquals(true, CompareFieldAudUsers.compareFields(AUDUSER_1_V1, entityList.get(0)));
		/*put second AudUser to database and check merge by uid*/
		AudienceService.processUIds(USER_1_V2);
		entityList = getEntityList();
		AUDUSER_1_V2.setFrequency(2L);
		Assert.assertEquals(true, CompareFieldAudUsers.compareFields(AUDUSER_1_V2, entityList.get(0)));
		
		//Merge by cd ID
		/*put third AudUser to database and check fields*/
		AudienceService.processUIds(USER_1_V3);
		AUDUSER_1_V3.setFrequency(3L);
		entityList = getEntityList();
		Assert.assertEquals(true, CompareFieldAudUsers.compareFields(AUDUSER_1_V3, entityList.get(0)));
		
		//Merge two users by another user
		AudienceService.processUIds(USER_2);
		entityList = getEntityList();
		Assert.assertEquals("count entity", 2, entityList.size());
		AudienceService.processUIds(USER_3);
		entityList = getEntityList();
		Assert.assertEquals("count entity", 1, entityList.size());
		AUDUSER_3.setFrequency(5L);
		entityList = getEntityList();
		Assert.assertEquals(true, CompareFieldAudUsers.compareFields(AUDUSER_3, entityList.get(0)));
		
	}

}
