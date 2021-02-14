import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EmptyStackException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GenericStackTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test_EmptyStack() {
		GenericStack<Integer> stack = new GenericStack<>();
		assertTrue(stack.isEmpty());
		assertTrue(stack.getSize() == 0);
		
		assertThrows(EmptyStackException.class,() -> stack.peek());
		assertThrows(EmptyStackException.class,() -> stack.pop());
	}
	
	@Test
	void test_PushPeek() {
		String[] expStr = {"stack: [one]","stack: [one, 2]","stack: [one, 2, 3]",
				           "stack: [one, 2, 3, four]"};
		
		GenericStack<String> stack = new GenericStack<String>();
		String[] testStr = {"one","2","3","four"};
		int expSize = 0;
		assertTrue(stack.getSize() == expSize);
		assertTrue(stack.isEmpty());
		
		for (int i = 0; i < testStr.length; i++) {
			String str = testStr[i];
			stack.push(str);
			expSize++;
			assertTrue(stack.getSize() == expSize);
			assertFalse(stack.isEmpty());
			assertTrue(str.equals(stack.peek()));
			assertTrue(stack.toString().equals(expStr[i]));
		}		
	}

	@Test
	void test_Pop() {
		GenericStack<String> stack = new GenericStack<String>();
		String[] testStr = {"one","2","3","four"};
		int expSize = 0;
		assertTrue(stack.getSize() == expSize);
		assertTrue(stack.isEmpty());
		
		for (String str : testStr) {
			stack.push(str);
		}
		
		expSize = testStr.length;
		for (int i = 0; i < testStr.length; i++) {
			System.out.println("expSize = "+ expSize);
			System.out.println("stackSize = " + stack.getSize());
			assertTrue(stack.getSize() == expSize);
			assertFalse(stack.isEmpty());
			String str = testStr[(testStr.length - 1)-i];
			assertTrue(str.equals(stack.pop()));
			expSize --;
		}
		assertTrue(stack.isEmpty());
		assertTrue(stack.getSize() == expSize);
	}

}
