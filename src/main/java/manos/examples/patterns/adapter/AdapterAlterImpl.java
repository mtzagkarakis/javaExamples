package manos.examples.patterns.adapter;

public class AdapterAlterImpl implements AdapterAlterInterface {
    AdapterInterface adapterInterface;

    public AdapterAlterImpl(AdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
    }

    @Override
    public String getMyName() {
        return calculateAlternaticeImplementation(adapterInterface.getMyName());
    }

    private String calculateAlternaticeImplementation(String baseAdapterImplemantationResult){
        return baseAdapterImplemantationResult.toLowerCase().trim();
    }

}
