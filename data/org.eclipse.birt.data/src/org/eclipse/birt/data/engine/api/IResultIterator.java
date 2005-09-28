/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;

/**
 * An iterator on a result set from a prepared and executed query.
 * Multiple <code>IResultIterator</code> objects could be associated with
 * the same <code>IQueryResults</code> object, such as in the case of an ODA data set
 * capable of producing multiple result sets.
 */
public interface IResultIterator
{
    /**
     * Returns the {@link org.eclipse.birt.data.engine.api.IQueryResults} 
     * from which this result iterator is obtained. If this iterator
     * is that of a subquery, null is returned. 
     */
    public IQueryResults getQueryResults();

    /**
     * Returns the JavaScript scope associated with this result iterator.
     * All JavaScript result objects, e.g. rows, row, specific to this
     * result set are defined within this scope.    
     * The returned scope is the same as the one passed to <code>IPreparedQuery.excute()</code>
     * which produced this iterator's <code>IQueryResults</code>.
     * @return	The JavaScript scope associated to this result iterator.
     */
    public Scriptable getScope();

    /**
     * Returns the metadata of this result set's detail row.
     * @return	The result metadata of a detail row.
     */
    public IResultMetaData getResultMetaData() throws BirtException;
    
    /**
     * Moves down one element from its current position of the iterator.
     * This method applies to a result whose ReportQuery is defined to 
     * use detail or group rows. 
     * @return 	true if next element exists and 
     * 			has not reached the limit on the maximum number of rows 
     * 			that can be accessed. 
     * @throws 	BirtException if error occurs in Data Engine
     */
    public boolean next() throws BirtException;

    /**
     * Returns the value of a query result expression. 
     * A given data expression could be for one of the Selected Columns
     * (if detail rows are used), or of an Aggregation specified 
     * in the prepared ReportQueryDefn spec.
     * When requesting for the value of a Selected Column, its value
     * in the current row of the iterator will be returned.
     * <p>
     * Throws an exception if a result expression value is requested 
     * out of sequence from the prepared <code>IQueryDefinition</code> spec.  
     * E.g. A group aggregation is defined to be after_last_row. 
     * It would be out of sequence if requested before having
     * iterated/skipped to the last row of the group. 
     * In future release, this could have intelligence to auto recover 
     * and performs dependent operations to properly evaluate 
     * any out-of-sequence result values. 
     * @param dataExpr 	An <code>IBaseExpression</code> object provided in
     * 					the <code>IQueryDefinition</code> at the time of prepare.
     * @return			The value of the given expression.
     * 					It could be null.
     * @throws 			BirtException if error occurs in Data Engine
     */
    public Object getValue( IBaseExpression dataExpr ) throws BirtException;

    /**
     * Returns the value of a query result expression as a Boolean,
     * by type casting the Object returned by getValue.
     * <br>
     * A convenience method for the API consumer.
     * <br>
     * If the expression value has an incompatible type,  
     * a <code>DataException</code> is thrown at runtime.
     * @param dataExpr 	An <code>IBaseExpression</code> object provided in
     * 					the <code>IQueryDefinition</code> at the time of prepare.
     * @return			The value of the given expression as a Boolean.
     * 					It could be null.
     * @throws 			BirtException if error occurs in Data Engine
     */
    public Boolean getBoolean( IBaseExpression dataExpr ) throws BirtException;

    /**
     * Returns the value of a query result expression as an Integer,
     * by type casting the Object returned by getValue.
     * <br>
     * A convenience method for the API consumer.
     * <br>
     * If the expression value has an incompatible type,  
     * a <code>DataException</code> is thrown at runtime.
     * @param dataExpr 	An <code>IBaseExpression</code> object provided in
     * 					the <code>IQueryDefinition</code> at the time of prepare.
     * @return			The value of the given expression as an Integer.
     * 					It could be null.
     * @throws 			BirtException if error occurs in Data Engine
     */
    public Integer getInteger( IBaseExpression dataExpr ) throws BirtException;

    /**
     * Returns the value of a query result expression as a Double,
     * by type casting the Object returned by getValue.
     * <br>
     * A convenience method for the API consumer.
     * <br>
     * If the expression value has an incompatible type,  
     * a <code>DataException</code> is thrown at runtime.
     * @param dataExpr 	An <code>IBaseExpression</code> object provided in
     * 					the <code>IQueryDefinition</code> at the time of prepare.
     * @return			The value of the given expression as a Double.
     * 					It could be null.
     * @throws 			BirtException if error occurs in Data Engine
     */
    public Double getDouble( IBaseExpression dataExpr ) throws BirtException;

