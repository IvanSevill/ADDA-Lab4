package common;

public record Spm(Integer a, Integer weight) implements Comparable<Spm> {
	public static Spm of(Integer a, Integer weight) {
		return new Spm(a, weight);
	}

	@Override
	public int compareTo(Spm sp) {
		return this.weight.compareTo(sp.weight);
	}
}