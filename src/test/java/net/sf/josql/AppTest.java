package net.sf.josql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
		
		List<Result> results = result.asList();
		
		@SuppressWarnings("unchecked")
		List<?> expectedResults = Lists.newArrayList(
				Lists.newArrayList(persons.get("jeremie"), 10.0),
				Lists.newArrayList(persons.get("sebastien"), 5.0)
		);

		assertEquals(2, results.size());
		for (Result row : results) {
			assertTrue(expectedResults.contains(row.getList()));
		}
		
	}
	
	@Test
	public void testOrderBy() throws QueryParseException, QueryExecutionException {
		
		Query q = new Query();
		q.parse("SELECT worker, superviser, time "
				+ "FROM net.sf.josql.Work "
				+ "ORDER BY time ASC");
		QueryResults result = q.execute(works);
		System.out.println(result.getResults());
		List<Result> results = result.asList();
		
		List<Work> sortedWorks = Lists.newArrayList();
		sortedWorks.addAll(works);
		Collections.sort(sortedWorks, new Comparator<Work>() {
			public int compare(Work o1, Work o2) {
				if (o1.getTime() < o2.getTime()) {
					return -1;
				}
				if (o1.getTime() > o2.getTime()) {
					return 1;
				}
				return 0;
			}		
		});
		
		for(int i=0; i<sortedWorks.size(); i++) {
			Work expectedWork = sortedWorks.get(i);
			System.out.println(expectedWork.getWorker().getName()+" "+expectedWork.getTime());
			assertEquals(expectedWork.getWorker(), results.get(i).getList().get(0));
			assertEquals(expectedWork.getSuperviser(), results.get(i).getList().get(1));
			assertEquals(expectedWork.getTime(), results.get(i).getList().get(2));
		}
		
	}
	
	
}
