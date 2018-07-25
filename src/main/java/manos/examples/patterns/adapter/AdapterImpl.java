package manos.examples.patterns.adapter;

public class AdapterImpl implements AdapterInterface {
    public static final String NAME = "Manos Tzagkarakis";
    @Override
    public String getMyName() {
        return NAME;
    }
}
