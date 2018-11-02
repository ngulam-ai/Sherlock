package agency.akcom.mmg.sherlock.collect;

import java.util.Iterator;
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
public class MergeAudUsersByIDsFieldsTest {
	
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	private static final CollectServlet COLLECT_SERVLET = new CollectServlet();
	private static final ObjectifyService os = new ObjectifyService();
	private com.googlecode.objectify.util.Closeable closeable;
	
	static final JSONObject USER_1 = new JSONObject().put("uid", "uid1").put("cd107", "107").put("time", "1535987245548");
	static final JSONObject USER_2 = new JSONObject().put("uid", "uid2").put("cd108", "108").put("time", "1535987245549");
	static final JSONObject USER_3 = new JSONObject().put("cd107", "107").put("cd108", "108").put("time", "1535987245550");
	static final JSONObject USER_4 = new JSONObject().put("cd107", "107").put("cd109", "109").put("time", "1535987245551");
	static final JSONObject USER_5 = new JSONObject().put("uid", "uid3").put("cd107", "another").put("cd109", "109").put("time", "1535987245552");

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

	public Iterator<Entity> getEntityList() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("AudUser").addSort("uid");
		PreparedQuery pq = ds.prepare(q);
		return pq.asIterator();
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

	public int getTotalAudUsers(Iterator<Entity> iter) {
		int count = 0;
		while (iter.hasNext()) {
			iter.next();
			count++;
		}
		return count;
	}

	@Test
	public void test() throws Exception {
		/* put first AudUser to database and check ID fields */
		AudienceService.processUIds(USER_1);
		Iterator<Entity> entityList = getEntityList();
		Entity ent = entityList.next();
		Assert.assertEquals("uid1", getFieldValue(ent, "uid"));
		Assert.assertEquals("107", getFieldValue(ent, "mparticleuserid"));

		/* put second different AudUser to database and check ID fields */
		AudienceService.processUIds(USER_2);
		entityList = getEntityList();
		Assert.assertEquals("total audUsers in DS", 2, getTotalAudUsers(entityList));
		entityList = getEntityList();
		ent = entityList.next();
		Assert.assertEquals("uid1", getFieldValue(ent, "uid"));
		Assert.assertEquals("107", getFieldValue(ent, "mparticleuserid"));
		ent = entityList.next();
		Assert.assertEquals("uid2", getFieldValue(ent, "uid"));
		Assert.assertEquals("108", getFieldValue(ent, "customerid"));

		/*
		 * put third different AudUser, he must merge previously two to one in database
		 */
		AudienceService.processUIds(USER_3);
		entityList = getEntityList();
		entityList = getEntityList();
		Assert.assertEquals("total audUsers in DS", 1, getTotalAudUsers(entityList));
		entityList = getEntityList();
		ent = entityList.next();
		Assert.assertEquals("uid1", getFieldValue(ent, "uid"));
		Assert.assertEquals("107", getFieldValue(ent, "mparticleuserid"));
		Assert.assertEquals("108", getFieldValue(ent, "customerid"));

		/* put same AudUser but has new field */
		AudienceService.processUIds(USER_4);
		entityList = getEntityList();
		Assert.assertEquals("total audUsers in DS", 1, getTotalAudUsers(entityList));
		entityList = getEntityList();
		ent = entityList.next();
		Assert.assertEquals("uid1", getFieldValue(ent, "uid"));
		Assert.assertEquals("107", getFieldValue(ent, "mparticleuserid"));
		Assert.assertEquals("108", getFieldValue(ent, "customerid"));
		Assert.assertEquals("109", getFieldValue(ent, "facebookid"));

		/* put AudUser has conflict ID */
		AudienceService.processUIds(USER_5);
		entityList = getEntityList();
		Assert.assertEquals("total audUsers in DS", 2, getTotalAudUsers(entityList));
		entityList = getEntityList();
		ent = entityList.next();
		Assert.assertEquals("uid1", getFieldValue(ent, "uid"));
		Assert.assertEquals("107", getFieldValue(ent, "mparticleuserid"));
		Assert.assertEquals("108", getFieldValue(ent, "customerid"));
		Assert.assertEquals("109", getFieldValue(ent, "facebookid"));
		ent = entityList.next();
		Assert.assertEquals("uid1", getFieldValue(ent, "uid"));
		Assert.assertEquals("for cd107 field", "another", getFieldValue(ent, "mparticleuserid"));
		Assert.assertEquals("109", getFieldValue(ent, "facebookid"));

	}
}
