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

import agency.akcom.mmg.sherlock.collect.audience.AudUserChild;
import agency.akcom.mmg.sherlock.collect.audience.Demography;
import agency.akcom.mmg.sherlock.collect.audience.Geography;
import agency.akcom.mmg.sherlock.collect.dao.AudUserDao;
import agency.akcom.mmg.sherlock.collect.domain.AudUser;
import agency.akcom.mmg.sherlock.collect.domain.BackupAudUser;

//@Ignore
public class MergeAudUserFieldsWithDifferentFieldsTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private static final CollectServlet COLLECT_SERVLET = new CollectServlet();
	private static final ObjectifyService os = new ObjectifyService();
	private com.googlecode.objectify.util.Closeable closeable;

	// field is different. AudUser is defined by uid
	static final JSONObject USER_1 = new JSONObject().put("uid", "uid1").put("cd107", "107").put("ua", "os1")
			.put("__bv", "version1").put("cd123", "ispostpaid").put("cd108", "108").put("ul", "eng")
			.put("__dm", "model").put("cd109", "109").put("cd43", "gender").put("cd84", "age")
			.put("time", "1535987249000").put("last_uid", "92762f5b-7837-338f-b72f-396e7061be33");;
	static AudUser AUDUSER_1_V1;

	static final JSONObject USER_1_V2 = new JSONObject().put("uid", "uid1").put("ul", "swe").put("time",
			"1535987250000");

	static final AudUser AUDUSER_BACKUP_1 = AUDUSER_1_V1;
	static final JSONObject USER_1_MERGE = new JSONObject().put("uid", "uid1").put("cd107", "107").put("ua", "os1")
			.put("__bv", "version1").put("cd123", "ispostpaid").put("cd108", "108").put("ul", "swe")
			.put("__dm", "model").put("cd109", "109").put("cd43", "gender").put("cd84", "age")
			.put("time", "1535987250000").put("last_uid", "92762f5b-7837-338f-b72f-396e7061be33");;
	static AudUser AUDUSER_1_MERGE;

	// field is different. AudUser is defined by cd
	static final AudUser AUDUSER_BACKUP_1_V2 = AUDUSER_1_MERGE;
	static final JSONObject USER_1_V3 = new JSONObject().put("cd109", "109").put("cd123", "ispostpaid_v2").put("time",
			"1535987260000").put("last_uid", "92762f5b-7837-338f-b72f-396e7061be33");;

	static final JSONObject USER_1_MERGE_V2 = new JSONObject().put("uid", "uid1").put("cd107", "107").put("ua", "os1")
			.put("__bv", "version1").put("cd123", "ispostpaid_v2").put("cd108", "108").put("ul", "swe")
			.put("__dm", "model").put("cd109", "109").put("cd43", "gender").put("cd84", "age")
			.put("time", "1535987260000").put("last_uid", "92762f5b-7837-338f-b72f-396e7061be33");;
	static AudUser AUDUSER_1_MERGE_V2;

	@Before
	public void setUp() throws ServletException {
		helper.setUp();
		COLLECT_SERVLET.init();
		closeable = ObjectifyService.begin();
		ObjectifyService.register(AudUser.class);
		ObjectifyService.register(BackupAudUser.class);
		ObjectifyService.register(AudUserChild.class);
		ObjectifyService.register(Geography.class);
		ObjectifyService.register(Demography.class);
		
		AUDUSER_1_V1 = new AudUser(USER_1);
		AUDUSER_1_MERGE = new AudUser(USER_1_MERGE);
		AUDUSER_1_MERGE_V2 = new AudUser(USER_1_MERGE_V2);
	}

	@After
	public void tearDown() {
		closeable.close();
		helper.tearDown();
	}

	public List<Entity> getEntityList(String query) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(query).addSort("uid");
		PreparedQuery pq = ds.prepare(q);
		Iterator<Entity> iter = pq.asIterator();
		List<Entity> audUserList = new ArrayList<>();

		while (iter.hasNext()) {
			Entity ent = iter.next();
			audUserList.add(ent);
		}
		return audUserList;
	}
	
	public List<AudUser> getAudUserList(){
		AudUserDao dao = new AudUserDao();
		List<AudUser> list = dao.listAll();
		return list;
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
	public void test() throws Exception {
		// field is different. AudUser is defined by uid
		/* put first AudUser to database and check fields */
		AudienceService.processUIds(USER_1);
		List<AudUser> entityList = getAudUserList();
		AUDUSER_1_V1.setFrequency(1L);
		Assert.assertEquals(true, CompareFieldAudUsers.compareFields(AUDUSER_1_V1, entityList.get(0)));
		/* put second AudUser to database and check merge by uid */
		AudienceService.processUIds(USER_1_V2);
		entityList = getAudUserList();
		Assert.assertEquals("count entity", 1, entityList.size());
		AUDUSER_1_MERGE.setFrequency(2L);
		Assert.assertEquals(true, CompareFieldAudUsers.compareFields(AUDUSER_1_MERGE, entityList.get(0)));
//		List<Entity> backupList = getEntityList("BackupAudUser");
//		Assert.assertEquals("compare backup", true,	CompareFieldAudUsers.compareBackUpFields(AUDUSER_BACKUP_1, backupList.get(0).getProperty("backupAudUser")));
//		Assert.assertEquals("compare replacedAudUser in backup", true,	CompareFieldAudUsers.compareBackUpFields(AUDUSER_1_MERGE, backupList.get(0).getProperty("replacedAudUser")));

		// field is different. AudUser is defined by cd
		AudienceService.processUIds(USER_1_V3);
		entityList = getAudUserList();
		Assert.assertEquals("count entity", 1, entityList.size());
		AUDUSER_1_MERGE_V2.setFrequency(3L);
		Assert.assertEquals(true, CompareFieldAudUsers.compareFields(AUDUSER_1_MERGE_V2, entityList.get(0)));
//		backupList = getEntityList("BackupAudUser");
//		Assert.assertEquals("count backup", 2, backupList.size());
//		Assert.assertEquals("compare backup", true,	CompareFieldAudUsers.compareBackUpFields(AUDUSER_BACKUP_1, backupList.get(0).getProperty("backupAudUser")));
//		Assert.assertEquals("compare backup", true,	CompareFieldAudUsers.compareBackUpFields(AUDUSER_1_MERGE, backupList.get(0).getProperty("replacedAudUser")));
//		Assert.assertEquals("compare backup", true,	CompareFieldAudUsers.compareBackUpFields(AUDUSER_BACKUP_1_V2, backupList.get(1).getProperty("backupAudUser")));
//		Assert.assertEquals("compare backup", true,	CompareFieldAudUsers.compareBackUpFields(AUDUSER_1_MERGE_V2, backupList.get(1).getProperty("replacedAudUser")));

	}

}
