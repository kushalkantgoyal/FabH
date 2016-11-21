package kkg.fabH.DAO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceException;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import kkg.fabH.Entity.BaseEntity;


/**
 * CommonDao is an abstract class and it perform common CRUD operation and
 * all DAO need to extend this class
 * 
 * @author Kushal
 * 
 * @param <E>
 */
public abstract class CommonDAO<E>
{

    @PersistenceContext (type = PersistenceContextType.TRANSACTION)
    protected EntityManager entityManager;
    private final Logger logger = Logger.getLogger(getClass());
    
    protected CriteriaBuilder builder;
    protected CriteriaQuery<E> criteria;
    protected Root<E> root;
    protected Root<E> rootUpdate;
    private Class<E> modelClass; // use this with getModelClass() api only not direct
    
    /**
     * Initialize common attributes
     * This method must be called in every DAOImpl if that class is using the common attributes provided here in this class.
     */
    protected void initCommonAttributes(){
    	this.modelClass = getModelClass();
    	builder = entityManager.getCriteriaBuilder();
        criteria = builder.createQuery(getModelClass());
        root = criteria.from(getModelClass());          
        criteria.select(root);        
    }
    
    /**
     * This method is used to provide the template type class used in the DAOImpl 
     * @return
     */
    protected abstract Class<E> getModelClass();
	 
    /*
     * Method to persist entity to database (non-Javadoc)
     * 
     */
    public E create(E entity) throws Exception
    {
    	try{
    		entityManager.persist(entity);
    	}catch(PersistenceException pe){
    		logger.error("Unable to persist entity :"+entity+" in DB", pe);
    		throw new Exception(pe);
    	}
        
        return entity;
    }
  
    /*
     * Update state of entity (non-Javadoc)
     * 
     */
    public E update(E entity) throws Exception
    {
    	E create;
    	try{
    		create = create(entityManager.merge(entity));
    	}catch(PersistenceException pe){
    		logger.error("Unable to update entity :"+entity+" in DB", pe);
    		throw new Exception(pe);
    	}
        return create;
    }

    /*
     * Delete entity from database (non-Javadoc)
     * 
     */
    public boolean remove(E entity) throws Exception
    {
    	try{
        entityManager.remove(entity);
    	}catch(PersistenceException pe){
    		logger.error("Unable to remove entity :"+entity+" from DB", pe);
    		throw new Exception(pe);
    	}
        return true;
    }

    /*
     * get entity from database(non-Javadoc)
     * 
     */
    public E getById(Long id) throws Exception
    {
    	E entity;
    	try{
        entity = entityManager.find(getModelClass(), id);
    	}catch(IllegalArgumentException ile){
    		logger.error("Unable to get entity from id:"+id, ile);
    		throw new Exception(ile);
    	}
        return entity;
    }
    
    public List<E> getAll() throws Exception
    {
    	initCommonAttributes();
    	return getResults();
    }
    
    
	
	 /*Get Single Result
		 * @return 
		 * */
    protected E getSingleResult() throws Exception{
    	E entity = null;
    	try{
		entity = entityManager.createQuery(criteria).getSingleResult();
    	}catch(NoResultException e){
    		logger.error("There is no result for entity");    		
    	}
    	catch(Exception e){
    		logger.error("Unable to get Single result ", e);
    		throw new Exception(e);
    	}
		return entity;
	}
		
	 /*Get Result List without paginated
	 * @return 
	 * */
	protected List<E> getResults() throws Exception{
		List<E> resultList;
		try{
		resultList = entityManager.createQuery(criteria).getResultList();
		}catch(Exception ile){
    		logger.error("Unable to get Results ", ile);
    		throw new Exception(ile);
    	}
		if(resultList == null) {
			return new ArrayList<E>();
		}
		return resultList;
	}
	
	/*
	 * Gives List of rows limited to no given as parameter.
	 */
	protected List<E> getResults(int noOfRows) throws Exception{
		List<E> resultList;
		try{
		resultList = entityManager.createQuery(criteria).setMaxResults(noOfRows).getResultList();
		}catch(Exception ile){
    		logger.error("Unable to get Results ", ile);
    		throw new Exception(ile);
    	}
		if(resultList == null) {
			return new ArrayList<E>();
		}
		return resultList;
	}
	
	/*
	 * Gives first row from the result set.
	 */
	protected E getFirstResult() throws Exception {
		List<E> results = getResults(1);
		if(CollectionUtils.isNotEmpty(results)) {
			return results.get(0);
		}
		return null;
	}
    
	public void setEntityManager(EntityManager entityManager) {  
	    this.entityManager = entityManager ;
	}
	
	protected void executeNativeUpdate(String query){
		entityManager.createNativeQuery(query).executeUpdate();
	}
	
	protected List<E> executeNativeFetch(String query){
		return entityManager.createNativeQuery(query, getModelClass()).getResultList();
	}	
	
	protected String getTableName(){
		return getModelClass().getAnnotation(Table.class).name();
	}
	
	/**
	 * Can be used when you want to fetch multiple rows based on some column
	 * @param criteriaColumn column on which you want to put IN query.
	 * @param values list of values you want to provide for IN query
	 * @param columns column names which you want to select. Actually it is property name of entity.
	 * @throws Exception 
	 */
	protected List<E> executeINFetch(String criteriaColumn, Set<Long> values, String ... columns) throws Exception {
		if(CollectionUtils.isEmpty(values)) {
			return Collections.EMPTY_LIST;
		}
		Expression<Long> expression = root.get(criteriaColumn);
		Predicate predicate = expression.in(values);
		for(String column: columns) {
			criteria.multiselect(root.get(column));
		}
		criteria.where(predicate);
		return getResults();
	}
	
	protected Set<Long> getIdsFromEntities(Collection<E> entities) {
		Set<Long> ids = new HashSet<Long>();
		for(E e: entities) {
			ids.add(((BaseEntity)e).getId());
		}
		return ids;
	}
	
	public void flush() {
		try {
			entityManager.flush();
		} catch (Exception e) {
			logger.error("Unable to flush the db at this time: %s", e);
		}		
	}
	
	public void clear() {
		try {
			entityManager.clear();
		} catch (Exception e) {
			logger.error("Unable to clear the sesion at this time: %s", e);
		}
	}
	
	public void hardFlush() {
		try {			
			flush();
			clear();
			//entityManager.getTransaction().commit();
			//entityManager.getTransaction().begin();
			
		} catch (Exception e) {
			logger.error("Unable to flush the db at this time: %s", e);
		}	
	}
}