package telran.streams.students;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;

class ColledgeTests {
	private static final String NAME1 = "1";
	private static final String NAME2 = "2";
	private static final String NAME3 = "3";
	private static final int HOURS1 = 100;
	private static final int HOURS2 = 100;
	private static final int HOURS3 = 150;
	private static final int[] MARKS1 = { 60, 70, 80 };
	private static final int[] MARKS2 = { 60, 60, 60 };
	Student st1 = new Student(NAME1, HOURS1, MARKS1);
	Student st2 = new Student(NAME2, HOURS2, MARKS2);
	Student st3 = new Student(NAME3, HOURS3, MARKS2);
	Colledge colledge = new Colledge(new Student[] { st1, st2, st3 });

	@Test
	void sortTest() {
		Student[] expected = { st1, st3, st2 };
		assertArrayEquals(expected, sortStudents(colledge));
	}

	@Test
	void summaryStatisticsHoursTest() {
		IntSummaryStatistics iss = getHoursStatistics(colledge);
		assertEquals(100, iss.getMin());
		assertEquals(150, iss.getMax());
		assertEquals(350, iss.getSum());
	}

	@Test
	void summaryStatisticsMarks() {
		IntSummaryStatistics iss = getMarksStatistics(colledge);
		assertEquals(60, iss.getMin());
		assertEquals(80, iss.getMax());
	}

	private static IntSummaryStatistics getMarksStatistics(Colledge col) {
		IntSummaryStatistics iss = StreamSupport.stream(col.spliterator(), false)
		.map(a -> a.marks())
		.flatMapToInt(a -> Arrays.stream(a))
		.summaryStatistics();
		return iss;
	}

	static private IntSummaryStatistics getHoursStatistics(Colledge col) {
		IntSummaryStatistics iss = StreamSupport.stream(col.spliterator(), false)
				.map(a -> a.hours())
				.collect(Collectors.summarizingInt((Integer::intValue)));
		return iss;
	}

	private static Student[] sortStudents(Colledge col) {
		Student[] students = StreamSupport.stream(col.spliterator(), false)
				.sorted((a1, a2) -> Integer.compare(a2.hours(), a1.hours()))
				.sorted((a1, a2) -> 
				Double.compare(Arrays.stream(a2.marks()).average().getAsDouble(),
						Arrays.stream(a1.marks()).average().getAsDouble()))
				.toArray(Student[]:: new); 
	
		return students;
	}

}
