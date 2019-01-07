package ca.grimoire.formtree.receiver.constructor;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import ca.grimoire.formtree.receiver.annotations.FormField;

public class ConstructorReceiverTest {
	public static class SimpleFields {
		public final String y;
		public final String x;

		public SimpleFields(@FormField("x") String x, @FormField("y") String y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Nested {
		public final SimpleFields inner;

		public Nested(@FormField("inner") SimpleFields inner) {
			this.inner = inner;
		}
	}

	public static class Unit {
		public final String name;
		public final UUID id;
		public final Set<String> abilities;

		public Unit(@FormField("name") String name, @FormField("id") UUID id,
				@FormField("ability") Set<String> abilities) {
			this.name = name;
			this.id = id;
			this.abilities = abilities;
		}
	}

	public static class Units {
		public final List<Unit> units;

		public Units(@FormField("unit") List<Unit> units) {
			this.units = units;
		}
	}

	public static class Collector {
		public final List<String> list;
		public final Set<String> set;
		public final String[] array;

		public Collector(@FormField("list") List<String> list,
				@FormField("set") Set<String> set,
				@FormField("array") String[] array) {
			this.list = list;
			this.set = set;
			this.array = array;
		}
	}

	@Test
	public void unitsGraph() {
		ConstructorReceiver<Units> r = ConstructorReceiver.create(Units.class);

		r.key("unit").index(0).key("name").values(Arrays.asList("alice"));
		r.key("unit").index(0).key("id").values(Arrays.asList("df389cbe-a876-449e-9408-bceae7469f5b"));
		r.key("unit").index(0).key("ability")
				.values(Arrays.asList("hat", "shirt", "lost"));

		r.key("unit").index(1).key("name").values(Arrays.asList("bob"));
		r.key("unit").index(1).key("ability")
				.values(Arrays.asList("helmet", "shirt", "found"));

		Units form = r.finished();

		assertEquals(2, form.units.size());

		Unit zero = form.units.get(0);
		assertEquals("alice", zero.name);
		assertEquals(UUID.fromString("df389cbe-a876-449e-9408-bceae7469f5b"), zero.id);
		assertEquals(
				new HashSet<String>(Arrays.asList("hat", "shirt", "lost")),
				zero.abilities);

		Unit one = form.units.get(1);
		assertEquals("bob", one.name);
		assertEquals(null, one.id);
		assertEquals(
				new HashSet<String>(Arrays.asList("helmet", "shirt", "found")),
				one.abilities);
	}

	@Test
	public void populatesCollectionFields() {
		ConstructorReceiver<Collector> r = ConstructorReceiver
				.create(Collector.class);

		r.key("list").values(Arrays.asList("list one", "list two"));
		r.key("set").values(Arrays.asList("set one", "set two"));
		r.key("array").values(Arrays.asList("array one", "array two"));

		Collector form = r.finished();

		assertEquals(Arrays.asList("list one", "list two"), form.list);
		assertEquals(new HashSet<String>(Arrays.asList("set one", "set two")),
				form.set);
		assertArrayEquals(new String[] { "array one", "array two" }, form.array);
	}

	@Test
	public void populatesSimpleFields() {
		ConstructorReceiver<SimpleFields> r = ConstructorReceiver
				.create(SimpleFields.class);

		r.key("x").values(Arrays.asList("x value"));
		r.key("y").values(Arrays.asList("y value"));

		SimpleFields form = r.finished();

		assertEquals("x value", form.x);
		assertEquals("y value", form.y);
	}

	@Test
	public void populatesSimpleHierarchy() {
		ConstructorReceiver<Nested> r = ConstructorReceiver
				.create(Nested.class);

		r.key("inner").key("x").values(Arrays.asList("x value"));
		r.key("inner").key("y").values(Arrays.asList("y value"));

		Nested form = r.finished();

		assertEquals("x value", form.inner.x);
		assertEquals("y value", form.inner.y);
	}

	@Test
	public void ignoresBareValues() {
		ConstructorReceiver<SimpleFields> r = ConstructorReceiver
				.create(SimpleFields.class);

		r.key("x").values(Arrays.asList("x value"));
		r.key("y").values(Arrays.asList("y value"));

		r.values(Arrays.asList("monkeys"));

		SimpleFields form = r.finished();

		assertEquals("x value", form.x);
		assertEquals("y value", form.y);
	}

	@Test
	public void ignoresUnexpectedKeys() {
		ConstructorReceiver<SimpleFields> r = ConstructorReceiver
				.create(SimpleFields.class);

		r.key("x").values(Arrays.asList("x value"));
		r.key("y").values(Arrays.asList("y value"));

		r.key("monkeys").values(Arrays.asList("banana"));

		SimpleFields form = r.finished();

		assertEquals("x value", form.x);
		assertEquals("y value", form.y);
	}
}
