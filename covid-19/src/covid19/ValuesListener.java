package covid19;

public interface ValuesListener {
    void onValuesChanged(int healthy, int recovered, int infected);
}
