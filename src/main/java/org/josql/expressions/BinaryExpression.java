/*
 * Copyright 2004-2007 Gary Bentley 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.josql.expressions;

import org.josql.Query;
import org.josql.exceptions.QueryExecutionException;
import org.josql.exceptions.QueryParseException;

/**
 * Super-class of Expressions that return a binary result.
 * <p>
 * A binary expression must always have a LHS.  The RHS is optional.
 */
public abstract class BinaryExpression extends Expression
{

    protected Expression left = null;
    protected Expression right = null;

    /**
     * Return whether this expression, and more specifically the left and right parts of
     * the expression return a fixed result.
     * Sub-classes may override this method for more tailored results, especially if the
     * binary expression does not demand a RHS.
     *
     * @param q The Query object.
     * @return <code>true<code> if the expression has a fixed result.
     */
    @Override
	public boolean hasFixedResult (final Query q)
    {

	boolean fr = true;

	if (right != null)
	{

	    fr = right.hasFixedResult (q);

	}

	return left.hasFixedResult (q) && fr;

    }

    /**
     * Return the expected return type from this expression.
     * 
     * @param q The Query object.
     * @return The class of the return type, this method ALWAYS returns <code>Boolean.class</code>.
     */
    @Override
	public Class getExpectedReturnType (final Query  q)
    {

	return Boolean.class;

    }

    /**
     * Init the expression.  Sub-classes will often override this method.
     * This method just calls: {@link Expression#init(Query)} on the LHS and RHS (if present) of the
     * expression.  
     * 
     * @param q The Query object.
     * @throws QueryParseException If the LHS and/or RHS cannot be inited.
     */
    @Override
	public void init (final Query  q)
	              throws QueryParseException
    {

	left.init (q);

	// There isn't always a RHS, for example IN expressions.
	if (right != null)
	{

	    right.init (q);

	}

    }

    /**
     * Get the value of this expression.  This will always return an instance of: 
     * <code>java.lang.Boolean</code> created as the result of a call to:
     * {@link Expression#getValue(Object,Query)}.
     *
     * @param o The object to evaluate the expression on.
     * @param q The Query object.
     * @return An instance of Boolean.
     * @throws QueryExecutionException If the expression cannot be evaluated.
     */
    @Override
	public Object getValue (final Object o,
			    final Query  q)
	                    throws QueryExecutionException
    {

	return Boolean.valueOf (isTrue (o,
					     q));

    }

    /**
     * Get the RHS.
     *
     * @return The RHS of the expression.
     */
    public Expression getRight ()
    {

	return right;

    }

    /**
     * Get the LHS.
     *
     * @return The LHS of the expression.
     */
    public Expression getLeft ()
    {

	return left;

    }

    public void setLeft (final Expression exp)
    {

	left = exp;

    }

    public void setRight (final Expression exp)
    {

	right = exp;

    }

}
