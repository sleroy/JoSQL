package net.sf.josql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.josql.Query;
import org.josql.QueryResults;
import org.josql.Result;
import org.josql.exceptions.QueryExecutionException;
import org.josql.exceptions.QueryParseException;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AppTest {
	
	private Map<String, Person> persons;
	private List<Work> works;
	
	@Before
	public void setUp() {
		
		persons = Maps.newHashMap();
		persons.put("jeremie", new Person("guidoux", "jeremie"));
		persons.put("sebastien", new Person("carreau", "sebastien"));
		persons.put("sylvain", new Person("leroy", "sylvain"));
		
		works = Lists.newArrayList(
				new Work(persons.get("jeremie"), persons.get("sylvain"), 8),
				new Work(persons.get("sebastien"), persons.get("sylvain"), 5),
				new Work(persons.get("jeremie"), persons.get("sylvain"), 2)
		);
		
	}
	
	@Test
	public void testGroupBy() throws QueryParseException, QueryExecutionException {
		
		Query q = new Query();
		q.parse("SELECT worker, @total_time "
				+ "FROM net.sf.josql.Work "
				+ "GROUP BY worker "
				+ "EXECUTE ON GROUP_BY_RESULTS sum(time) AS total_time");
		
		QueryResults result = q.execute(works);
		
		System.out.println(result.getResults());
		
		List<Result> results = result.getResultsAsList();
		
		@SuppressWarnings("unchecked")
		List<?> expectedResults = Lists.newArrayList(
				Lists.newArrayList(persons.get("jeremie"), 10.0),
				Lists.newArrayList(persons.get("sebastien"), 5.0)
		);

		assertEquals(2, results.size());
		for (Result row : results) {
			assertTrue(expectedResults.contains(row.getList()));
		}
		
		/*System.out.println(result.getGroupByResults());

		for(Object k : result.getGroupByResults().keySet()) {

			List<?> list1 = (List<?>) result.getGroupByResults().get(k);
			List<?> list2 = (List<String>) list1.get(0); 
			Number value = (Number) list2.get(1);
			System.out.println(list2.get(0) + " " + value);
			
		}*/
		
	}
	
	
}
