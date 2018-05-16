package agency.akcom.mmg.sherlock.ui.server.dao;

import static agency.akcom.mmg.sherlock.ui.server.dao.objectify.OfyService.ofy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.api.client.util.Lists;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import agency.akcom.mmg.sherlock.ui.shared.TooManyResultsException;

public abstract class BaseDao<T> {
	private final Class<T> clazz;

	protected BaseDao(final Class<T> clazz) {
		this.clazz = clazz;
	}

	public List<T> listAll() {
		return ofy().load().type(clazz).list();
	}

	public void save(T entity) {
		saveAndReturn(entity);
	}

	public T saveAndReturn(T entity) {
		ofy().save().entity(entity).now();
		return entity;
	}

	public Key<T> saveAndReturnKey(T entity) {
		return ofy().save().entity(entity).now();
	}

	public Collection<T> saveAndReturn(Iterable<T> entities) {
		return ofy().save().entities(entities).now().values();
	}

	public T get(Key<T> key) {
		return ofy().load().key(key).now();
	}

	public T get(Long id) {
		// TODO probably it could be fixed by parameters of
		// work around for objectify cacheing and new query not having the
		// latest
		// data
		ofy().clear();

		return ofy().load().type(clazz).id(id).now();
	}

	public T getByProperty(String propName, Object propValue) throws TooManyResultsException {
		Query<T> q = ofy().load().type(clazz);
		q = q.filter(propName, propValue);
		Iterator<T> fetch = q.limit(2).list().iterator();
		if (!fetch.hasNext()) {
			return null;
		}
		T obj = fetch.next();
		if (fetch.hasNext()) {
			throw new TooManyResultsException();
		}
		return obj;
	}

	public Boolean exists(Key<T> key) {
		return get(key) != null;
	}

	public Boolean exists(Long id) {
		return get(id) != null;
	}

	public List<T> getSubset(List<Long> ids) {
		return new ArrayList<T>(ofy().load().type(clazz).ids(ids).values());
	}

	public Map<Long, T> getSubsetMap(List<Long> ids) {
		return new HashMap<Long, T>(ofy().load().type(clazz).ids(ids));
	}

	public void delete(T object) {
		ofy().delete().entity(object);
	}

	public void delete(Long id) {
		ofy().delete().type(clazz).id(id);
	}

	public void delete(List<T> objects) {
		ofy().delete().entities(objects);
	}

	public List<T> get(List<Key<T>> keys) {
		// TODO why guava used?
		return Lists.newArrayList(ofy().load().keys(keys).values());
	}

	protected LoadType<T> query() {
		return ofy().load().type(clazz);
	}
	
	public T loadUserByEmail (String email) {
		return ofy().load().type(clazz).filter("email", email).first().now();
	}
	
	public T loadUserById (Long id) {
		return ofy().load().type(clazz).id(id).now();
	}
}