    /**
     * Returns the value of a query result expression as a String,
     * by type casting the Object returned by getValue.
     * <br>
     * A convenience method for the API consumer.
     * <br>
     * If the expression value has an incompatible type,  
     * a <code>DataException</code> is thrown at runtime.
     * @param dataExpr 	An <code>IBaseExpression</code> object provided in
     * 					the <code>IQueryDefinition</code> at the time of prepare.
     * @return			The value of the given expression as a String.
     * 					It could be null.
     * @throws 			BirtException if error occurs in Data Engine
     */
    public String getString( IBaseExpression dataExpr ) throws BirtException;

    /**
     * Returns the value of a query result expression as a BigDecimal,
     * by type casting the Object returned by getValue.
     * <br>
     * A convenience method for the API consumer.
     * <br>
     * If the expression value has an incompatible type,  
     * a <code>DataException</code> is thrown at runtime.
     * @param dataExpr 	An <code>IBaseExpression</code> object provided in
     * 					the <code>IQueryDefinition</code> at the time of prepare.
     * @return			The value of the given expression as a BigDecimal.
     * 					It could be null.
     * @throws 			BirtException if error occurs in Data Engine
     */
    public BigDecimal getBigDecimal( IBaseExpression dataExpr ) throws BirtException;

    /**
     * Returns the value of a query result expression as a Date,
     * by type casting the Object returned by getValue.
     * <br>
     * A convenience method for the API consumer.
     * <br>
     * If the expression value has an incompatible type,  
     * a <code>DataException</code> is thrown at runtime.
     * @param dataExpr 	An <code>IBaseExpression</code> object provided in
     * 					the <code>IQueryDefinition</code> at the time of prepare.
     * @return			The value of the given expression as a Date.
     * 					It could be null.
     * @throws 			BirtException if error occurs in Data Engine
     */
    public Date getDate( IBaseExpression dataExpr ) throws BirtException;

    /**
     * Returns the value of a query result expression 
     * representing Blob data.
     * <br>
     * If the expression value has an incompatible type,  
     * a <code>DataException</code> is thrown at runtime.
     * @param dataExpr 	An <code>IBaseExpression</code> object provided in
     * 					the <code>IQueryDefinition</code> at the time of prepare.
     * @return			The value of the given Blob expression.
     * 					It could be null.
     * @throws 			BirtException if error occurs in Data Engine
     */
    public Blob getBlob( IBaseExpression dataExpr ) throws BirtException;

    /**
     * Advances the iterator, skipping rows to the last row in the current group 
     * at the specified group level.
     * This is for result sets that do not use detail rows to advance
     * to next group.  Calling next() after skip() would position 
     * the current row to the first row of the next group.
     * @param groupLevel	An absolute value for group level. 
     * 						A value of 0 applies to the whole result set.
     * @throws 			BirtException if error occurs in Data Engine
     */
    public void skipToEnd( int groupLevel ) throws BirtException;

    /**
     * Returns the 1-based index of the outermost group
     * in which the current row is the first row. 
     * For example, if a query contain N groups 
     * (group with index 1 being the outermost group, and group with 
     * index N being the innermost group),
     * and this function returns a value M, it indicates that the 
     * current row is the first row in groups with 
     * indexes (M, M+1, ..., N ).
     * @return	1-based index of the outermost group in which 
     * 			the current row is the first row;
     * 			(N+1) if the current row is not at the start of any group;
     * 			0 if the result set has no groups.
     */
    public int getStartingGroupLevel() throws BirtException;

    /**
     * Returns the 1-based index of the outermost group
     * in which the current row is the last row. 
     * For example, if a query contain N groups 
     * (group with index 1 being the outermost group, and group with 
     * index N being the innermost group),
     * and this function returns a value M, it indicates that the 
     * current row is the last row in groups with 
     * indexes (M, M+1, ..., N ). 
     * @return	1-based index of the outermost group in which 
     * 			the current row is the last row;
     * 			(N+1) if the current row is not at the end of any group;
     * 			0 if the result set has no groups.
     */
    public int getEndingGroupLevel() throws BirtException;

    /**
     * Returns the secondary result specified by a sub query 
     * that was defined in the prepared <code>IQueryDefinition</code>.
     * @throws 			DataException if error occurs in Data Engine
     * @param subQueryName name of sub query which defines the secondary result set
     * @param scope Javascript scope to be associated with the secondary result set
     */
    public IResultIterator getSecondaryIterator( String subQueryName, Scriptable scope ) 
    		throws BirtException;

    /** 
     * Closes this result and any associated secondary result iterator(s),  
     * providing a hint that the consumer is done with this result,
     * whose resources can be safely released as appropriate.  
     */
    public void close();
}